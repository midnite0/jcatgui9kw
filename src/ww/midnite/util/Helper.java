package ww.midnite.util;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.net.URI;
import java.net.URL;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;


public class Helper {

	public static void initLaf() {
		final LookAndFeel laf = UIManager.getLookAndFeel();
		if (laf == null || laf instanceof MetalLookAndFeel) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (final Exception ignore) {
				/* NoOp */
			}
		}
	}


	public static void browse(final URI uri) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
					try {
						Desktop.getDesktop().browse(uri);
					} catch (final Exception ignore) {
						/* ignore */
					}
				}
			}
		}).start();
	}


	public static URL getURL(final URI uri) {
		try {
			return uri.toURL();
		} catch (final Exception e) {
			/* NoOp */
		}

		return null;
	}


	public static URL getURL(final String str) {

		try {
			return new URL(str);
		} catch (final Exception e) {
			/* NoOp */
		}

		return null;
	}


	public static URI getURI(final String str) {
		try {
			return new URI(str);
		} catch (final Exception e) {
			/* NoOp */
		}

		return null;
	}

}
