package ww.midnite.jcatgui9kw.net.response;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ww.midnite.util.http.HttpGetException.Type;
import ww.midnite.util.http.HttpGetResponse;
import ww.midnite.util.model.Failure;


public abstract class Response {

	private final LocalDateTime createTime;
	private final HttpGetResponse response;
	private final Failure error;


	public Response(final HttpGetResponse response0) {
		createTime = LocalDateTime.now();
		response = response0;

		error = parseError(response);
	}

	private static final Pattern KNOWN_ERROR_PATTERN = Pattern.compile("(\\d{4})\\s(.{1,64})");


	private static Failure parseError(final HttpGetResponse response) {
		if (response == null) {
			return new Failure("NORESPONSE");
		}

		if (response.isException()) {
			return new Failure(response.getException().toString());
		}

		final Matcher errorMatcher = KNOWN_ERROR_PATTERN.matcher(response.getString());
		if (errorMatcher.matches()) {
			return new Failure(errorMatcher.group(1), errorMatcher.group(2));
		}

		return null;
	}


	public LocalDateTime getCreateTime() {
		return createTime;
	}


	public byte[] getBinary() {
		return response.getBinary();
	}


	public String getString() {
		return response.getString();
	}


	public String getType() {
		return response.getType();
	}


	public long getResponseTime() {
		return response.getResponseTime();
	}


	public Failure getError() {
		return error;
	}


	public boolean isError() {
		return getError() != null;
	}


	public boolean isConnectionException() {
		return isTimeoutException() || isInterruptedException();
	}


	public boolean isTimeoutException() {
		return response.isException() && response.getException().getType() == Type.TIMEOUT;
	}


	public boolean isInterruptedException() {
		return response.isException() && response.getException().getType() == Type.INTERRUPTED;
	}

}
