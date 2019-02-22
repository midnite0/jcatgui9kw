package ww.midnite.util.i18n;

import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;


public class I18n {

	public static final String LANG = System.getProperty("user.language");

	public static final String PATH = "/resources/lang/";

	public static final String DEFAULT_LANG = "en";
	public static final String DEFAULT_LANG_FILE = PATH + DEFAULT_LANG + ".properties";

	public static final String USER_LANG_FILE = PATH + LANG + ".properties";

	private static final Properties container;

	private static Logger log = null;

	static {
		Properties tmp;
		try {
			tmp = new Properties();
			tmp.load(I18n.class.getResourceAsStream(USER_LANG_FILE));

		} catch (final Exception e1) {
			try {
				tmp = new Properties();
				tmp.load(I18n.class.getResourceAsStream(DEFAULT_LANG_FILE));

			} catch (final Exception e2) {
				tmp = new Properties();
			}
		}

		container = tmp;

		for (final Entry<Object, Object> entry : container.entrySet()) {
			try {
				final String key = entry.getKey().toString();
				final String value = new String(entry.getValue().toString().getBytes("ISO-8859-1"), "UTF-8");
				container.setProperty(key, value);
			} catch (final UnsupportedEncodingException e) {
				/* NoOp */
			}
		}
	}


	public static String get(final String key, final String pp) {
		return pp + get(key) + pp;
	}


	public static String get(final String key) {
		return getProperty(key, key);
	}


	private static String getProperty(final String key, final String def) {
		final String value = container.getProperty(key);
		if (value == null) {

			if (log != null) {
				log.warning("No tranlation for: " + key);
			}
			return def;
		}

		return value;
	}


	public static String get(final String key, final int shortTo) {
		final String text = getProperty(key, key);

		if (text != null && shortTo > 0 && text.length() > shortTo + 1) {
			return text.substring(0, shortTo) + '.';
		}

		return text;
	}


	public static void setLogger(final Logger log0) {
		log = log0;
	}
}
