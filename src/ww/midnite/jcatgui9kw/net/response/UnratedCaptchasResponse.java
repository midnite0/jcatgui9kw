package ww.midnite.jcatgui9kw.net.response;

import ww.midnite.util.http.HttpGetResponse;


public class UnratedCaptchasResponse extends Response {

	private final int count;


	public UnratedCaptchasResponse(final HttpGetResponse response) {
		super(response);

		if (isError()) {
			count = -1;
		} else {
			final String[] lines = getString().split("[\r\n]+");
			if (!lines[0].matches("\\d+")) {
				count = -1;
			} else {
				count = Double.valueOf(lines[0]).intValue();
			}
		}
	}


	public int getCount() {
		return count;
	}
}
