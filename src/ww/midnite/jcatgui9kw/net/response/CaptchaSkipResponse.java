package ww.midnite.jcatgui9kw.net.response;

import ww.midnite.jcatgui9kw.engine.Controller.AfterCommand;
import ww.midnite.util.http.HttpGetResponse;


public class CaptchaSkipResponse extends Response {

	private final CaptchaDetails captchaDetails;
	private final boolean autoskip;
	private final AfterCommand afterCommand;


	public CaptchaSkipResponse(final HttpGetResponse response, final CaptchaDetails captchaDetails, final boolean autoskip, final AfterCommand afterCommand) {
		super(response);

		this.captchaDetails = captchaDetails;
		this.autoskip = autoskip;
		this.afterCommand = afterCommand;
	}


	public String getAnswer() {
		return getString();
	}


	public boolean isAnswered() {
		return getAnswer() != null && getAnswer().equalsIgnoreCase("ok");
	}


	public CaptchaDetails getCaptchaDetails() {
		return captchaDetails;
	}


	public boolean isAutoskip() {
		return autoskip;
	}


	public AfterCommand getAfterCommand() {
		return afterCommand;
	}

}
