package ww.midnite.jcatgui9kw.net.request;

import java.net.URL;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.CaptchaPauseResponse;
import ww.midnite.util.http.HttpGetResponse;


public class CaptchaPause extends Request {

	protected CaptchaPause(final RequestFactory factory) {
		super(factory);
	}


	public void request(final String apiKey) {
		getFactory().getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				final URL url = Globals.getUrlCaptchaPause(apiKey);

				final HttpGetResponse response = getFactory().getHttpGet(url).get(Globals.HTTP_TIMEOUT, Globals.HTTP_RETRIES);
				getFactory().getListener().captchaPauseResponse(new CaptchaPauseResponse(response));
			}
		});
	}

}
