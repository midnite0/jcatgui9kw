package ww.midnite.util.http;

public class HttpGetResponse {

	private final long responseTime;
	private final byte[] binary;
	private final String type;
	private final int code;
	private final HttpGetException e;

	private boolean parsed = false;
	private String parsedString;


	protected HttpGetResponse(final HttpGetException e) {
		responseTime = -1;
		binary = null;
		type = null;
		code = -1;

		this.e = e;
	}


	protected HttpGetResponse(final long responseTime, final byte[] binary, final String type, final int code) {
		this.responseTime = responseTime;
		this.binary = binary;
		this.type = type;
		this.code = code;

		e = null;
	}


	public long getResponseTime() {
		return responseTime;
	}


	public byte[] getBinary() {
		return binary;
	}


	public String getType() {
		return type;
	}


	public int getCode() {
		return code;
	}


	public HttpGetException getException() {
		return e;
	}


	public boolean isException() {
		return getException() != null;
	}


	public String getString() {
		if (isException()) {
			return e.toString();
		}

		if (!parsed) {
			parsed = true;
			parsedString = new String(binary);
		}

		return parsedString;
	}
}
