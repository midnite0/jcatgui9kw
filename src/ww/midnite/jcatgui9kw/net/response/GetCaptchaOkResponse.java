package ww.midnite.jcatgui9kw.net.response;

import ww.midnite.util.http.HttpGetResponse;


public class GetCaptchaOkResponse extends Response {

	private final CaptchaDetails captchaDetails;


	public GetCaptchaOkResponse(final HttpGetResponse response, final CaptchaDetails captchaDetails) {
		super(response);
		this.captchaDetails = captchaDetails;
	}


	public CaptchaDetails getCaptchaDetails() {
		return captchaDetails;
	}

}
