package ww.midnite.util.file;

import java.io.File;
import java.io.IOException;


public class HomeFile {

	public static final File HOME_JAVA;
	static {
		final String userHome = System.getProperty("user.home");
		final String os = System.getProperty("os.name");

		if (os == null || !os.toLowerCase().contains("mac")) {
			HOME_JAVA = new File(userHome, ".java");
		} else {
			HOME_JAVA = new File(userHome + "/Library/Preferences/", ".java");
		}
	}

	public final File file;


	public HomeFile(final String filename) throws HomeFileException {
		file = initFile(filename);
	}


	public File getFile() {
		return file;
	}


	private File initFile(final String filename) throws HomeFileException {

		/* try to create a file in the user profile directory */
		final File file = createFile(HOME_JAVA, filename);
		if (file.exists() && file.canRead() && file.canWrite()) {
			return file;
		}

		/* use the current directory to store the profile */
		final File fallbackFile = createFile(new File(".java"), filename);
		return fallbackFile;
	}


	private File createFile(final File dir, final String filename) throws HomeFileException {
		/* create the target directory */
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (!dir.canRead()) {
			dir.setReadable(true);
		}
		if (!dir.canWrite()) {
			dir.setWritable(true);
		}

		/* create the target file */
		final File file = new File(dir, filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (final IOException e) {
				throw new HomeFileException("Could not create the file: " + e.getMessage());
			}
		}
		if (!file.canRead()) {
			file.setReadable(true);
		}
		if (!file.canWrite()) {
			file.setWritable(true);
		}

		return file;
	}

	public static class HomeFileException extends Exception {

		private static final long serialVersionUID = 1L;


		public HomeFileException(final String message) {
			super(message);
		}
	}
}
