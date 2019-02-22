package ww.midnite.jcatgui9kw.engine;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ww.midnite.util.model.Queue;


public class Session {

	private static final Map<String, Session> map = new HashMap<String, Session>();

	private final int startCredits;
	private int solved;
	private int skipped;
	private int earned;
	private long getCaptchaRequestCount;
	private long getCaptchaTimeoutCount;
	private long showCaptchaRequestCount;
	private long lastGetCaptchaResponseTime;
	private long lastGetCaptchaOkResponseTime;
	private long lastShowCaptchaResponseTime;
	private final Queue<Date> failureQueue;


	public static Session getSession(final String namespace, final int credits) {
		synchronized (map) {
			Session session = map.get(namespace);
			if (session == null) {
				session = new Session(credits);
				map.put(namespace, session);
			}

			return session;
		}
	}


	private Session(final int startCredits) {
		this.startCredits = startCredits;
		failureQueue = new Queue<Date>(3);
	}


	public int getStartCredits() {
		return startCredits;
	}


	public void incSolved() {
		solved++;
	}


	public void decSolved() {
		solved--;
	}


	public int getSolved() {
		return solved;
	}


	public void incSkipped() {
		skipped++;
	}


	public int getSkipped() {
		return skipped;
	}


	public void updateEarned(final int credits) {
		earned = credits - startCredits;
	}


	public void incEarned(final int credits) {
		earned += credits;
	}


	public void decEarned(final int credits) {
		earned -= credits;
	}


	public int getEarned() {
		return earned;
	}


	public boolean isAltered() {
		return solved != 0 || skipped != 0 || earned != 0;
	}


	public long incGetCaptchaRequestCount() {
		return ++getCaptchaRequestCount;
	}


	public long incGetCaptchaTimeoutCount() {
		return ++getCaptchaTimeoutCount;
	}


	public long incShowCaptchaRequestCount() {
		return ++showCaptchaRequestCount;
	}


	public long getLastGetCaptchaResponseTime() {
		return lastGetCaptchaResponseTime;
	}


	public void setLastGetCaptchaResponseTime(final long lastGetCaptchaResponseTime) {
		this.lastGetCaptchaResponseTime = lastGetCaptchaResponseTime;
	}


	public long getLastGetCaptchaOkResponseTime() {
		return lastGetCaptchaOkResponseTime;
	}


	public void setLastGetCaptchaOkResponseTime(final long lastGetCaptchaOkResponseTime) {
		this.lastGetCaptchaOkResponseTime = lastGetCaptchaOkResponseTime;
	}


	public long getLastShowCaptchaResponseTime() {
		return lastShowCaptchaResponseTime;
	}


	public void setLastShowCaptchaResponseTime(final long lastShowCaptchaResponseTime) {
		this.lastShowCaptchaResponseTime = lastShowCaptchaResponseTime;
	}


	public Queue<Date> getFailureDatesQueue() {
		return failureQueue;
	}

}
