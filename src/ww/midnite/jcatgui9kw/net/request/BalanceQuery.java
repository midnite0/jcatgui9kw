package ww.midnite.jcatgui9kw.net.request;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.BalanceQueryResponse;
import ww.midnite.util.http.HttpGetResponse;


public class BalanceQuery extends Request {

	protected BalanceQuery(final RequestFactory factory) {
		super(factory);
	}


	public void request(final String apiKey) {
		getFactory().getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				final HttpGetResponse response = getFactory().getHttpGet(Globals.getUrlBalanceQuery(apiKey)).get(Globals.HTTP_TIMEOUT, Globals.HTTP_RETRIES);
				sendResponse(response);
			}
		});
	}


	protected void sendResponse(final HttpGetResponse response) {
		getFactory().getListener().balanceQueryResponse(new BalanceQueryResponse(response));
	}

}
