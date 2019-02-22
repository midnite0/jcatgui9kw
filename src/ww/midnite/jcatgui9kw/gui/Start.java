package ww.midnite.jcatgui9kw.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.util.Helper;
import ww.midnite.util.i18n.I18n;


public class Start {

	private final StartListener startListener;
	private JFrame start;
	private JPanel mainPanel;

	private JTextField apiKey;
	private JCheckBox remember;
	private JCheckBox autostart;
	private JCheckBox updatecheck;
	private JCheckBox useHttps;


	public Start(final StartListener startListener) {
		this.startListener = startListener;
		build();
	}


	private void build() {
		/* init */
		start = new JFrame(Globals.TITLE);
		start.setIconImages(Icons.LOGOS);

		mainPanel = new JPanel();
		final BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(layout);

		final JPanel apiKeyPanel = new JPanel(new BorderLayout(2, 2));
		apiKey = new JTextField(24);

		final JPanel rememberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		remember = new JCheckBox(I18n.get("remember"));

		final JPanel autostartPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		autostart = new JCheckBox(I18n.get("autostart"));

		final JPanel updatecheckPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		updatecheck = new JCheckBox(I18n.get("updatecheck"));

		final JPanel useHttpsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		useHttps = new JCheckBox(I18n.get("https"));

		final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		final JButton ok = new JButton(I18n.get("start"), Icons.START);
		ok.setFont(ok.getFont().deriveFont(Font.PLAIN, 16));

		final JPanel getAccountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		final JLabel getAccountLabel = new JLabel("<html><a href='#'>" + I18n.get("need.account") + "</a></html>");

		final JPanel forgotPasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		final JLabel forgotPasswordLabel = new JLabel("<html><a href='#'>" + I18n.get("forgot.apiKey") + "</a></html>");

		final JPanel versionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		final JLabel versionLabel = new JLabel("v" + Globals.VERSION.getValue(), JLabel.RIGHT);
		versionLabel.setForeground(versionLabel.getForeground().brighter());
		versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getSize() - 1f));

		/* style */
		mainPanel.setBorder(new TitledBorder(""));
		apiKeyPanel.setBorder(new TitledBorder("API Key:"));

		final Font df = new JTextField().getFont();
		apiKey.setFont(df.deriveFont(Font.BOLD, df.getSize() + 2));

		/* layout */
		apiKeyPanel.add(new JLabel(Icons.START_KEY), BorderLayout.WEST);
		apiKeyPanel.add(apiKey);
		rememberPanel.add(remember);
		autostartPanel.add(autostart);
		updatecheckPanel.add(updatecheck);
		useHttpsPanel.add(useHttps);

		buttonPanel.add(ok);

		getAccountPanel.add(getAccountLabel);
		getAccountLabel.setCursor(Globals.CLICK_CURSOR);
		forgotPasswordPanel.add(forgotPasswordLabel);
		forgotPasswordPanel.setCursor(Globals.CLICK_CURSOR);

		versionPanel.add(versionLabel);

		mainPanel.add(apiKeyPanel);
		mainPanel.add(rememberPanel);
		mainPanel.add(autostartPanel);
		mainPanel.add(updatecheckPanel);
		mainPanel.add(useHttpsPanel);
		mainPanel.add(buttonPanel);
		mainPanel.add(getAccountPanel);
		mainPanel.add(forgotPasswordPanel);
		mainPanel.add(versionPanel);

		start.add(mainPanel);

		/* actions */
		final KeyListener enterKey = new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					start();
				}
			}
		};

		apiKey.addKeyListener(enterKey);

		start.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent e) {
				startListener.startIsClosing();
			}
		});
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				start();
			}
		});

		remember.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {
				autostart.setEnabled(remember.isSelected());
			}
		});

		getAccountLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				Helper.browse(Globals.getUriRegister());
			}
		});

		forgotPasswordLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				Helper.browse(Globals.getUriForgotApiKey());
			}
		});

		/* set */
		loadSettings();

		/* finish */
		start.pack();
		start.setResizable(false);
	}


	private void start() {
		if (apiKey.getText().trim().isEmpty()) {
			return;
		}

		Globals.SET.set("apiKey", apiKey.getText());
		Globals.SET.set("remember", remember.isSelected());
		Globals.SET.set("autostart", autostart.isSelected());
		Globals.SET.set("updatecheck", updatecheck.isSelected());
		Globals.SET.set("useHttps", useHttps.isSelected());

		startListener.startConfirm();
	}


	private void loadSettings() {
		autostart.setEnabled(false);

		apiKey.setText(Globals.SET.get("apiKey", ""));
		autostart.setSelected(Globals.SET.getAsBoolean("autostart", false));
		remember.setSelected(Globals.SET.getAsBoolean("remember", false));
		updatecheck.setSelected(Globals.SET.getAsBoolean("updatecheck", true));
		useHttps.setSelected(Globals.SET.getAsBoolean("useHttps", true));
	}


	public void open() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				start.setLocation(
						/**/Globals.SET.getAsInt("start.x", (int) (screenSize.getWidth() - start.getWidth()) / 2),
						/**/Globals.SET.getAsInt("start.y", (int) (screenSize.getHeight() - start.getHeight()) / 2));

				start.setVisible(true);

				startListener.startIsOpened();
			}
		});
	}


	public void close() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				start.setVisible(false);

				Globals.SET.set("start.x", start.getX());
				Globals.SET.set("start.y", start.getY());

				start.dispose();
			}
		});
	}


	public void hide() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				start.setVisible(false);
			}
		});
	}


	public void show() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				start.setVisible(true);
			}
		});
	}


	public void reloadSettings() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				loadSettings();
			}
		});
	}

}
