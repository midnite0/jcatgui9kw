package ww.midnite.jcatgui9kw;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import ww.midnite.util.Helper;
import ww.midnite.util.file.ApplicationSettings;
import ww.midnite.util.file.ApplicationSettings.ApplicationSettingsException;
import ww.midnite.util.file.HomeFile;
import ww.midnite.util.file.HomeFile.HomeFileException;
import ww.midnite.util.i18n.I18n;
import ww.midnite.util.model.Time;
import ww.midnite.util.update.Version;


public class Globals {

	public static final String TITLE = "jCatGUI [9kw edition]";

	public static final Version VERSION = new Version("$version$");

	public static final String FULL_NAME = TITLE + " (v" + VERSION.getShortValue() + ")";

	private static final String SOURCE = "jCatGUI_9kw";

	public static final Logger log = Logger.getLogger("jCatGUI_9kw");
	static {
		final Formatter newFormatter = new Formatter() {

			private final String pattern = "HH:mm:ss.SSS";


			@Override
			public String format(final LogRecord record) {

				final StringBuilder sb = new StringBuilder(64);
				sb.append(new SimpleDateFormat(pattern).format(new Date(record.getMillis()))).append(' ');
				sb.append('[').append(record.getLevel().getName()).append(']').append(' ');
				sb.append(record.getMessage()).append('\n');

				return sb.toString();
			}
		};
		final Handler handler = new ConsoleHandler();
		handler.setFormatter(newFormatter);
		log.addHandler(handler);
		log.setUseParentHandlers(false);

		I18n.setLogger(log);
	}

	public static ApplicationSettings SET = new ApplicationSettings(null);


	public static void loadProfile(final String profile) {
		loadSettings(".jcatgui-9kw" + (profile == null || profile.isEmpty() ? "" : "-" + profile.replaceAll("\\W", "_")));
	}


	private static void loadSettings(final String filename) {
		try {
			final File profile = new HomeFile(filename).getFile();
			log.info("Load settings from profile: " + profile.getAbsolutePath());

			SET = new ApplicationSettings(profile);
			SET.load();

		} catch (final HomeFileException e) {
			log.warning(e.getMessage());

		} catch (final ApplicationSettingsException e) {
			log.warning(e.getMessage());
		}

		log.info("Check periods: " +
		/**/"balance " + GET_BALANCE_PERIOD / 1000 + "s, " +
		/**/"server check " + GET_SERVERCHECK_PERIOD / 1000 + "s, " +
		/**/"unrated captchas " + GET_UNRATED_CAPTCHAS_PERIOD / 1000 + "s.");
	}


	public static void saveSettings() {
		try {
			SET.store();
		} catch (final ApplicationSettingsException e) {
			log.warning(e.getMessage());
		}
	}

	public static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	public static final Cursor CLICK_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

	public static final Cursor TARGET_CURSOR = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

	public static final String HOST_HTTP = "http://www.9kw.eu";

	public static final String HOST_HTTPS = "https://www.9kw.eu";

	public static final long HTTP_TIMEOUT = 30000;

	public static final int HTTP_RETRIES = 2;

	public static final long MAX_HTTP_TIMEOUT_COUNT = 10;

	public static final long ANSWER_TIMEOUT = 29500;

	public static final long CLOSE_TIMEOUT = 5000;

	public static final long GET_CAPTCHA_INTERVAL = 250;

	public static final long GET_BALANCE_PERIOD = 60000;

	public static final long GET_UNRATED_CAPTCHAS_PERIOD = 120000;

	public static final long GET_SERVERCHECK_PERIOD = 10000;

	public static final String SKIP_ANSWER = "";

	public static final int NO_CAPTCHA = -1;

	public static final Dimension[] CAPTCHA_DIMENSIONS = {
			new Dimension(200, 75),
			new Dimension(300, 150),
			new Dimension(450, 300) };

	public static final Time[] PAUSE_SECONDS = new Time[29];
	static {
		int seconds = 0;
		for (int i = 0; i < PAUSE_SECONDS.length; i++) {
			PAUSE_SECONDS[i] = Time.valueOf(seconds);
			seconds += seconds >= 600 ? 300 : seconds >= 300 ? 60 : seconds >= 30 ? 30 : seconds >= 5 ? 5 : 1;
		}
	}

	public static final URL FILE_NOTIFY_URL = Globals.class.getResource("/resources/notify.wav");

	public static final File FILE_NOTIFY_OWN = new File(".", "notify.wav");

	public static final URL FILE_NOTIFY_OWN_URL = Helper.getURL(FILE_NOTIFY_OWN.toURI());


	public static boolean isHttps() {
		return SET.getAsBoolean("useHttps", false);
	}


	private static String host() {
		return isHttps() ? HOST_HTTPS : HOST_HTTP;
	}


	public static URL getUrlBalanceQuery(final String apiKey) {
		return Helper.getURL(host() + "/index.cgi?action=usercaptchaguthaben&apikey=" + apiKey);
	}


	public static URL getUrlGetCaptcha(final String apiKey, final boolean text, final boolean mouse, final boolean confirm) {
		final StringBuilder additional = new StringBuilder("");

		if (text) {
			additional.append("&text=yes");
		} else {
			additional.append("&text=no");
		}

		if (mouse) {
			additional.append("&mouse=1");
		}

		if (confirm) {
			additional.append("&confirm=1");
		}

		return Helper.getURL(host() + "/index.cgi?action=usercaptchanew&source=" + SOURCE + additional.toString() + "&withok=1&extended=1&apikey=" + apiKey);
	}


	public static URL getUrlGetCaptchaOk(final String apiKey) {
		return Helper.getURL(host() + "/index.cgi?action=usercaptchanewok&source=" + SOURCE + "&apikey=" + apiKey);
	}


	public static URL getUrlShowCaptcha(final int captchaId, final boolean speed, final String apiKey, final boolean fastMethod) {
		if (fastMethod) {
			return Helper.getURL(host() + "/grafik/captchas/" + captchaId + ".txt");
		}

		return Helper.getURL(host() + "/index.cgi?action=usercaptchashow&source=" + SOURCE + "&id=" + captchaId + "&apikey=" + apiKey + (speed ? "&speed=1" : ""));
	}


	public static URL getUrlCaptchaSkip(final int captchaId, final String apiKey) {
		return Helper.getURL(host() + "/index.cgi?action=usercaptchaskip&source=" + SOURCE + "&id=" + captchaId + "&apikey=" + apiKey);
	}


	public static URL getUrlCaptchaAutoskip(final int captchaId, final String apiKey) {
		return Helper.getURL(host() + "/index.cgi?action=usercaptchaskip&source=" + SOURCE + "&id=" + captchaId + "&apikey=" + apiKey + "&end=1");
	}


	public static URL getUrlCaptchaPause(final String apiKey) {
		return Helper.getURL(host() + "/index.cgi?action=usercaptchaskip&source=" + SOURCE + "&apikey=" + apiKey + "&end=1");
	}


	public static URL getUrlCaptchaAnswer(final int captchaId, final String answer, final boolean confirm, final String apiKey) {
		return Helper.getURL(host() + "/index.cgi?action=usercaptchacorrect&extended=1&source=" + SOURCE + "&id=" + captchaId + "&captcha=" + answer + (confirm ? "&confirm=1" : "") + "&apikey=" + apiKey);
	}


	public static URL getUrlUnratedCaptchas(final String apiKey) {
		return Helper.getURL(host() + "/index.cgi?action=userhistory&apikey=" + apiKey + "&filter=other");
	}


	public static URL getUrlServercheck() {
		return Helper.getURL(host() + "/index.cgi?action=userservercheck");
	}


	public static URL getUrlCheckForUpdate() {
		return Helper.getURL("https://raw.githubusercontent.com/midnite0/jcatgui9kw/master/jCatGUI_9kw/version");
	}


	public static URI getUriRegister() {
		return Helper.getURI("http://www.9kw.eu/register_en_61.html");
	}


	public static URI getUriForgotApiKey() {
		return Helper.getURI("https://www.9kw.eu/userapi.html");
	}


	public static URI getUriCaptchasHistoryPage() {
		return Helper.getURI("https://www.9kw.eu/index.cgi?action=userhistory&filter=other");
	}


	public static URI getUriHomeLink() {
		return Helper.getURI("http://sites.google.com/site/jcatgui9kw/");
	}


	public static URI getUriIconsLink() {
		return Helper.getURI("http://www.visualpharm.com/");
	}

}
