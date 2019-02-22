package ww.midnite.jcatgui9kw.net.response;

import ww.midnite.util.http.HttpGetResponse;
import ww.midnite.util.image.Captcha;


public class ShowCaptchaResponse extends Response {

	private final CaptchaDetails captchaDetails;

	private final Captcha captcha;


	public ShowCaptchaResponse(final HttpGetResponse response, final CaptchaDetails captchaDetails) {
		super(response);
		this.captchaDetails = captchaDetails;

		Captcha captcha = null;
		if (!isError()) {
			captcha = new Captcha(getBinary(), getType());
		}

		this.captcha = captcha;
	}


	public CaptchaDetails getCaptchaDetails() {
		return captchaDetails;
	}


	public Captcha getCaptcha() {
		return captcha;
	}

}
