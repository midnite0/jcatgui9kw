package ww.midnite.jcatgui9kw.net.request;

import java.net.URL;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.engine.Controller.AfterCommand;
import ww.midnite.jcatgui9kw.net.response.CaptchaDetails;
import ww.midnite.jcatgui9kw.net.response.CaptchaSkipResponse;
import ww.midnite.util.http.HttpGetResponse;


public class CaptchaSkip extends Request {

	protected CaptchaSkip(final RequestFactory factory) {
		super(factory);
	}


	public void request(final CaptchaDetails captchaDetails, final String apiKey, final boolean autoskip, final AfterCommand afterCommand) {
		getFactory().getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				final URL url = autoskip ?
				/**/ Globals.getUrlCaptchaAutoskip(captchaDetails.getId(), apiKey) :
				/**/ Globals.getUrlCaptchaSkip(captchaDetails.getId(), apiKey);

				final HttpGetResponse response = getFactory().getHttpGet(url).get(Globals.HTTP_TIMEOUT, Globals.HTTP_RETRIES);
				getFactory().getListener().captchaSkipResponse(new CaptchaSkipResponse(response, captchaDetails, autoskip, afterCommand));
			}
		});
	}

}
