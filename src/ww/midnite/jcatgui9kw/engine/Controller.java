package ww.midnite.jcatgui9kw.engine;

import static ww.midnite.jcatgui9kw.Globals.log;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.gui.Gui;
import ww.midnite.jcatgui9kw.gui.GuiListener;
import ww.midnite.jcatgui9kw.gui.Start;
import ww.midnite.jcatgui9kw.gui.StartListener;
import ww.midnite.jcatgui9kw.net.request.GetCaptcha;
import ww.midnite.jcatgui9kw.net.request.RequestFactory;
import ww.midnite.jcatgui9kw.net.request.ShowCaptcha;
import ww.midnite.jcatgui9kw.net.request.VersionCheck;
import ww.midnite.jcatgui9kw.net.response.BalanceQueryResponse;
import ww.midnite.jcatgui9kw.net.response.CaptchaAnswerResponse;
import ww.midnite.jcatgui9kw.net.response.CaptchaDetails;
import ww.midnite.jcatgui9kw.net.response.CaptchaPauseResponse;
import ww.midnite.jcatgui9kw.net.response.CaptchaSkipResponse;
import ww.midnite.jcatgui9kw.net.response.CaptchaType;
import ww.midnite.jcatgui9kw.net.response.CheckApiKeyResponse;
import ww.midnite.jcatgui9kw.net.response.GetCaptchaOkResponse;
import ww.midnite.jcatgui9kw.net.response.GetCaptchaResponse;
import ww.midnite.jcatgui9kw.net.response.Response;
import ww.midnite.jcatgui9kw.net.response.ResponseListener;
import ww.midnite.jcatgui9kw.net.response.ServercheckResponse;
import ww.midnite.jcatgui9kw.net.response.ShowCaptchaResponse;
import ww.midnite.jcatgui9kw.net.response.UnratedCaptchasResponse;
import ww.midnite.jcatgui9kw.net.response.VersionCheckResponse;
import ww.midnite.util.TimeoutTask;
import ww.midnite.util.file.FileContentReader;
import ww.midnite.util.i18n.I18n;
import ww.midnite.util.model.Failure;
import ww.midnite.util.model.Queue;
import ww.midnite.util.model.Time;
import ww.midnite.util.sound.WavePlayer;


public class Controller implements StartListener, GuiListener, ResponseListener {

	private final Start start;
	private final Gui gui;
	private final ThreadPoolExecutor threadPool;
	private final WavePlayer player;
	private final Timer timer;
	private TimerTask answerTimeoutTask;
	private TimerTask balanceQueryTask;
	private TimerTask unratedCaptchasTask;
	private TimerTask servercheckTask;
	private TimerTask sleepingTask;
	private final RequestFactory factory;
	private VersionCheck versionCheck;
	private GetCaptcha getCaptcha;
	private ShowCaptcha showCaptcha;
	private boolean running;
	private Session session;


	public enum AfterCommand {
		NEXT_CAPTCHA, SLEEP, SIGN_OUT, SHUTDOWN, QUIET;
	}


	public Controller() {

		start = new Start(this);
		gui = new Gui(this);

		threadPool = new ThreadPoolExecutor(4, 10, 120, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));

		factory = new RequestFactory(threadPool, this);

		timer = new Timer("Main Timer");

		final byte[] waveFileContent = FileContentReader.readBytes(Globals.FILE_NOTIFY_OWN, Globals.FILE_NOTIFY_INTERNAL);
		player = new WavePlayer(waveFileContent, threadPool);
	}


	/* XXX engine */
	private void igniteEngine(final BalanceQueryResponse response) {
		final int balance = response.getBalance();
		session = Session.getSession(apiKey(), balance);
		session.updateEarned(balance);

		gui.open(apiKey());

		gui.setBalance(response.getCreateTime(), balance);
		gui.setEarned(session.getEarned(), true);

		gui.setSolved(session.getSolved());
		gui.setSkipped(session.getSkipped());
	}


	private void startEngine() {
		running = true;

		getCaptcha();
		initBalanceQueryTask();
		initUnratedCaptchasTask();
		initServercheckTask();

		versionCheck();
	}


	private void shutdownEngine(final boolean loggedIn) {
		running = false;

		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				start.hide();
				gui.hide();

				if (loggedIn) {
					cancelTasks();
				}
				threadPool.shutdown();
				timer.cancel();

				gui.close();
				start.close();

				if (!Globals.SET.getAsBoolean("remember", false)) {
					Globals.SET.set("apiKey", null);
					Globals.SET.set("autostart", null);
				}
				Globals.saveSettings();
			}
		});
	}


	private void initBalanceQueryTask() {
		balanceQueryTask = new TimerTask() {

			@Override
			public void run() {
				balanceQuery();
			}
		};
		timer.schedule(balanceQueryTask, Globals.GET_BALANCE_PERIOD, Globals.GET_BALANCE_PERIOD);
	}


	private void initUnratedCaptchasTask() {
		unratedCaptchasTask = new TimerTask() {

			@Override
			public void run() {
				unratedCaptchas();
			}
		};
		timer.schedule(unratedCaptchasTask, 0, Globals.GET_UNRATED_CAPTCHAS_PERIOD);
	}


	private void initServercheckTask() {
		servercheckTask = new TimerTask() {

			@Override
			public void run() {
				servercheckQuery();
			}
		};
		timer.schedule(servercheckTask, 0, Globals.GET_SERVERCHECK_PERIOD);
	}


	private void cancelTasks() {
		if (answerTimeoutTask != null) {
			answerTimeoutTask.cancel();
			answerTimeoutTask = null;
		}

		if (balanceQueryTask != null) {
			balanceQueryTask.cancel();
			balanceQueryTask = null;
		}

		if (unratedCaptchasTask != null) {
			unratedCaptchasTask.cancel();
			unratedCaptchasTask = null;
		}

		if (servercheckTask != null) {
			servercheckTask.cancel();
			servercheckTask = null;
		}

		if (sleepingTask != null) {
			sleepingTask.cancel();
			sleepingTask = null;
		}

		if (getCaptcha != null) {
			getCaptcha.interrupt();
			getCaptcha = null;
		}

		if (showCaptcha != null) {
			showCaptcha.interrupt();
			showCaptcha = null;
		}

		if (versionCheck != null) {
			versionCheck.interrupt();
			versionCheck = null;
		}

		timer.purge();
	}


	public void setCredentials(final String user, final String pass) {
		Globals.SET.set("user", user);
		Globals.SET.set("pass", pass);

		Globals.SET.set("remember", true);
		Globals.SET.set("autostart", true);

		start.reloadSettings();
	}


	private Boolean autostart() {
		return Globals.SET.getAsBoolean("autostart", false);
	}


	private Boolean updatecheck() {
		return Globals.SET.getAsBoolean("updatecheck", true);
	}


	private String apiKey() {
		return Globals.SET.get("apiKey", "");
	}


	public void start() {
		if (autostart()) {
			checkApiKey();
		} else {
			start.open();
		}
	}


	/* XXX start */
	@Override
	public void startIsOpened() {
		/* do nothing */
	}


	@Override
	public void startIsClosing() {
		shutdownEngine(false);
	}


	@Override
	public void startConfirm() {
		checkApiKey();
		start.hide();
	}


	/* XXX view */
	@Override
	public void guiIsOpened() {
		startEngine();
	}


	@Override
	public void guiIsClosing(final CaptchaDetails captchaDetails) {
		if (captchaDetails.getId() > 0) {
			captchaSkip(captchaDetails, true, AfterCommand.SHUTDOWN);
		} else {
			shutdownEngine(true);
		}
	}


	@Override
	public void guiAnswer(final CaptchaDetails captchaDetails, final String answer, final boolean signOut) {
		session.incSolved();
		captchaAnswer(captchaDetails, answer, signOut);
		gui.setSendingAnswer();
		gui.setSolved(session.getSolved());
	}


	@Override
	public void guiSleepChanged() {
		if (sleepingTask != null) {
			sleepingTask.cancel();
			sleepingTask = null;

			sleepAndGetCaptchaAfter(gui.getIntervalSeconds() * 1000);
		}
	}


	@Override
	public void guiSkip(final CaptchaDetails captchaDetails) {
		session.incSkipped();

		captchaSkip(captchaDetails, false, AfterCommand.NEXT_CAPTCHA);
		gui.setSendingAnswer();

		gui.setSkipped(session.getSkipped());
	}


	@Override
	public void guiStop(final CaptchaDetails captchaDetails) {
		running = false;

		gui.hide();
		cancelTasks();
		gui.close();

		if (captchaDetails.getId() > 0) {
			captchaSkip(captchaDetails, true, AfterCommand.SIGN_OUT);
		} else {
			start.show();
		}
	}


	/* XXX checkApiKey */
	private void checkApiKey() {
		log.info("Check API Key -> request");
		factory.getCheckApiKey().request(apiKey());
	}


	@Override
	public void checkApiKeyResponse(final CheckApiKeyResponse response) {
		if (response.isError()) {
			log.warning("Check API Key <- error: " + response.getError());
			if (isErrorAllowed(response)) {
				checkApiKey();
			} else {
				handleError(I18n.get("login.failed"), response.getError());
			}
			return;
		}

		log.info("Check API Key <- successful" + getTime(response));
		igniteEngine(response);
	}


	/* XXX balance */
	private void balanceQuery() {
		log.fine("Balance query -> request");
		factory.getBalanceQuery().request(apiKey());
	}


	@Override
	public void balanceQueryResponse(final BalanceQueryResponse response) {
		if (response.isError()) {
			log.warning("Balance query <- error: " + response.getError());
			return;
		}

		final int balance = response.getBalance();
		log.fine("Balance query <- " + balance + " credits" + getTime(response));
		session.updateEarned(balance);

		gui.setBalance(response.getCreateTime(), balance);
		gui.setEarned(session.getEarned(), true);
	}


	/* XXX unrated captchas */
	private void unratedCaptchas() {
		log.fine("Unrated captchas -> request");
		factory.getUnratedCaptchas().request(apiKey());
	}


	@Override
	public void unratedCaptchasResponse(final UnratedCaptchasResponse response) {
		if (response.isError()) {
			// log.warning("Unrated captchas <- error: " + response.getError());
			return;
		}

		final int count = response.getCount();
		final String txt = count > 0 ? "--------> " + count + " <--------" : "(" + count + ")";
		log.fine("Unrated captchas <- " + txt + " unrated" + getTime(response));

		gui.setUnrated(count);
	}


	/* XXX servercheck */
	private void servercheckQuery() {
		log.fine("Servercheck -> request");
		factory.getServercheck().request();
	}


	@Override
	public void servercheckResponse(final ServercheckResponse response) {
		if (response.isError()) {
			log.warning("Servercheck <- error: " + response.getError());
			return;
		}

		log.fine("Servercheck <- response " + getTime(response));
		gui.setServercheck(response.getWorker(), response.getInwork(), response.getQueue());
	}


	private String getTime(final Response response) {
		return " (" + response.getResponseTime() / 1000d + "s)";
	}


	/* XXX getCaptcha */
	private void getCaptcha() {

		if (!running) {
			return;
		}

		try {
			Thread.sleep(Globals.GET_CAPTCHA_INTERVAL);
		} catch (final InterruptedException e) {
			log.warning("Get captcha sleep -> Error: " + e.getMessage());
		}

		if (!running) {
			return;
		}

		final boolean text = gui.isText();
		final boolean mouse = gui.isMouse();
		final boolean confirm = gui.isConfirm();

		log.info("Get captcha -> request (" + (!text ? "NO " : "") + "text, " + (!mouse ? "NO " : "") + "click/mouse, " + (!confirm ? "NO " : "") + "confirm" + ")");

		gui.setRequestingProgress(session.incGetCaptchaRequestCount(), text, mouse, confirm);

		getCaptcha = factory.getGetCaptcha();
		getCaptcha.request(apiKey(), text, mouse, confirm);
	}


	@Override
	public void getCaptchaResponse(final GetCaptchaResponse response) {
		session.setLastGetCaptchaResponseTime(System.currentTimeMillis());

		/* already interrupted through sign out? */
		if (!running || getCaptcha == null) {
			log.warning("Get captcha <- interrupted");
			return;
		}
		getCaptcha = null;

		if (response.isTimeoutException()) {
			log.warning("Get captcha <- timeout");

			if (session.incGetCaptchaTimeoutCount() <= Globals.MAX_HTTP_TIMEOUT_COUNT) {
				log.info("Get captcha -> try to get next captcha after 2 seconds");
				sleepAndGetCaptchaAfter(2000);
			} else {
				log.warning("Get captcha -> max count of reconnections has been reached.");
				handleError("Several Get Captcha Errors", response.getError());
			}

			return;
		}

		if (response.isError()) {
			log.warning("Get captcha <- error: " + response.getError());
			if (isErrorAllowed(response)) {
				getCaptcha();
			} else {
				handleError("Get Captcha Error", response.getError());
			}
			return;
		}

		if (response.getCaptchaDetails().getId() <= 0) {
			log.info("Get captcha <- no captcha" + getTime(response));
			getCaptcha();
			return;
		}

		log.info("Get captcha <- captchaId: " + response.getCaptchaDetails().getId() + ", captcha type: " + response.getCaptchaDetails().getType().name() + getTime(response));
		gui.initCaptcha(response.getCaptchaDetails());
		getCaptchaOk(response.getCaptchaDetails());
	}


	/* XXX getCaptchaOk */
	private void getCaptchaOk(final CaptchaDetails captchaDetails) {
		log.info("Get captcha ok -> request ");
		factory.getGetCaptchaOk().request(apiKey(), captchaDetails);
	}


	@Override
	public void getCaptchaOkResponse(final GetCaptchaOkResponse response) {
		session.setLastGetCaptchaOkResponseTime(System.currentTimeMillis());

		/* already interrupted through sign out? */
		if (!running) {
			log.warning("Get captcha ok <- interrupted");
			return;
		}

		if (response.isError()) {
			log.warning("Get captcha ok <- error: " + response.getError());
			if (isErrorAllowed(response)) {
				if (response.isConnectionException()) {
					getCaptchaOk(response.getCaptchaDetails());
				} else {
					getCaptcha();
				}
			} else {
				handleError("Get Captcha Ok Error", response.getError());
			}
			return;
		}

		log.info("Get captcha ok <- " + response.getString() + getTime(response));
		showCaptcha(response.getCaptchaDetails());
	}


	/* XXX showCaptcha */
	private void showCaptcha(final CaptchaDetails captchaDetails) {
		final boolean speed = gui.isSpeed();
		final boolean fastMethod = !speed || captchaDetails.getType() == CaptchaType.CONFIRM_MOUSE;

		log.info("Show captcha -> request captcha image" + (speed ? " (speed)" : "") + (fastMethod ? " (fast method)" : ""));

		session.incShowCaptchaRequestCount();

		showCaptcha = factory.getShowCaptcha();
		showCaptcha.request(apiKey(), speed, captchaDetails, fastMethod);
	}


	@Override
	public void showCaptchaResponse(final ShowCaptchaResponse response) {
		session.setLastShowCaptchaResponseTime(System.currentTimeMillis());

		final long startMillis = System.currentTimeMillis();

		/* already interrupted through sign out? */
		if (!running || showCaptcha == null) {
			log.info("Show captcha <- interrupted");
			return;
		}
		showCaptcha = null;

		if (response.isError() || response.getCaptcha() == null) {
			log.warning("Show captcha <- error: " + response.getError());
			if (isErrorAllowed(response)) {
				if (response.isConnectionException()) {
					showCaptcha(response.getCaptchaDetails());
				} else {
					getCaptcha();
				}
			} else {
				handleError(I18n.get("enqueue.failed"), response.getError());
			}
			return;
		}

		if (gui.isSound()) {
			player.play();
		}

		log.info("Show captcha <- captcha: " + response.getCaptcha() + ", response content-type: " + response.getType() + getTime(response));
		gui.showCaptcha(response.getCaptcha());
		answerTimeoutTask = new TimeoutTask(startMillis, Globals.ANSWER_TIMEOUT) {

			@Override
			public void remain(final long remainMillis) {
				gui.setAnswerTimeout(Time.valueOf(Math.round(remainMillis / 1000f)));
			}


			@Override
			public void timeout() {
				guiStop(response.getCaptchaDetails());
			}
		};
		timer.schedule(answerTimeoutTask, 0, 200);
	}


	/* XXX skip */
	private void captchaSkip(final CaptchaDetails captchaDetails, final boolean autoskip, final AfterCommand afterCommand) {
		if (captchaDetails.isValid()) {
			log.info("Captcha skip -> " + (autoskip ? "autoskip" : "skip") + ", captcha id: " + captchaDetails.getId());
		} else {
			log.info("Captcha pause -> request");
		}
		if (answerTimeoutTask != null) {
			answerTimeoutTask.cancel();
			answerTimeoutTask = null;
		}

		factory.getCaptchaSkip().request(captchaDetails, apiKey(), autoskip, afterCommand);

		timer.purge();
	}


	@Override
	public void captchaSkipResponse(final CaptchaSkipResponse response) {

		if (response.isError()) {
			log.warning("Captcha skip <- error: " + response.getError());
			if (isErrorAllowed(response)) {
				if (response.isConnectionException()) {
					captchaSkip(response.getCaptchaDetails(), response.isAutoskip(), response.getAfterCommand());
				} else {
					getCaptcha();
				}
			} else {
				handleError(I18n.get("skip.failed"), response.getError());
			}
			return;
		}

		if (response.getCaptchaDetails().isValid()) {
			log.info("Captcha skip <- successful" + getTime(response));
		} else {
			log.info("Captcha pause <- successful" + getTime(response));
		}

		switch (response.getAfterCommand()) {
		case NEXT_CAPTCHA:
			prepareForNextCaptcha();
			break;

		case SLEEP:
			sleepAndGetCaptchaAfter(gui.getIntervalSeconds() * 1000);
			break;

		case SIGN_OUT:
			start.show();
			break;

		case SHUTDOWN:
			shutdownEngine(true);
			break;

		case QUIET:
			/* do mothing */
		}
	}


	/* XXX skip */
	private void captchaPause() {
		log.info("Captcha pause -> request");
		factory.getCaptchaPause().request(apiKey());
		timer.purge();
	}


	@Override
	public void captchaPauseResponse(final CaptchaPauseResponse response) {

		long elapsedTime = 0L;

		if (response.isError()) {
			log.warning("Captcha pause <- error: " + response.getError());
		} else {
			elapsedTime = response.getResponseTime();
			log.info("Captcha pause <- successful" + getTime(response));
		}

		sleepAndGetCaptchaAfter(gui.getIntervalSeconds() * 1000 - elapsedTime);
	}


	private void prepareForNextCaptcha() {
		gui.reset();

		final long intervalMillis = gui.getIntervalSeconds() * 1000;
		if (intervalMillis <= 0) {
			getCaptcha();
		} else {
			captchaPause();
		}
	}


	private void sleepAndGetCaptchaAfter(final long sleepingTime) {

		if (sleepingTime <= 0) {
			sleepingTimeoutAftercare();
			return;
		}

		sleepingTask = new TimeoutTask(System.currentTimeMillis(), sleepingTime) {

			@Override
			public void remain(final long remainMillis) {
				gui.setSleepTime(Time.valueOf(Math.round(remainMillis / 1000f)));
			}


			@Override
			public void timeout() {
				sleepingTimeoutAftercare();
			}

		};
		log.info("Sleep " + Time.valueOf((int) (sleepingTime / 1000)));
		timer.schedule(sleepingTask, 0, 1000);
	}


	private void sleepingTimeoutAftercare() {
		gui.setSleepTime(Time.valueOf(0));
		getCaptcha();

		if (sleepingTask != null) {
			sleepingTask.cancel();
			sleepingTask = null;
		}
	}


	/* XXX answer */
	private void captchaAnswer(final CaptchaDetails captchaDetails, final String answer, final boolean signOut) {
		log.info("Captcha answer -> send answer: '" + answer + "', captcha id: " + captchaDetails.getId());
		if (answerTimeoutTask != null) {
			answerTimeoutTask.cancel();
			answerTimeoutTask = null;
		}

		factory.getCaptchaAnswer().request(captchaDetails, answer, apiKey(), signOut);

		timer.purge();
	}


	@Override
	public void captchaAnswerResponse(final CaptchaAnswerResponse response) {
		final long time = System.currentTimeMillis();

		if (response.isError()) {
			log.warning("Captcha answer <- error: " + response.getError());
			if (isErrorAllowed(response)) {
				if (response.isConnectionException()) {
					captchaAnswer(response.getCaptchaDetails(), response.getCaptchaAnswer(), response.isSignOut());
				} else {
					getCaptcha();
				}
			} else {
				handleError(I18n.get("answer.failed"), response.getError());
			}
			return;
		}

		session.incEarned(response.getAnswerPoints());

		final String answerTimes = " [after get/ok/show: " +
		/**/(time - session.getLastGetCaptchaResponseTime()) / 1000d + "s / " +
		/**/(time - session.getLastGetCaptchaOkResponseTime()) / 1000d + "s / " +
		/**/(time - session.getLastShowCaptchaResponseTime()) / 1000d + "s]";
		final String msg = "Captcha answer <- " + response.getAnswer() + answerTimes + getTime(response);
		if (response.isAnswered()) {
			log.info(msg);
		} else {
			log.warning(msg);
		}

		gui.setEarned(session.getEarned(), false);

		if (response.isSignOut()) {
			guiStop(CaptchaDetails.DUMMY);
			start.show();
		} else {
			prepareForNextCaptcha();
		}
	}


	/* XXX versionCheck */
	private void versionCheck() {
		if (updatecheck()) {

			final LocalDateTime lastCheckDate = Globals.SET.getAsLocalDateTime("updatecheck.date", LocalDateTime.now().minusMonths(1));
			if (lastCheckDate.isBefore(LocalDateTime.now().minusHours(2))) {
				versionCheck = factory.getVersionCheck();
				versionCheck.request();
			}
		}
	}


	@Override
	public void versionCheckResponse(final VersionCheckResponse response) {
		if (!response.isError()) {
			Globals.SET.set("updatecheck.date", LocalDateTime.now());
			if (response.getVersion().greaterThan(Globals.VERSION)) {
				gui.updateAvailable(response.getVersion());
			}
		}
	}


	private boolean isErrorAllowed(final Response response) {

		if (!gui.isIgnoreSomeErrors()) {
			return false;
		}

		final Failure error = response.getError();
		if (error == null) {
			return true;
		}

		/* check for allowed error types */
		if (!response.isConnectionException() &&
				(error.getCode() == null || !error.getCode().matches("00(12|14|15|19|20)"))) {
			return false;
		}

		final Queue<LocalDateTime> queue = session.getFailureDatesQueue();
		queue.add(response.getCreateTime());

		if (!queue.full()) {
			return true;
		}

		if (queue.first().isBefore(LocalDateTime.now().minusMinutes(5))) {
			return true;
		}

		return false;
	}


	private void handleError(final String title, final Failure responseError) {
		running = false;

		gui.hide();
		start.show();

		cancelTasks();
		gui.close();

		final String errorMessage = (responseError.getCode() != null ? responseError.getCode() + " " : "") + responseError.getText();
		JOptionPane.showMessageDialog(
				/**/null,
				/**/new JLabel(String.format("<html><b>%s!</b><br><br>Error: %s", title, errorMessage)),
				/**/title,
				/**/JOptionPane.ERROR_MESSAGE);
	}


	@Override
	protected void finalize() throws Throwable {
		start.close();
		gui.close();
		Globals.saveSettings();
	}

}
