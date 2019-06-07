package ww.midnite.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Properties;
import java.util.regex.Pattern;

import ww.midnite.jcatgui9kw.Globals;


public class ApplicationSettings {

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
				props.storeToXML(out, LocalDateTime.now().toString());

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


	public Double get(final String str, final double backup) {
		return str != null && DOUBLE.matcher(str).matches() ? Double.valueOf(str) : backup;
	}


	public int get(final String name, final int backupValue) {
		return get(get(name, (String) null), (double) backupValue).intValue();
	}


	public int getAsInt(final String name, final int backupValue, final int min, final int max) {
		final int number = get(name, backupValue);
		return number > max ? max : number < min ? min : number;
	}


	public Boolean getAsBoolean(final String name, final boolean backupValue) {
		return Boolean.valueOf(get(name, String.valueOf(backupValue)));
	}


	public LocalDateTime getAsLocalDateTime(final String key, final LocalDateTime backup) {
		final String value = props.getProperty(key);
		try {
			return value != null ? LocalDateTime.parse(value, Globals.XML_DATE_FORMAT) : backup;
		} catch (final DateTimeParseException e) {
			return backup;
		}
	}


	public void set(final String name, final Object value) {
		if (value != null) {
			if (value instanceof LocalDateTime) {
				props.setProperty(name, ((LocalDateTime) value).format(Globals.XML_DATE_FORMAT));
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
