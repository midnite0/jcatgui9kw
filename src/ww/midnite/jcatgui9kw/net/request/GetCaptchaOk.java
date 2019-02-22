package ww.midnite.jcatgui9kw.net.request;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.GetCaptchaOkResponse;
import ww.midnite.jcatgui9kw.net.response.CaptchaDetails;
import ww.midnite.util.http.HttpGetResponse;


public class GetCaptchaOk extends Request {

	protected GetCaptchaOk(final RequestFactory factory) {
		super(factory);
	}


	public void request(final String apiKey, final CaptchaDetails newCaptcha) {
		getFactory().getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				final HttpGetResponse response = getFactory().getHttpGet(Globals.getUrlGetCaptchaOk(apiKey)).get(Globals.HTTP_TIMEOUT, Globals.HTTP_RETRIES);
				getFactory().getListener().getCaptchaOkResponse(new GetCaptchaOkResponse(response, newCaptcha));
			}
		});
	}

}
