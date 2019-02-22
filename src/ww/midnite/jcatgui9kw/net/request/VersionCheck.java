package ww.midnite.jcatgui9kw.net.request;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.VersionCheckResponse;
import ww.midnite.util.http.HttpGet;
import ww.midnite.util.http.HttpGetResponse;


public class VersionCheck {

	private final RequestFactory factory;

	private HttpGet httpGet = null;


	protected VersionCheck(final RequestFactory factory) {
		this.factory = factory;
	}


	public void request() {
		httpGet = factory.getHttpGet(Globals.getUrlCheckForUpdate());
		factory.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				final HttpGetResponse response = httpGet.get(Globals.HTTP_TIMEOUT, Globals.HTTP_RETRIES);
				factory.getListener().versionCheckResponse(new VersionCheckResponse(response));
			}
		});
	}


	public void interrupt() {
		if (httpGet != null) {
			factory.getThreadPool().execute(new Runnable() {

				@Override
				public void run() {
					httpGet.cancel();
					httpGet = null;
				}
			});
		}
	}

}
