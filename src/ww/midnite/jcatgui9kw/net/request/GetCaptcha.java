package ww.midnite.jcatgui9kw.net.request;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.GetCaptchaResponse;
import ww.midnite.util.http.HttpGet;
import ww.midnite.util.http.HttpGetResponse;


public class GetCaptcha extends Request {

	private HttpGet httpGet = null;


	protected GetCaptcha(final RequestFactory factory) {
		super(factory);
	}


	public void request(final String apiKey, final boolean text, final boolean mouse, final boolean confirm) {
		httpGet = getFactory().getHttpGet(Globals.getUrlGetCaptcha(apiKey, text, mouse, confirm));
		getFactory().getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				final HttpGetResponse response = httpGet.get(Globals.HTTP_TIMEOUT, Globals.HTTP_RETRIES);
				getFactory().getListener().getCaptchaResponse(new GetCaptchaResponse(response));
			}
		});
	}


	public void interrupt() {
		if (httpGet != null) {
			getFactory().getThreadPool().execute(new Runnable() {

				@Override
				public void run() {
					httpGet.cancel();
					httpGet = null;
				}
			});
		}
	}

}
