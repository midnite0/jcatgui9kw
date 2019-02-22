package ww.midnite.jcatgui9kw.net.response;

import ww.midnite.util.http.HttpGetResponse;


public class BalanceQueryResponse extends Response {

	private final int balance;


	public BalanceQueryResponse(final HttpGetResponse response) {
		super(response);

		if (isError() || !getString().matches("\\d+")) {
			balance = -1;
		} else {
			balance = Double.valueOf(getString()).intValue();
		}
	}


	public int getBalance() {
		return balance;
	}
}
