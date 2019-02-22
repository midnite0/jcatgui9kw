package ww.midnite.jcatgui9kw.net.request;

import ww.midnite.jcatgui9kw.net.response.CheckApiKeyResponse;
import ww.midnite.util.http.HttpGetResponse;


public class CheckApiKey extends BalanceQuery {

	protected CheckApiKey(final RequestFactory factory) {
		super(factory);
	}


	@Override
	protected void sendResponse(final HttpGetResponse response) {
		getFactory().getListener().checkApiKeyResponse(new CheckApiKeyResponse(response));
	}

}
