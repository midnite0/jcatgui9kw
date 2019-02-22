package ww.midnite.jcatgui9kw.net.request;

import java.net.URL;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.CaptchaDetails;
import ww.midnite.jcatgui9kw.net.response.ShowCaptchaResponse;
import ww.midnite.util.http.HttpGet;
import ww.midnite.util.http.HttpGetResponse;


public class ShowCaptcha extends Request {

	private HttpGet httpGet = null;


	protected ShowCaptcha(final RequestFactory factory) {
		super(factory);
	}


	protected URL getUrl(final CaptchaDetails captchaDetails, final boolean speed, final String apiKey, final boolean fastMethod) {
		return Globals.getUrlShowCaptcha(captchaDetails.getId(), speed, apiKey, fastMethod);
	}


	public void request(final String apiKey, final boolean speed, final CaptchaDetails captchaDetails, final boolean fastMethod) {
		httpGet = getFactory().getHttpGet(getUrl(captchaDetails, speed, apiKey, fastMethod));
		getFactory().getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				final HttpGetResponse response = httpGet.get(Globals.HTTP_TIMEOUT, Globals.HTTP_RETRIES);
				getFactory().getListener().showCaptchaResponse(new ShowCaptchaResponse(response, captchaDetails));
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
