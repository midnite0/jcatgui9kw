package ww.midnite.jcatgui9kw.gui;

import ww.midnite.jcatgui9kw.net.response.CaptchaDetails;


public interface GuiListener {

	void guiIsOpened();


	void guiIsClosing(CaptchaDetails captchaDetails);


	void guiAnswer(CaptchaDetails captchaDetails, String answer, boolean signOut);


	void guiSleepChanged();


	void guiSkip(CaptchaDetails captchaDetails);


	void guiStop(CaptchaDetails captchaDetails);

}
