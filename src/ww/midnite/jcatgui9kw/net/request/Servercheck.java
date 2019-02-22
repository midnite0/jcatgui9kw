package ww.midnite.jcatgui9kw.net.request;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.ServercheckResponse;
import ww.midnite.util.http.HttpGetResponse;


public class Servercheck extends Request {

	protected Servercheck(final RequestFactory factory) {
		super(factory);
	}


	public void request() {
		getFactory().getThreadPool().execute(new Runnable() {

			public void run() {
				final HttpGetResponse response = getFactory().getHttpGet(Globals.getUrlServercheck()).get(Globals.HTTP_TIMEOUT, Globals.HTTP_RETRIES);
				getFactory().getListener().servercheckResponse(new ServercheckResponse(response));
			}

		});
	}

}
