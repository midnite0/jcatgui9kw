package ww.midnite.jcatgui9kw.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.CaptchaDetails;
import ww.midnite.jcatgui9kw.net.response.CaptchaType;
import ww.midnite.util.Helper;
import ww.midnite.util.i18n.I18n;
import ww.midnite.util.image.Captcha;
import ww.midnite.util.model.Time;
import ww.midnite.util.model.Zoom;
import ww.midnite.util.update.Version;


public class Gui {

	private final GuiListener viewListener;

	private final JFrame frame;
	private final JButton close;
	private final JPanel framePanel;
	private final JLabel apiKey;
	private final JLabel balance;
	private final JLabel solved;
	private final JLabel skipped;
	private final JLabel earned;
	private final JLabel servercheck;
	private final JButton stop;

	private JPanel userStatisticsPanel;

	private final JPanel captchaPanel;
	private final CaptchaLabel captchaLabel;
	private final JTextField answer;
	private final JLabel confirmAnswerLabel;

	private final MouseCaptchaAdapter answerMouseClickAdapter;
	private final UnratedCaptchasAdapter unratedCaptchasAdapter;

	private final JLabel tabToFocusLabel;

	private final JButton sendYes;
	private final JButton sendNo;
	private final JButton send;
	private final JButton skip;

	private final JPanel zoomPanel;
	private final JButton zoomIn;
	private final JButton zoomOut;
	private final JButton zoomReset;
	private final JLabel zoomLabel;

	private final JLabel settingsLabel;
	private final JPanel settingsPanel;

	private final JLabel servercheckLabel;
	private final JPanel servercheckPanel;

	private final JLabel infoLabel;
	private final JPanel infoPanel;

	private final JLabel updateLabel;
	private final JPanel updatePanel;

	private final JLabel updateInfoLabel;
	private final JLabel downloadLabel;

	private final JLabel statusLabel;
	private final JLabel unratedLabel;

	private final JPanel requestPanel;
	private final JLabel requestTextLabel;
	private final JLabel requestMouseLabel;
	private final JLabel requestConfirmLabel;
	private final JLabel requestProgressLabel;

	private final JPanel responsePanel;

	private final JComboBox<String> positionX;
	private final JComboBox<String> positionY;
	private final JCheckBox smooth;
	private final JCheckBox ontop;
	private final JCheckBox sound;

	private final JCheckBox minimize;
	private final JCheckBox maximize;
	private final JCheckBox tabToFocus;

	private final JComboBox<Time> interval;
	private final JCheckBox speed;

	private final JCheckBox textCaptchas;
	private final JCheckBox clickCaptchas;
	private final JCheckBox confirmCaptchas;

	private final JCheckBox ignoreSomeErrors;

	private final ZoomWrapper[] zooms;

	private CaptchaDetails captchaDetails;
	private Captcha captcha;

	private JLabel home;

	private JLabel icons;


	public Gui(final GuiListener viewListener) {

		this.viewListener = viewListener;

		frame = new JFrame(Globals.TITLE);
		frame.setIconImages(Icons.LOGOS);
		frame.setResizable(false);
		framePanel = new JPanel(new BorderLayout(0, 4));
		framePanel.setBorder(BorderFactory.createEmptyBorder(2, 4, 0, 4));

		close = new JButton();
		toJLabel(close);

		apiKey = new JLabel("???????????", Icons.KEY, JLabel.LEFT);
		apiKey.setToolTipText(I18n.get("apiKey"));
		balance = new JLabel("??????", Icons.BALANCE, JLabel.LEFT);
		balance.setToolTipText(I18n.get("balance"));
		solved = new JLabel("????", JLabel.LEFT);
		skipped = new JLabel("????", JLabel.LEFT);
		earned = new JLabel("????", JLabel.LEFT);
		stop = new JButton(I18n.get("stop"), Icons.STOP);
		stop.setFont(stop.getFont().deriveFont(Font.BOLD, stop.getFont().getSize() - 2));
		servercheck = new JLabel("worker:? inwork:? queue:?", JLabel.CENTER);

		captchaPanel = new JPanel();
		captchaLabel = new CaptchaLabel();
		captchaLabel.setHorizontalAlignment(JTextField.CENTER);

		answerMouseClickAdapter = new MouseCaptchaAdapter();
		answerMouseClickAdapter.setEnabled(false);

		unratedCaptchasAdapter = new UnratedCaptchasAdapter(this);

		tabToFocusLabel = new JLabel(" ");
		tabToFocusLabel.setFocusable(true);

		answer = new JTextField(16);
		answer.setHorizontalAlignment(JTextField.CENTER);
		answer.setFont(answer.getFont().deriveFont(Font.PLAIN, 18));

		confirmAnswerLabel = new JLabel("");
		confirmAnswerLabel.setFont(confirmAnswerLabel.getFont().deriveFont(Font.BOLD, 24));
		confirmAnswerLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(224, 196, 128), 2), BorderFactory.createEmptyBorder(4, 16, 4, 16)));
		confirmAnswerLabel.setFocusable(false);
		confirmAnswerLabel.setVisible(false);

		sendYes = new JButton(I18n.get("true"), Icons.OK);
		sendYes.setVisible(false);
		sendYes.setFont(sendYes.getFont().deriveFont(Font.BOLD, 16));

		sendNo = new JButton(I18n.get("false"), Icons.NOT_OK);
		sendNo.setVisible(false);
		sendNo.setFont(sendNo.getFont().deriveFont(Font.BOLD, 16));

		send = new JButton(I18n.get("send"), Icons.SEND);
		send.setToolTipText(I18n.get("send"));
		send.setEnabled(false);
		send.setFont(send.getFont().deriveFont(Font.BOLD, 16));

		skip = new JButton(Icons.SKIP);
		skip.setToolTipText(I18n.get("skip"));
		skip.setEnabled(false);

		zoomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
		zoomPanel.setVisible(false);

		zoomIn = new JButton(Icons.ZOOM_IN);
		zoomIn.setEnabled(false);
		toJLabel(zoomIn);

		zoomOut = new JButton(Icons.ZOOM_OUT);
		zoomOut.setEnabled(false);
		toJLabel(zoomOut);

		zoomReset = new JButton();
		zoomReset.setEnabled(false);
		toJLabel(zoomReset);

		zoomLabel = new JLabel(new Zoom(1d).toString(), JLabel.RIGHT);
		zoomLabel.setFont(zoomLabel.getFont().deriveFont(Font.PLAIN, zoomLabel.getFont().getSize() - 2));

		settingsLabel = new JLabel(Icons.SETTINGS);
		settingsLabel.setToolTipText(" " + I18n.get("settings") + " ");

		settingsPanel = new JPanel();
		settingsPanel.setBorder(BorderFactory.createTitledBorder(I18n.get("settings")));
		final BoxLayout settingsPanelLayout = new BoxLayout(settingsPanel, BoxLayout.PAGE_AXIS);
		settingsPanel.setLayout(settingsPanelLayout);

		servercheckLabel = new JLabel(Icons.SERVERCHECK);
		servercheckLabel.setToolTipText(" " + I18n.get("servercheck") + " ");

		servercheckPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		servercheckPanel.setBorder(BorderFactory.createTitledBorder("Servercheck"));

		infoLabel = new JLabel(Icons.INFO);
		infoLabel.setToolTipText(" " + I18n.get("info") + " ");

		infoPanel = new JPanel(new GridLayout(3, 1));
		infoPanel.setBorder(BorderFactory.createTitledBorder(I18n.get("info")));

		updateLabel = new JLabel(Icons.UPDATE);
		updateLabel.setToolTipText(" " + I18n.get("update") + " ");
		updateLabel.setVisible(false);

		updatePanel = new JPanel(new BorderLayout());
		updatePanel.setBorder(BorderFactory.createTitledBorder(I18n.get("update")));

		updateInfoLabel = new JLabel("", JLabel.CENTER);

		statusLabel = new JLabel("", JLabel.RIGHT);
		statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, statusLabel.getFont().getSize() - 1));

		unratedLabel = new JLabel("   ", JLabel.LEFT);
		unratedLabel.setCursor(Globals.CLICK_CURSOR);

		requestPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 1, 0));
		requestPanel.setVisible(false);

		requestTextLabel = new JLabel(Icons.TEXT);
		requestMouseLabel = new JLabel(Icons.MOUSE);
		requestConfirmLabel = new JLabel(Icons.CONFIRM);
		requestProgressLabel = new JLabel(" ", JLabel.LEFT);

		responsePanel = new JPanel(new BorderLayout());
		responsePanel.setVisible(false);

		downloadLabel = new JLabel("<html><a href=\"#\">" + I18n.get("downloadnow") + "</a></html>", JLabel.CENTER);
		downloadLabel.setFont(downloadLabel.getFont().deriveFont(Font.BOLD));
		downloadLabel.setForeground(Color.BLUE);
		downloadLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		smooth = new JCheckBox(I18n.get("smooth"));
		smooth.setToolTipText(I18n.get("smooth.info"));
		positionX = new JComboBox<String>(new String[] { I18n.get("left"), I18n.get("center"), I18n.get("right") });
		positionX.setToolTipText(I18n.get("position.x.info"));
		positionY = new JComboBox<String>(new String[] { I18n.get("top"), I18n.get("bottom") });
		positionY.setToolTipText(I18n.get("position.y.info"));

		ontop = new JCheckBox(I18n.get("ontop"));
		ontop.setToolTipText(I18n.get("ontop.info"));

		minimize = new JCheckBox(I18n.get("minimize"));
		minimize.setToolTipText(I18n.get("minimize.info"));

		maximize = new JCheckBox(I18n.get("maximize"));
		maximize.setToolTipText(I18n.get("maximize.info"));

		sound = new JCheckBox(I18n.get("sound"));
		sound.setToolTipText(I18n.get("sound.info"));

		tabToFocus = new JCheckBox(I18n.get("press.tab"));
		tabToFocus.setToolTipText(I18n.get("press.tab.info"));

		interval = new JComboBox<Time>(Globals.PAUSE_SECONDS);

		speed = new JCheckBox(I18n.get("speed"));
		speed.setToolTipText(I18n.get("speed.info"));

		textCaptchas = new JCheckBox(I18n.get("text.captcha"));
		textCaptchas.setToolTipText(I18n.get("text.captcha.info"));

		clickCaptchas = new JCheckBox(I18n.get("click.captcha"));
		clickCaptchas.setToolTipText(I18n.get("click.captcha.info"));

		ignoreSomeErrors = new JCheckBox(I18n.get("ignore.some.errors"));
		ignoreSomeErrors.setToolTipText(I18n.get("ignore.some.errors.info"));

		confirmCaptchas = new JCheckBox(I18n.get("confirm.captcha"));
		confirmCaptchas.setToolTipText(I18n.get("confirm.captcha.info"));

		zooms = new ZoomWrapper[Globals.CAPTCHA_DIMENSIONS.length + 1];

		layout();
		actions();
	}


	private void toJLabel(final JButton button) {
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setContentAreaFilled(false);
		button.setBorder(null);
		button.setBorderPainted(false);
		button.setOpaque(false);
		button.setFocusPainted(false);
	}


	private void layout() {

		final JPanel userTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 4));
		userTopPanel.add(apiKey);
		userTopPanel.add(new JLabel(" "));
		userTopPanel.add(balance);
		userTopPanel.add(new JLabel("  "));
		userTopPanel.add(stop);
		userTopPanel.add(close);

		userStatisticsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 4));
		userStatisticsPanel.add(new JLabel(I18n.get("solved") + ":", Icons.SOLVED, JLabel.RIGHT));
		userStatisticsPanel.add(solved);
		userStatisticsPanel.add(new JLabel(" "));
		userStatisticsPanel.add(new JLabel(I18n.get("skipped") + ":", Icons.SKIPPED, JLabel.RIGHT));
		userStatisticsPanel.add(skipped);
		userStatisticsPanel.add(new JLabel(" "));
		userStatisticsPanel.add(new JLabel(I18n.get("earned") + ":", Icons.EARNED, JLabel.RIGHT));
		userStatisticsPanel.add(earned);

		final JPanel userPanel = new JPanel(new BorderLayout(2, 2));
		userPanel.add(userTopPanel, BorderLayout.NORTH);
		userPanel.add(userStatisticsPanel, BorderLayout.SOUTH);

		zoomPanel.add(zoomIn);
		zoomPanel.add(zoomOut);
		zoomPanel.add(zoomReset);
		zoomPanel.add(zoomLabel);

		final JPanel captchaLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		captchaLabelPanel.add(captchaLabel);

		captchaPanel.setLayout(new BorderLayout(0, 0));
		captchaPanel.add(zoomPanel, BorderLayout.NORTH);
		captchaPanel.add(captchaLabelPanel, BorderLayout.CENTER);

		final JPanel answerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 2));
		answerPanel.add(tabToFocusLabel);
		answerPanel.add(answer);
		answerPanel.add(confirmAnswerLabel);
		answerPanel.add(new JLabel(" "));

		final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.add(sendYes);
		buttonsPanel.add(sendNo);
		buttonsPanel.add(send);

		final JPanel secondButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		secondButtonsPanel.add(skip);

		responsePanel.add(answerPanel, BorderLayout.NORTH);
		responsePanel.add(buttonsPanel, BorderLayout.CENTER);
		responsePanel.add(secondButtonsPanel, BorderLayout.SOUTH);

		final JPanel menuPanel = new JPanel(new BorderLayout());
		final JPanel menuSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		menuSubPanel.add(settingsLabel);
		menuSubPanel.add(infoLabel);
		menuSubPanel.add(servercheckLabel);
		menuSubPanel.add(updateLabel);
		menuSubPanel.add(unratedLabel);

		requestPanel.add(requestTextLabel);
		requestPanel.add(requestMouseLabel);
		requestPanel.add(requestConfirmLabel);
		requestPanel.add(requestProgressLabel);

		menuPanel.add(menuSubPanel, BorderLayout.WEST);
		menuPanel.add(statusLabel, BorderLayout.CENTER);
		menuPanel.add(requestPanel, BorderLayout.EAST);

		final JPanel firstLineSettings = new JPanel();
		firstLineSettings.add(new JLabel(I18n.get("captchas.receive") + ":", JLabel.RIGHT));
		firstLineSettings.add(textCaptchas);
		firstLineSettings.add(clickCaptchas);
		firstLineSettings.add(confirmCaptchas);

		final JPanel secondLineSettings = new JPanel();
		secondLineSettings.add(new JLabel(I18n.get("pause") + ":", JLabel.RIGHT));
		secondLineSettings.add(interval);
		secondLineSettings.add(speed);

		final JPanel thirdLineSettings = new JPanel();
		thirdLineSettings.add(new JLabel(I18n.get("position.scalling") + ":", JLabel.RIGHT));
		thirdLineSettings.add(positionX);
		thirdLineSettings.add(positionY);

		final JPanel fourthLineSettings = new JPanel();
		fourthLineSettings.add(minimize);
		fourthLineSettings.add(maximize);
		fourthLineSettings.add(ontop);

		final JPanel fifthLineSettings = new JPanel();
		fifthLineSettings.add(sound);
		fifthLineSettings.add(smooth);
		fifthLineSettings.add(tabToFocus);

		final JPanel sixthLineSettings = new JPanel();
		sixthLineSettings.add(ignoreSomeErrors);

		settingsPanel.add(firstLineSettings);
		settingsPanel.add(secondLineSettings);
		settingsPanel.add(thirdLineSettings);
		settingsPanel.add(fourthLineSettings);
		settingsPanel.add(fifthLineSettings);
		settingsPanel.add(sixthLineSettings);

		final JLabel titleLabel = new JLabel("Version: " + Globals.VERSION.getValue(), JLabel.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		infoPanel.add(titleLabel);
		home = new JLabel("<html><a href='#'>" + Globals.TITLE + "</a> (c) midnite</html>", Icons.INFO_LOGO, JLabel.CENTER);
		home.setCursor(new Cursor(Cursor.HAND_CURSOR));
		infoPanel.add(home);

		icons = new JLabel("<html>Icons (c) <a href='#'>VisualPharm.com</a></html>", JLabel.CENTER);
		icons.setCursor(new Cursor(Cursor.HAND_CURSOR));
		infoPanel.add(icons);

		updatePanel.add(updateInfoLabel, BorderLayout.CENTER);
		updatePanel.add(downloadLabel, BorderLayout.SOUTH);

		final JPanel clickPanel = new JPanel();
		final BoxLayout clickPanelLayout = new BoxLayout(clickPanel, BoxLayout.Y_AXIS);
		clickPanel.setLayout(clickPanelLayout);

		servercheckPanel.add(servercheck);

		clickPanel.add(servercheckPanel);
		clickPanel.add(settingsPanel);
		clickPanel.add(infoPanel);
		clickPanel.add(updatePanel);

		final JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(responsePanel, BorderLayout.NORTH);
		bottomPanel.add(menuPanel, BorderLayout.CENTER);
		bottomPanel.add(clickPanel, BorderLayout.SOUTH);

		final JPanel evenBottomPanel = new JPanel(new BorderLayout());
		evenBottomPanel.add(bottomPanel, BorderLayout.CENTER);

		framePanel.add(userPanel, BorderLayout.NORTH);
		framePanel.add(captchaPanel, BorderLayout.CENTER);
		framePanel.add(evenBottomPanel, BorderLayout.SOUTH);

		servercheckPanel.setVisible(false);
		settingsPanel.setVisible(false);
		infoPanel.setVisible(false);
		updatePanel.setVisible(false);

		frame.add(framePanel);
	}


	private void actions() {
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent e) {
				viewListener.guiIsClosing(captchaDetails);
			}
		});

		addActionAndShortkey(close, KeyEvent.VK_Q, new AbstractAction() {

			private static final long serialVersionUID = 1L;


			@Override
			public void actionPerformed(final ActionEvent e) {
				viewListener.guiIsClosing(captchaDetails);
			}
		});

		balance.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				userStatisticsPanel.setVisible(!userStatisticsPanel.isVisible());
				pack();
			}
		});

		addActionAndShortkey(stop, KeyEvent.VK_W, new AbstractAction() {

			private static final long serialVersionUID = 1L;


			@Override
			public void actionPerformed(final ActionEvent e) {
				stop();
			}
		});

		answer.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(answer.getText().trim(), e.isControlDown());
				}
			}
		});

		captchaLabel.addMouseListener(answerMouseClickAdapter);

		unratedLabel.addMouseListener(unratedCaptchasAdapter);

		sendYes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				send("yes", isCtrlDown(e));
			}
		});

		sendNo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				send("no", isCtrlDown(e));
			}
		});

		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				send(answer.getText().trim(), isCtrlDown(e));
			}

		});

		addActionAndShortkey(skip, KeyEvent.VK_S, new AbstractAction() {

			private static final long serialVersionUID = 1L;


			@Override
			public void actionPerformed(final ActionEvent e) {
				skip();
			}
		});

		final ItemListener positionListener = new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {

				if (e.getStateChange() != ItemEvent.SELECTED) {
					return;
				}

				final FlowLayout layout = new FlowLayout(0, 0, 0);
				switch (positionX.getSelectedIndex()) {
				case 0:
					layout.setAlignment(FlowLayout.LEFT);
					break;

				case 1:
					layout.setAlignment(FlowLayout.CENTER);
					break;

				case 2:
					layout.setAlignment(FlowLayout.RIGHT);
					break;
				}

				switch (positionY.getSelectedIndex()) {
				case 0:
					captchaPanel.add(zoomPanel, BorderLayout.NORTH);
					break;

				case 1:
					captchaPanel.add(zoomPanel, BorderLayout.SOUTH);
					break;
				}

				zoomPanel.setLayout(layout);
				captchaPanel.revalidate();
			}
		};

		positionX.addItemListener(positionListener);
		positionY.addItemListener(positionListener);

		smooth.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (captchaDetails == CaptchaDetails.NO_CAPTCHA) {
					return;
				}

				if (captcha != null) {
					setCaptcha(captcha, getZoom(captcha).value.getFactor(), smooth.isSelected());
					pack();
				}

			}

		});

		ontop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				frame.setAlwaysOnTop(ontop.isSelected());
			}
		});

		interval.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					final Time time = (Time) e.getItem();
					if (time.getSeconds() == 0) {
						speed.setEnabled(true);
					} else {
						speed.setSelected(false);
						speed.setEnabled(false);
					}

					viewListener.guiSleepChanged();
				}
			}
		});

		addActionAndShortkey(zoomIn, KeyEvent.VK_PLUS, new AbstractAction() {

			private static final long serialVersionUID = 1L;


			@Override
			public void actionPerformed(final ActionEvent e) {
				zoomIn();
			}
		});

		addActionAndShortkey(zoomOut, KeyEvent.VK_MINUS, new AbstractAction() {

			private static final long serialVersionUID = 1L;


			@Override
			public void actionPerformed(final ActionEvent e) {
				zoomOut();
			}
		});

		addActionAndShortkey(zoomReset, KeyEvent.VK_0, new AbstractAction() {

			private static final long serialVersionUID = 1L;


			@Override
			public void actionPerformed(final ActionEvent e) {
				zoomReset();
			}
		});

		addShortkey(sendYes, KeyEvent.VK_Y, new AbstractAction() {

			private static final long serialVersionUID = 1L;


			@Override
			public void actionPerformed(final ActionEvent e) {
				send("yes", false);
			}
		});

		addShortkey(sendNo, KeyEvent.VK_N, new AbstractAction() {

			private static final long serialVersionUID = 1L;


			@Override
			public void actionPerformed(final ActionEvent e) {
				send("no", false);
			}
		});

		settingsLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (!settingsPanel.isVisible()) {
					infoPanel.setVisible(false);
					updatePanel.setVisible(false);
				}
				settingsPanel.setVisible(!settingsPanel.isVisible());
				pack();
			}
		});

		servercheckLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				servercheckPanel.setVisible(!servercheckPanel.isVisible());
				pack();
			}
		});

		infoLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (!infoPanel.isVisible()) {
					settingsPanel.setVisible(false);
					updatePanel.setVisible(false);
				}
				infoPanel.setVisible(!infoPanel.isVisible());
				pack();
			}
		});

		updateLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (!updatePanel.isVisible()) {
					infoPanel.setVisible(false);
					settingsPanel.setVisible(false);
				}
				updatePanel.setVisible(!updatePanel.isVisible());
				pack();
			}
		});

		home.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				Helper.browse(Globals.getUriHomeLink());
			}
		});

		icons.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				Helper.browse(Globals.getUriIconsLink());
			}
		});

		downloadLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				Helper.browse(Globals.getUriHomeLink());
				viewListener.guiIsClosing(captchaDetails);
			}
		});

		final ActionListener captchaSelectListener = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				synchronized (this) {
					if (confirmCaptchas.isSelected() || clickCaptchas.isSelected()) {
						textCaptchas.setEnabled(true);
					} else {
						textCaptchas.setSelected(true);
						textCaptchas.setEnabled(false);
					}
				}
			}
		};

		textCaptchas.addActionListener(captchaSelectListener);
		clickCaptchas.addActionListener(captchaSelectListener);
		confirmCaptchas.addActionListener(captchaSelectListener);

	}


	private void addActionAndShortkey(final JButton button, final int keyEvent, final Action action) {
		addAction(button, action);
		addShortkey(button, keyEvent, action);
	}


	private void addAction(final JButton button, final Action action) {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				action.actionPerformed(e);
			}
		});
	}


	private void addShortkey(final JButton button, final int keyEvent, final Action action) {
		action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(keyEvent, InputEvent.CTRL_DOWN_MASK));
		button.getActionMap().put(keyEvent + "action", action);
		button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put((KeyStroke) action.getValue(Action.ACCELERATOR_KEY), keyEvent + "action");
	}


	private boolean isCtrlDown(final ActionEvent e) {
		return (e.getModifiers() & InputEvent.CTRL_MASK) != 0;
	}


	private void zoomIn() {
		if (!zoomIn.isEnabled()) {
			return;
		}
		zoom(0.1, 4.0);
	}


	private void zoomOut() {
		if (!zoomOut.isEnabled()) {
			return;
		}
		zoom(-0.1, 0.5);
	}


	private void zoomReset() {
		if (!zoomReset.isEnabled()) {
			return;
		}
		zoom(0.0, 1.0);
	}


	private void zoom(final double delta, final double edge) {

		if (captchaDetails == CaptchaDetails.NO_CAPTCHA) {
			return;
		}

		if (captcha == null) {
			return;
		}

		final ZoomWrapper zoom = getZoom(captcha);

		if (delta == 0) {

			if (zoom.value.getFactor() == edge) {
				return;
			}

			zoom.value = new Zoom(edge);

		} else {

			if (delta > 0 && zoom.value.getFactor() > edge || delta < 0 && zoom.value.getFactor() < edge) {
				return;
			}

			zoom.value = new Zoom(zoom.value.getFactor() + delta);
		}

		zoomLabel.setText(zoom.value.toString() + " {" + zoom.size + "}");
		setCaptcha(captcha, zoom.value.getFactor(), smooth.isSelected());
		pack();
	}


	private void setCaptcha(final Captcha captcha, final double zoom, final boolean smooth) {
		captchaLabel.setZoom(zoom);

		final Icon icon = captcha != null ? captcha.getImageIcon(zoom, smooth) : null;
		setCaptchaIcon(icon);
	}


	private void setCaptchaIcon(final Icon icon) {

		final Dimension newLabelSize;
		if (icon != null) {
			newLabelSize = new Dimension(icon.getIconWidth(), icon.getIconHeight());
		} else {
			newLabelSize = new Dimension(32, 32);
		}

		captchaLabel.setMinimumSize(newLabelSize);
		captchaLabel.setPreferredSize(newLabelSize);
		captchaLabel.setIcon(icon);

		captchaLabel.repaint();
		captchaLabel.doLayout();
		captchaLabel.revalidate();

		captchaPanel.repaint();
		captchaPanel.doLayout();
		captchaPanel.revalidate();
	}


	private synchronized void send(final String answerStr, final boolean signOut) {
		if (!send.isEnabled() && !sendYes.isEnabled() && !answerMouseClickAdapter.isEnabled()) {
			return;
		}

		if (answerStr.isEmpty()) {
			answer.setBackground(new Color(255, 202, 202));
			answer.setSelectionStart(0);
			answer.setSelectionEnd(answer.getText().length());
			return;
		}

		if (captchaDetails.getType() == CaptchaType.TEXT) {
			if (captchaDetails.getMinLength() > 0 && answerStr.length() < captchaDetails.getMinLength() ||
					captchaDetails.isNoSpace() && answerStr.contains(" ") ||
					captchaDetails.isNumeric() && !answerStr.matches("[\\d\\s]+")) {
				answer.setBackground(new Color(255, 224, 202));
				answer.requestFocusInWindow();
				return;
			}
		}

		viewListener.guiAnswer(captchaDetails, answerStr, signOut);
		answer.setBackground(new JTextField().getBackground());
		answerAftercare();
	}


	private synchronized void skip() {
		if (!skip.isEnabled()) {
			return;
		}

		viewListener.guiSkip(captchaDetails);
		answerAftercare();
	}


	private synchronized void stop() {
		if (!stop.isEnabled()) {
			return;
		}

		viewListener.guiStop(captchaDetails);
	}


	private void answerAftercare() {
		statusLabel.setText(null);
		captchaDetails = CaptchaDetails.NO_CAPTCHA;

		minimalReset();
		pack();

		if (minimize.isSelected()) {
			minimize();
		}
	}


	private void minimalReset() {
		responsePanel.setVisible(false);

		skip.setEnabled(false);
		sendYes.setEnabled(false);
		sendNo.setEnabled(false);
		send.setEnabled(false);

		zoomPanel.setVisible(false);
		zoomIn.setEnabled(false);
		zoomOut.setEnabled(false);
		zoomReset.setEnabled(false);
		zoomLabel.setText(null);

		setCaptchaIcon(Icons.COOL);

		captchaLabel.setCrossCoordinate(null);
		answer.setText(null);
		answer.setEnabled(false);
		confirmAnswerLabel.setVisible(false);
		answerMouseClickAdapter.setEnabled(false);
		captchaLabel.setCursor(Globals.DEFAULT_CURSOR);
	}


	private void pack() {
		final Dimension oldSize = frame.getSize();
		final Point location = frame.getLocation();

		frame.repaint();
		frame.doLayout();
		frame.pack();

		final int posX = positionX.getSelectedIndex();
		final int posY = positionY.getSelectedIndex();

		if (posX == 0 && posY == 0) {
			return;
		}

		final Dimension newSize = frame.getSize();
		final int tx = oldSize.width - newSize.width;
		final int ty = oldSize.height - newSize.height;

		switch (posX) {
		case 1:
			location.translate(tx / 2, 0);
			break;

		case 2:
			location.translate(tx, 0);
			break;
		}

		switch (posY) {
		case 1:
			location.translate(0, ty);
			break;
		}

		final Rectangle displayBounds = frame.getGraphicsConfiguration().getBounds();

		if (location.x < displayBounds.x) {
			location.x = displayBounds.x;
		} else if (location.x + newSize.width > displayBounds.x + displayBounds.width) {
			location.x = displayBounds.width + displayBounds.x - newSize.width;
		}

		if (location.y < displayBounds.y) {
			location.y = displayBounds.y;
		} else if (location.y + newSize.height > displayBounds.y + displayBounds.height) {
			location.y = displayBounds.height + displayBounds.y - newSize.height;
		}

		frame.setLocation(location);
	}


	private void minimize() {
		if (frame.isDisplayable()) {
			frame.toBack();
			frame.setExtendedState(frame.getExtendedState() | JFrame.ICONIFIED);
		}
	}


	private void maximize() {
		if (frame.isDisplayable()) {
			frame.setExtendedState(frame.getExtendedState() & ~JFrame.ICONIFIED);
			frame.toFront();
			frame.repaint();
			frame.requestFocus();
		}
	}


	public boolean isSpeed() {
		return speed.isEnabled() && speed.isSelected();
	}


	public boolean isSound() {
		return sound.isSelected();
	}


	public void open(final String apiKey0) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				final int x = (int) (screenSize.getWidth() - frame.getWidth()) / 2;
				final int y = (int) (screenSize.getHeight() - frame.getHeight()) / 2;

				final Point location = new Point(
						/**/Globals.SET.get("main.x", x),
						/**/Globals.SET.get("main.y", y));

				final String gd = Globals.SET.get("main.gd", null);
				if (gd != null) {

					for (final GraphicsDevice screen : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {

						if (gd.equals(screen.getIDstring())) {

							final int dx = screen.getDefaultConfiguration().getBounds().x;
							final int dy = screen.getDefaultConfiguration().getBounds().y;
							location.translate(dx, dy);

							break;
						}
					}
				}

				frame.setLocation(location);

				apiKey.setText(
						/**/apiKey0 != null && apiKey0.length() > 11 ?
				/**/apiKey0.substring(0, 4) + "..." + apiKey0.substring(apiKey0.length() - 4) :
				/**/apiKey0);
				apiKey.setToolTipText(apiKey0);
				for (int i = 0; i < zooms.length; i++) {
					zooms[i] = new ZoomWrapper(i, new Zoom(Globals.SET.get("zoom" + (i + 1), 100) / 100d));
				}
				userStatisticsPanel.setVisible(Globals.SET.getAsBoolean("showUserStatistics", true));
				smooth.setSelected(Globals.SET.getAsBoolean("smooth", true));
				positionX.setSelectedIndex(Globals.SET.getAsInt("position.x", 1, 0, positionX.getItemCount() - 1));
				positionY.setSelectedIndex(Globals.SET.getAsInt("position.y", 0, 0, positionY.getItemCount() - 1));
				final Time intervalTime = Time.valueOf(Globals.SET.get("pause", 0));
				interval.setSelectedItem(intervalTime);
				if (intervalTime.getSeconds() == 0) {
					speed.setSelected(Globals.SET.getAsBoolean("speed", false));
				} else {
					speed.setSelected(false);
					speed.setEnabled(false);
				}
				ontop.setSelected(Globals.SET.getAsBoolean("ontop", true));
				minimize.setSelected(Globals.SET.getAsBoolean("minimize", true));
				maximize.setSelected(Globals.SET.getAsBoolean("maximize", true));
				sound.setSelected(Globals.SET.getAsBoolean("sound", true));
				tabToFocus.setSelected(Globals.SET.getAsBoolean("tabtofocus", false));
				textCaptchas.setSelected(Globals.SET.getAsBoolean("textcaptchas", true));
				clickCaptchas.setSelected(Globals.SET.getAsBoolean("clickcaptchas", true));
				confirmCaptchas.setSelected(Globals.SET.getAsBoolean("confirmcaptchas", true));
				ignoreSomeErrors.setSelected(Globals.SET.getAsBoolean("ignoresomeerrors", true));

				balance.setText("?");
				solved.setText("?");
				skipped.setText("?");
				earned.setText("?");

				servercheckPanel.setVisible(Globals.SET.getAsBoolean("servercheck", false));
				settingsPanel.setVisible(Globals.SET.getAsBoolean("settings", false));
				infoPanel.setVisible(Globals.SET.getAsBoolean("info", false));

				statusLabel.setText(null);
				captchaDetails = CaptchaDetails.NO_CAPTCHA;
				captcha = null;

				minimalReset();

				stop.setEnabled(true);
				skip.setText(I18n.get("skip"));
				frame.setTitle(Globals.TITLE);

				frame.repaint();
				frame.doLayout();
				frame.pack();

				frame.setVisible(true);
				frame.setAlwaysOnTop(ontop.isSelected());

				viewListener.guiIsOpened();
			}
		});
	}


	public void close() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (!frame.isDisplayable()) {
					return;
				}

				Globals.SET.set("main.gd", frame.getGraphicsConfiguration().getDevice().getIDstring());
				Globals.SET.set("main.x", frame.getX());
				Globals.SET.set("main.y", frame.getY());
				Globals.SET.set("showUserStatistics", userStatisticsPanel.isVisible());

				frame.setVisible(false);

				for (int i = 0; i < zooms.length; i++) {
					Globals.SET.set("zoom" + (i + 1), Math.round(zooms[i].value.getFactor() * 100));
				}
				Globals.SET.set("smooth", smooth.isSelected());
				Globals.SET.set("position.x", positionX.getSelectedIndex());
				Globals.SET.set("position.y", positionY.getSelectedIndex());
				Globals.SET.set("pause", Math.round(((Time) interval.getSelectedItem()).getSeconds()));
				Globals.SET.set("speed", speed.isSelected());
				Globals.SET.set("ontop", ontop.isSelected());
				Globals.SET.set("minimize", minimize.isSelected());
				Globals.SET.set("maximize", maximize.isSelected());
				Globals.SET.set("sound", sound.isSelected());
				Globals.SET.set("tabtofocus", tabToFocus.isSelected());
				Globals.SET.set("textcaptchas", textCaptchas.isSelected());
				Globals.SET.set("clickcaptchas", clickCaptchas.isSelected());
				Globals.SET.set("confirmcaptchas", confirmCaptchas.isSelected());
				Globals.SET.set("ignoresomeerrors", ignoreSomeErrors.isSelected());

				Globals.SET.set("servercheck", servercheckPanel.isVisible());
				Globals.SET.set("settings", settingsPanel.isVisible());
				Globals.SET.set("info", infoPanel.isVisible());

				frame.dispose();
			}
		});
	}


	public void hide() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (frame.isDisplayable()) {
					frame.setVisible(false);
				}
			}
		});
	}


	public void initCaptcha(final CaptchaDetails captchaDetails) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Gui.this.captchaDetails = captchaDetails;
				requestPanel.setVisible(false);
				statusLabel.setText("captchaId: " + captchaDetails.getId());
				setCaptchaIcon(Icons.WAIT);

				stop.setEnabled(false);
			}
		});
	}


	public void showCaptcha(final Captcha captcha) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				if (captcha == null) {
					Globals.log.warning("No captcha to show!");

					setCaptchaIcon(null);
					pack();

					skip.setEnabled(true);
					skip();

					return;
				}

				Gui.this.captcha = captcha;

				final ZoomWrapper zoom = getZoom(captcha);
				setCaptcha(captcha, zoom.value.getFactor(), smooth.isSelected());

				responsePanel.setVisible(true);
				zoomPanel.setVisible(true);
				zoomIn.setEnabled(true);
				zoomOut.setEnabled(true);
				zoomReset.setEnabled(true);
				zoomLabel.setText(zoom.value.toString() + " {" + zoom.size + "}");

				if (captchaDetails.getType() == CaptchaType.CONFIRM_TEXT) {
					send.setVisible(false);

					answer.setVisible(false);

					confirmAnswerLabel.setVisible(true);
					confirmAnswerLabel.setText(captchaDetails.getTextAnswer());

					sendYes.setVisible(true);
					sendYes.setEnabled(true);
					sendNo.setVisible(true);
					sendNo.setEnabled(true);

				} else if (captchaDetails.getType() == CaptchaType.CONFIRM_MOUSE) {

					captchaLabel.setCrossCoordinate(captchaDetails.getMouseAnswer());

					send.setVisible(false);

					answer.setVisible(false);

					sendYes.setVisible(true);
					sendYes.setEnabled(true);
					sendNo.setVisible(true);
					sendNo.setEnabled(true);

				} else if (captchaDetails.getType() == CaptchaType.MOUSE) {
					answer.setVisible(false);
					sendYes.setVisible(false);
					sendNo.setVisible(false);
					send.setVisible(false);

					answerMouseClickAdapter.setEnabled(true);
					captchaLabel.setCursor(Globals.TARGET_CURSOR);

				} else {
					sendYes.setVisible(false);
					sendNo.setVisible(false);

					answer.setVisible(true);
					answer.setEnabled(true);
					send.setVisible(true);
					send.setEnabled(true);
				}

				skip.setEnabled(true);

				final String text = statusLabel.getText();
				requestPanel.setVisible(false);
				statusLabel.setText((text != null ? text + " " : "") + captcha);

				pack();
				frame.setAlwaysOnTop(ontop.isSelected());

				if (maximize.isSelected()) {
					maximize();
				}

				if (tabToFocus.isSelected()) {
					tabToFocusLabel.requestFocusInWindow();
				} else {
					answer.requestFocusInWindow();
				}

				stop.setEnabled(true);
			}
		});
	}


	public void setSolved(final int value) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				solved.setText(String.valueOf(value));
			}
		});
	}


	public void setSkipped(final int value) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				skipped.setText(String.valueOf(value));
			}
		});
	}


	public void setEarned(final int earned0, final boolean exactValue) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final String oldEarned = earned.getText();
				final String newEarned = (!exactValue ? "~" : "") + String.valueOf(earned0);

				earned.setText(newEarned);

				if (oldEarned == null || oldEarned.length() < newEarned.length()) {
					pack();
				}
			}
		});
	}


	public void setBalance(final LocalDateTime time, final int balance0) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final String balanceInfo = String.format(
						/**/I18n.get("balance.received"),
						/**/time.format(Globals.DATE_FORMATTER),
						/**/time.format(Globals.TIME_FORMATTER));

				final String oldBalance = balance.getText();
				final String newBalance = String.valueOf(balance0);

				balance.setToolTipText(balanceInfo);
				balance.setText(newBalance);
				earned.setToolTipText(balanceInfo);

				if (oldBalance == null || oldBalance.length() < newBalance.length()) {
					pack();
				}
			}
		});
	}


	public void setServercheck(final int worker, final int inwork, final int queue) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				servercheck.setText("worker:" + worker + "  inwork:" + inwork + "  queue:" + queue);
			}
		});
	}


	public void setUnrated(final int count) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				showUnrated(count);
			}
		});
	}


	private void showUnrated(final int count) {
		if (count > 0) {
			if (!unratedCaptchasAdapter.isEnabled()) {
				unratedLabel.setText("<html><body>(<a href=\"#\">" + count + "</a>)</body></html>");
				unratedLabel.setToolTipText(count + " unrated captchas");
				unratedCaptchasAdapter.setEnabled(true);
				unratedLabel.setCursor(Globals.CLICK_CURSOR);
			}
		} else {
			if (unratedCaptchasAdapter.isEnabled()) {
				unratedLabel.setText("   ");
				unratedLabel.setToolTipText(null);
				unratedCaptchasAdapter.setEnabled(false);
				unratedLabel.setCursor(Globals.DEFAULT_CURSOR);
			}
		}
	}


	public void reset() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (frame.isDisplayable()) {

					statusLabel.setText(null);
					captchaDetails = CaptchaDetails.NO_CAPTCHA;
					captcha = null;

					minimalReset();

					skip.setText(I18n.get("skip"));
					frame.setTitle(Globals.TITLE);

					pack();
				}
			}
		});
	}


	public void setSleepTime(final Time sleepTime0) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (sleepTime0.getSeconds() > 0) {
					frame.setTitle(String.format(I18n.get("sleeptimed"), sleepTime0) + " - " + Globals.TITLE);
				} else {
					frame.setTitle(Globals.TITLE);
				}

				final ImageIcon icon;
				if (sleepTime0.getSeconds() <= 0) {
					icon = Icons.COOL;
				} else {
					icon = Icons.SLEEP;
				}
				if (icon != captchaLabel.getIcon()) {
					setCaptchaIcon(icon);
				}
			}
		});
	}


	public void setAnswerTimeout(final Time timeout) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final String oldText = skip.getText();

				if (timeout.getSeconds() > 0) {
					skip.setText(String.format(I18n.get("skiptimed"), timeout));
					frame.setTitle(String.format(I18n.get("autoskip"), timeout) + " - " + Globals.TITLE);
				} else {
					skip.setText(I18n.get("skip"));
					frame.setTitle(Globals.TITLE);
				}

				if (oldText == null || oldText.length() < skip.getText().length()) {
					pack();
				}
			}
		});
	}


	public void setSendingAnswer() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setTitle(I18n.get("sending") + " - " + Globals.TITLE);
			}
		});
	}


	public void updateAvailable(final Version version) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (frame.isDisplayable()) {
					updateLabel.setVisible(true);
					updateInfoLabel.setText(String.format(I18n.get("updateavailable"), version.getShortValue()));
					updatePanel.setVisible(true);
					pack();
				}
			}
		});
	}


	public int getIntervalSeconds() {
		return ((Time) interval.getSelectedItem()).getSeconds();
	}


	public void setRequestingProgress(final long requestCount, final boolean text, final boolean mouse, final boolean confirm) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final int id = (int) ((requestCount - 1) % Icons.PROGRESS.length);
				requestTextLabel.setVisible(text);
				requestMouseLabel.setVisible(mouse);
				requestConfirmLabel.setVisible(confirm);
				requestProgressLabel.setIcon(Icons.PROGRESS[id]);
				requestPanel.setVisible(true);
			}
		});
	}

	private class CaptchaLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		private Point crossCoordinate = null;
		private double zoom;


		public synchronized void setCrossCoordinate(final Point crossCoordinate) {
			this.crossCoordinate = crossCoordinate;
		}


		public synchronized void setZoom(final double zoom) {
			this.zoom = zoom;
		}


		@Override
		public void paintComponent(final Graphics g) {
			super.paintComponent(g);

			final int x;
			final int y;

			synchronized (this) {
				if (crossCoordinate == null) {
					return;
				}
				x = (int) Math.round(crossCoordinate.x * zoom);
				y = (int) Math.round(crossCoordinate.y * zoom);
			}

			final int r = (int) Math.round(24 * zoom);
			final int l = (int) Math.round(2 * zoom);
			final int w = getWidth();
			final int h = getHeight();

			final Graphics2D g2 = (Graphics2D) g;

			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			g2.setColor(Color.BLUE);
			g2.setStroke(new BasicStroke(l));

			g2.drawLine(x, 0, x, y - r);
			g2.drawLine(x, y + r, x, h);
			g2.drawLine(0, y, x - r, y);
			g2.drawLine(x + r, y, w, y);

			g2.fillOval(x - r, y - r, r * 2, r * 2);
		}
	}

	private class MouseCaptchaAdapter extends MouseAdapter {

		private boolean enabled;


		public MouseCaptchaAdapter() {
			setEnabled(false);
		}


		public void setEnabled(final boolean enabled) {
			this.enabled = enabled;
		}


		public boolean isEnabled() {
			return enabled;
		}


		@Override
		public void mouseClicked(final MouseEvent e) {
			if (enabled) {
				final Point p = e.getPoint();
				final double factor = getZoom(captcha).value.getFactor();

				final int x = (int) Math.round(p.x / factor);
				final int y = (int) Math.round(p.y / factor);
				send(x + "x" + y, e.isControlDown());
			}
		}
	}


	private ZoomWrapper getZoom(final Captcha captcha) {
		final int width = captcha.getWidth();
		final int height = captcha.getHeight();

		for (int i = 0; i < Globals.CAPTCHA_DIMENSIONS.length; i++) {

			if (width <= Globals.CAPTCHA_DIMENSIONS[i].width &&
					height <= Globals.CAPTCHA_DIMENSIONS[i].height) {
				return zooms[i];
			}
		}

		return zooms[zooms.length - 1];
	}

	private static class ZoomWrapper {

		private static final String[] SIZES = { "S", "M", "L", "XL" };

		private final String size;
		Zoom value;


		public ZoomWrapper(final int size, final Zoom value) {
			this.size = getSize(size);
			this.value = value;
		}


		private String getSize(final int size) {
			if (size >= 0 && size < SIZES.length) {
				return SIZES[size];
			}

			if (size >= SIZES.length) {
				return ">" + SIZES[SIZES.length - 1];
			}

			return "-";
		}
	}

	private static class UnratedCaptchasAdapter extends MouseAdapter {

		private boolean enabled;
		private final Gui gui;


		public UnratedCaptchasAdapter(final Gui gui) {
			this.gui = gui;
			enabled = false;
		}


		public void setEnabled(final boolean enabled) {
			this.enabled = enabled;
		}


		public boolean isEnabled() {
			return enabled;
		}


		@Override
		public void mouseClicked(final MouseEvent e) {
			if (enabled) {
				Helper.browse(Globals.getUriCaptchasHistoryPage());
				gui.showUnrated(0);
			}
		}
	}


	public boolean isIgnoreSomeErrors() {
		return ignoreSomeErrors.isSelected();
	}


	public boolean isText() {
		return textCaptchas.isSelected();
	}


	public boolean isMouse() {
		return clickCaptchas.isSelected();
	}


	public boolean isConfirm() {
		return confirmCaptchas.isSelected();
	}
}
