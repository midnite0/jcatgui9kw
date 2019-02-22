package ww.midnite.jcatgui9kw.gui;

import java.awt.Image;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;


public interface Icons {

	String LOGO_PATH = "/resources/logo/";

	ImageIcon INFO_LOGO = new ImageIcon(Icons.class.getResource(LOGO_PATH + "16.png"));

	public static final List<Image> LOGOS = Arrays.asList(new Image[] {
			/**/new ImageIcon(Icons.class.getResource(LOGO_PATH + "16.png")).getImage(),
			/**/new ImageIcon(Icons.class.getResource(LOGO_PATH + "24.png")).getImage(),
			/**/new ImageIcon(Icons.class.getResource(LOGO_PATH + "32.png")).getImage(),
			/**/new ImageIcon(Icons.class.getResource(LOGO_PATH + "48.png")).getImage(),
			/**/new ImageIcon(Icons.class.getResource(LOGO_PATH + "64.png")).getImage(),
			/**/new ImageIcon(Icons.class.getResource(LOGO_PATH + "72.png")).getImage(),
			/**/new ImageIcon(Icons.class.getResource(LOGO_PATH + "96.png")).getImage(),
			/**/new ImageIcon(Icons.class.getResource(LOGO_PATH + "128.png")).getImage()
			/**/ });

	String ICONS_PATH = "/resources/icons/";

	ImageIcon START_KEY = new ImageIcon(Icons.class.getResource(ICONS_PATH + "start-key.png"));

	ImageIcon KEY = new ImageIcon(Icons.class.getResource(ICONS_PATH + "key.png"));
	ImageIcon BALANCE = new ImageIcon(Icons.class.getResource(ICONS_PATH + "balance.png"));
	ImageIcon SOLVED = new ImageIcon(Icons.class.getResource(ICONS_PATH + "solved.png"));
	ImageIcon SKIPPED = new ImageIcon(Icons.class.getResource(ICONS_PATH + "skipped.png"));
	ImageIcon EARNED = new ImageIcon(Icons.class.getResource(ICONS_PATH + "earned.png"));

	ImageIcon SETTINGS = new ImageIcon(Icons.class.getResource(ICONS_PATH + "settings.png"));
	ImageIcon SERVERCHECK = new ImageIcon(Icons.class.getResource(ICONS_PATH + "servercheck.png"));
	ImageIcon INFO = new ImageIcon(Icons.class.getResource(ICONS_PATH + "info.png"));
	ImageIcon UPDATE = new ImageIcon(Icons.class.getResource(ICONS_PATH + "update.png"));
	ImageIcon START = new ImageIcon(Icons.class.getResource(ICONS_PATH + "start.png"));
	ImageIcon STOP = new ImageIcon(Icons.class.getResource(ICONS_PATH + "stop.png"));
	ImageIcon OK = new ImageIcon(Icons.class.getResource(ICONS_PATH + "ok.png"));
	ImageIcon NOT_OK = new ImageIcon(Icons.class.getResource(ICONS_PATH + "not-ok.png"));
	ImageIcon SEND = new ImageIcon(Icons.class.getResource(ICONS_PATH + "send.png"));
	ImageIcon SKIP = new ImageIcon(Icons.class.getResource(ICONS_PATH + "skip.png"));
	ImageIcon ZOOM_IN = new ImageIcon(Icons.class.getResource(ICONS_PATH + "zoom-in.png"));
	ImageIcon ZOOM_OUT = new ImageIcon(Icons.class.getResource(ICONS_PATH + "zoom-out.png"));
	ImageIcon COMMAND = new ImageIcon(Icons.class.getResource(ICONS_PATH + "command.png"));
	ImageIcon EXECUTE = new ImageIcon(Icons.class.getResource(ICONS_PATH + "execute.png"));

	ImageIcon TEXT = new ImageIcon(Icons.class.getResource(ICONS_PATH + "text.png"));
	ImageIcon MOUSE = new ImageIcon(Icons.class.getResource(ICONS_PATH + "mouse.png"));
	ImageIcon CONFIRM = new ImageIcon(Icons.class.getResource(ICONS_PATH + "confirm.png"));

	String WAITING_PATH = "/resources/waiting/";
	ImageIcon ASK = new ImageIcon(Icons.class.getResource(WAITING_PATH + "ask.png"));
	ImageIcon COOL = new ImageIcon(Icons.class.getResource(WAITING_PATH + "cool.png"));
	ImageIcon SLEEP = new ImageIcon(Icons.class.getResource(WAITING_PATH + "sleep.png"));
	ImageIcon WAIT = new ImageIcon(Icons.class.getResource(WAITING_PATH + "wait.png"));

	String PROGRESS_PATH = "/resources/progress/";
	ImageIcon[] PROGRESS = new ImageIcon[] {
			/**/new ImageIcon(Icons.class.getResource(PROGRESS_PATH + "p0.png")),
			/**/new ImageIcon(Icons.class.getResource(PROGRESS_PATH + "p1.png")),
			/**/new ImageIcon(Icons.class.getResource(PROGRESS_PATH + "p2.png")),
			/**/new ImageIcon(Icons.class.getResource(PROGRESS_PATH + "p3.png")),
			/**/new ImageIcon(Icons.class.getResource(PROGRESS_PATH + "p4.png")),
			/**/new ImageIcon(Icons.class.getResource(PROGRESS_PATH + "p5.png")),
			/**/new ImageIcon(Icons.class.getResource(PROGRESS_PATH + "p6.png")),
			/**/new ImageIcon(Icons.class.getResource(PROGRESS_PATH + "p7.png")),
			/**/new ImageIcon(Icons.class.getResource(PROGRESS_PATH + "p8.png")),
			/**/new ImageIcon(Icons.class.getResource(PROGRESS_PATH + "p9.png"))
			/**/ };

}
