package ww.midnite.jcatgui9kw.net.response;

import java.util.regex.Pattern;

import ww.midnite.util.http.HttpGetResponse;


public class ServercheckResponse extends Response {

	private static final Pattern SPLIT = Pattern.compile("|", Pattern.LITERAL);

	private final int worker;
	private final int inwork;
	private final int queue;


	public ServercheckResponse(final HttpGetResponse response) {
		super(response);

		int worker = -1;
		int inwork = -1;
		int queue = -1;

		if (!isError()) {
			for (final String part : SPLIT.split(getString())) {

				final int eqIndex = part.indexOf('=');
				if (eqIndex == -1 || eqIndex == 0 || eqIndex == part.length() - 1) {
					continue;
				}

				final String key = part.substring(0, eqIndex);
				if (key.equals("worker")) {
					worker = getInt(part, eqIndex);

				} else if (key.equals("inwork")) {
					inwork = getInt(part, eqIndex);

				} else if (key.equals("queue")) {
					queue = getInt(part, eqIndex);
				}
			}
		}

		this.worker = worker;
		this.inwork = inwork;
		this.queue = queue;
	}


	private int getInt(final String part, final int eqIndex) {
		return Double.valueOf(part.substring(eqIndex + 1)).intValue();
	}


	public int getWorker() {
		return worker;
	}


	public int getInwork() {
		return inwork;
	}


	public int getQueue() {
		return queue;
	}
}
