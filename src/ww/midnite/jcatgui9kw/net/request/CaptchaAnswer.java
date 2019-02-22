package ww.midnite.jcatgui9kw.net.request;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.CaptchaAnswerResponse;
import ww.midnite.jcatgui9kw.net.response.CaptchaDetails;
import ww.midnite.util.http.HttpGet;
import ww.midnite.util.http.HttpGetResponse;


public class CaptchaAnswer extends Request {

	protected CaptchaAnswer(final RequestFactory factory) {
		super(factory);
	}


	public void request(final CaptchaDetails captchaDetails, final String answer, final String apiKey, final boolean signOut) {
		getFactory().getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				final String encodedAnswer = HttpGet.urlencode(answer);
				final HttpGetResponse response = getFactory().getHttpGet(Globals.getUrlCaptchaAnswer(captchaDetails.getId(), encodedAnswer, captchaDetails.isConfirm(), apiKey)).get(Globals.HTTP_TIMEOUT, Globals.HTTP_RETRIES);
				getFactory().getListener().captchaAnswerResponse(new CaptchaAnswerResponse(response, captchaDetails, answer, signOut));
			}
		});
	}

}
