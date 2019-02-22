package ww.midnite.util;

import java.util.TimerTask;


public abstract class TimeoutTask extends TimerTask {

	private final long startMillis;
	private final long timeoutMillis;


	public TimeoutTask(final long startMillis, final long timeoutMillis) {
		this.startMillis = startMillis;
		this.timeoutMillis = timeoutMillis;
	}


	@Override
	public void run() {
		final long runningMillis = System.currentTimeMillis() - startMillis;
		if (runningMillis > timeoutMillis) {
			timeout();
		} else {
			remain(timeoutMillis - runningMillis);
		}

	}


	public abstract void remain(long remainMillis);


	public abstract void timeout();

}
