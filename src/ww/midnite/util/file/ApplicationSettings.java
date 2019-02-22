package ww.midnite.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;


public class ApplicationSettings {

	private static final String XML_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	private static final Pattern DOUBLE = Pattern.compile("[\\+\\-]?[0-9]*\\.?[0-9]+");
	private final Properties props;
	private final File file;


	public ApplicationSettings(final File file) {
		this.file = file;
		props = new Properties();
	}


	public void load() throws ApplicationSettingsException {
		if (file != null && file.exists() && file.length() > 0) {
			InputStream in = null;
			try {
				if (!file.canRead()) {
					file.setReadable(true, true);
				}
				in = new FileInputStream(file);
				props.loadFromXML(in);

			} catch (final Exception e) {
				throw new ApplicationSettingsException("Could not load the application settings: " + e.getMessage());

			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (final IOException ignore) {
						/* NoOp */
					}
				}
			}
		}
	}


	public void store() throws ApplicationSettingsException {
		if (file != null) {

			OutputStream out = null;
			try {
				out = new FileOutputStream(file);
				props.storeToXML(out, new Date().toString());

			} catch (final Exception e) {
				throw new ApplicationSettingsException("Could not store the application settings: " + e.getMessage());

			} finally {
				if (out != null) {
					try {
						out.flush();
					} catch (final IOException ignore) {
						/* NoOp */
					}
					try {
						out.close();
					} catch (final IOException ignore) {
						/* NoOp */
					}
				}
			}
		}
	}


	public String get(final String name) {
		return props.getProperty(name);
	}


	public String get(final String name, final String backupValue) {
		return props.getProperty(name, backupValue);
	}


	public String[] getKeys() {
		final String[] keys = new String[props.size()];
		int i = 0;
		for (final Object key : props.keySet()) {
			keys[i++] = (String) key;
		}
		return keys;
	}


	public Double getAsDouble(final String str, final double backup) {
		return str != null && DOUBLE.matcher(str).matches() ? Double.valueOf(str) : backup;
	}


	public int getAsInt(final String name, final int backupValue) {
		return getAsDouble(get(name, null), backupValue).intValue();
	}


	public int getAsInt(final String name, final int backupValue, final int min, final int max) {
		final int number = getAsInt(name, backupValue);
		return number > max ? max : number < min ? min : number;
	}


	public Boolean getAsBoolean(final String name, final boolean backupValue) {
		return Boolean.valueOf(get(name, String.valueOf(backupValue)));
	}


	public Date getAsDate(final String key, final Date backup) {
		final String value = props.getProperty(key);
		try {
			return value != null ? new SimpleDateFormat(XML_DATE_FORMAT).parse(value) : backup;
		} catch (final ParseException e) {
			return backup;
		}
	}


	public void set(final String name, final Object value) {
		if (value != null) {
			if (value instanceof Date) {
				props.setProperty(name, new SimpleDateFormat(XML_DATE_FORMAT).format((Date) value));
			} else {
				props.setProperty(name, value.toString());
			}
		} else {
			props.remove(name);
		}
	}


	public String remove(final String key) {
		return (String) props.remove(key);
	}


	public void clear() {
		props.clear();
	}

	public static class ApplicationSettingsException extends Exception {

		private static final long serialVersionUID = 1L;


		public ApplicationSettingsException(final String message) {
			super(message);
		}
	}
}
