package ww.midnite.util.update;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Version implements Comparable<Version> {

	private static final Pattern VERSION = Pattern.compile("([0-9]{1,4})(\\.([0-9]{1,4}))?(\\.([0-9]{1,8}))?([a-zA-Z]+)?");

	private final int major;
	private final int minor;
	private final int patch;
	private final String tail;


	public Version(final String version) {
		if (version == null) {
			throw new NullPointerException("Version value must not be null");
		}

		final Matcher matcher = VERSION.matcher(version);
		if (matcher.matches()) {

			final String majorGroup = matcher.group(1);
			major = Integer.parseInt(majorGroup);

			final String minorGroup = matcher.group(3);
			if (minorGroup != null) {
				minor = Integer.parseInt(minorGroup);
			} else {
				minor = 0;
			}

			final String patchGroup = matcher.group(5);
			if (patchGroup != null) {
				patch = Integer.parseInt(patchGroup);
			} else {
				patch = 0;
			}

			final String tailGroup = matcher.group(6);
			if (tailGroup != null) {
				tail = tailGroup;
			} else {
				tail = "";
			}

		} else {
			major = 0;
			minor = 0;
			patch = 0;
			tail = "";
		}
	}


	public Version(final int major, final int minor, final int patch, final String tail) {
		this.major = major > 0 ? major : 0;
		this.minor = minor > 0 ? minor : 0;
		this.patch = patch > 0 ? patch : 0;
		this.tail = tail != null ? tail : "";
	}


	public boolean greaterThan(final Version ver) {
		return compareTo(ver) < 0;
	}


	public boolean lowerThan(final Version ver) {
		return compareTo(ver) > 0;
	}


	@Override
	public int compareTo(final Version ver) {

		if (ver == null) {
			return -1;
		}

		int majorDiff = ver.major - major;
		if (majorDiff != 0) {
			return majorDiff;
		}

		int minorDiff = ver.minor - minor;
		if (minorDiff != 0) {
			return minorDiff;
		}

		int patchDiff = ver.patch - patch;
		if (patchDiff != 0) {
			return patchDiff;
		}

		return ver.tail.compareToIgnoreCase(tail);
	}


	@Override
	public boolean equals(final Object obj) {
		return obj != null && obj instanceof Version && compareTo((Version) obj) == 0;
	}


	@Override
	public String toString() {
		return getValue();
	}


	public String getValue() {
		return major + "." + minor + (patch > 0 ? "." + patch : "") + tail;
	}


	public String getShortValue() {
		return major + (minor > 0 || patch > 0 ? "." + minor + (patch > 0 ? "." + patch : "") : "") + tail;
	}
}
