package ww.midnite.util.model;

public class Time {

	public static final Time[] TIME_CACHE = new Time[30 * 60 + 1];
	static {
		for (int i = 0; i < TIME_CACHE.length; i++) {
			TIME_CACHE[i] = new Time(i);
		}
	}

	private final int seconds;
	private final String text;


	private Time(final int seconds) {
		this.seconds = seconds;
		text = text();
	}


	public static Time valueOf(final int seconds) {
		if (seconds >= 0 && seconds < TIME_CACHE.length) {
			return TIME_CACHE[seconds];
		}

		return new Time(seconds);
	}


	public int getSeconds() {
		return seconds;
	}


	private String text() {
		final int s = seconds % 60;
		final int m = seconds / 60;

		if (m < 60) {
			final StringBuilder sb = new StringBuilder(8);
			sb.append(m);
			sb.append(':');
			if (s < 10) {
				sb.append('0');
			}
			sb.append(s);

			return sb.toString();
		}

		final int m2 = m % 60;
		final int h = m / 60;

		final StringBuilder sb = new StringBuilder(16);
		sb.append(h);
		sb.append(':');
		if (m2 < 10) {
			sb.append('0');
		}
		sb.append(m2);
		sb.append(':');
		if (s < 10) {
			sb.append('0');
		}
		sb.append(s);

		return sb.toString();
	}


	@Override
	public String toString() {
		return text;
	}


	@Override
	public int hashCode() {
		return 31 + seconds;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (seconds != ((Time) obj).seconds) {
			return false;
		}
		return true;
	}

}
