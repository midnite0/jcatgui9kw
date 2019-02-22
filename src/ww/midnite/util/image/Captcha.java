package ww.midnite.util.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Captcha {

	public static final byte[] ANIMATED_KEY = { 0x21, (byte) 0xFF, 0x0B, 0x4E, 0x45, 0x54, 0x53, 0x43, 0x41, 0x50, 0x45, 0x32, 0x2E, 0x30 };

	private final Image image;
	private final int imageWidth;
	private final int imageHeight;

	private final String mimeType;
	private final String magicType;
	private final boolean animated;


	public Captcha(final byte[] raw, final String mimeType) {

		this.mimeType = mimeType;

		if (raw == null) {
			image = null;
			imageWidth = 0;
			imageHeight = 0;
			magicType = null;
			animated = false;

			return;
		}

		magicType = getMagicType(raw);
		animated = isAnimated(raw);

		image = getImage(raw);
		if (image == null) {
			imageWidth = 0;
			imageHeight = 0;
			return;
		}

		imageWidth = image.getWidth(null);
		imageHeight = image.getHeight(null);

	}


	private String getMagicType(final byte[] raw) {

		final String header = new String(raw, 0, 16, Charset.forName("iso-8859-1"));

		if (header.substring(0, 3).equals("GIF")) {
			return "GIF";
		}

		if (header.substring(1, 4).equals("PNG")) {
			return "PNG";
		}

		if (header.substring(0, 2).equals("BM")) {
			return "BMP";
		}

		if (header.substring(0, 4).equals("ÿØÿà")) {
			return "JPG";
		}

		return mimeType;
	}


	private boolean isAnimated(final byte[] raw) {
		if (!magicType.equals("GIF")) {
			return false;
		}

		for (int i = 0; i < raw.length; i++) {
			if (isKeyPresent(raw, i, ANIMATED_KEY)) {
				return true;
			}
		}

		return false;
	}


	private boolean isKeyPresent(final byte[] master, final int shift, final byte[] key) {

		if (master.length < shift + key.length) {
			return false;
		}

		for (int i = 0; i < ANIMATED_KEY.length; i++) {
			if (master[shift + i] != ANIMATED_KEY[i]) {
				return false;
			}
		}

		return true;
	}


	private Image getImage(final byte[] raw) {
		if (animated) {
			return new ImageIcon(raw).getImage();
		}

		return bufferedImage(raw);
	}


	private static Image bufferedImage(final byte[] imgBytes) {
		final ByteArrayInputStream imgBytesIn = new ByteArrayInputStream(imgBytes, 0, imgBytes.length);
		final BufferedInputStream imgIn = new BufferedInputStream(imgBytesIn);

		try {
			return ImageIO.read(imgIn);
		} catch (final Exception e) {
			return null;
		}
	}


	public ImageIcon getImageIcon() {
		if (image == null) {
			return new ImageIcon();
		}

		return new ImageIcon(image);
	}


	public ImageIcon getImageIcon(final double zoom, final boolean smooth) {
		final Image image = getImage(zoom, smooth);
		if (image == null) {
			return new ImageIcon();
		}

		return new ImageIcon(image);
	}


	public Image getImage() {
		return image;
	}


	public Image getImage(final double zoom, final boolean smooth) {
		if (image == null || zoom == 1d || zoom <= 0d) {
			return image;
		}

		final int scaledWidth = (int) Math.round(imageWidth * zoom);
		final int scaledHeight = (int) Math.round(imageHeight * zoom);

		return getImage(scaledWidth, scaledHeight, smooth);
	}


	public Image getImage(final int scaledWidth, final int scaledHeight, final boolean smooth) {
		if (image == null) {
			return image;
		}

		if (scaledWidth == imageWidth && scaledHeight == imageHeight) {
			return image;
		}

		if (animated || magicType.contains("GIF")) {
			return image.getScaledInstance(scaledWidth, scaledHeight, !smooth || animated ? Image.SCALE_DEFAULT : Image.SCALE_SMOOTH);

		} else {
			final BufferedImage scaledBufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g2 = (Graphics2D) scaledBufferedImage.getGraphics();
			if (smooth) {
				g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			} else {
				g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			}
			g2.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
			g2.dispose();

			return scaledBufferedImage;
		}
	}


	@Override
	public String toString() {
		return (animated ? "Animated " : "") + magicType + ' ' + imageWidth + 'x' + imageHeight;
	}


	public int getWidth() {
		return imageWidth;
	}


	public int getHeight() {
		return imageHeight;
	}
}
