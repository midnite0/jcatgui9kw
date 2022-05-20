package ww.midnite.util.file;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class FileContentReader {

	public static byte[] readBytes(final String file, final String fallback) {
		final byte[] result = readBytes(file);

		if (result != null) {
			return result;
		}

		return readBytes(fallback);
	}


	public static byte[] readBytes(final String file) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = getInputStream(file);

			final byte[] buf = new byte[4096];
			int read = 0;
			while ((read = is.read(buf)) > 0) {
				baos.write(buf, 0, read);
			}

			return baos.toByteArray();

		} catch (final IOException e) {
			return null;

		} finally {
			close(baos);
			close(is);
		}
	}


	private static void close(final Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (final IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}


	private static InputStream getInputStream(final String file) throws FileNotFoundException {
		if (file == null || file.isEmpty()) {
			return null;
		}

		if (file.startsWith("/")) {
			return FileContentReader.class.getResourceAsStream(file);
		}

		return new FileInputStream(file);
	}
}
