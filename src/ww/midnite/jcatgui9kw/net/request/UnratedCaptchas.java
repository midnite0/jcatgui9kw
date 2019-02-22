package ww.midnite.jcatgui9kw.net.request;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.UnratedCaptchasResponse;
import ww.midnite.util.http.HttpGetResponse;


public class UnratedCaptchas extends Request {

	protected UnratedCaptchas(final RequestFactory factory) {
		super(factory);
	}


	public void request(final String apiKey) {
		getFactory().getThreadPool().execute(new Runnable() {

			public void run() {
				final HttpGetResponse response = getFactory().getHttpGet(Globals.getUrlUnratedCaptchas(apiKey)).get(Globals.HTTP_TIMEOUT, Globals.HTTP_RETRIES);
				getFactory().getListener().unratedCaptchasResponse(new UnratedCaptchasResponse(response));
			}

		});
	}

}
