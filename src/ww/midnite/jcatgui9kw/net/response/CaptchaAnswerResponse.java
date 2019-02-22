package ww.midnite.jcatgui9kw.net.response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ww.midnite.util.http.HttpGetResponse;


public class CaptchaAnswerResponse extends Response {

	public static final Pattern RESPONSE = Pattern.compile("OK\\|(\\d+)", Pattern.CASE_INSENSITIVE);

	private final CaptchaDetails captchaDetails;
	private final boolean signOut;
	private final String captchaAnswer;
	private final int answerPoints;


	public CaptchaAnswerResponse(final HttpGetResponse response, final CaptchaDetails captchaDetails, final String captchaAnswer, final boolean signOut) {
		super(response);

		this.captchaDetails = captchaDetails;
		this.captchaAnswer = captchaAnswer;
		this.signOut = signOut;

		final Matcher m = RESPONSE.matcher(getString());
		if (m.matches()) {
			answerPoints = Integer.parseInt(m.group(1));
		} else {
			answerPoints = 0;
		}
	}


	public String getAnswer() {
		return getString();
	}


	public boolean isAnswered() {
		return getAnswer() != null && getAnswerPoints() > 0;
	}


	public CaptchaDetails getCaptchaDetails() {
		return captchaDetails;
	}


	public String getCaptchaAnswer() {
		return captchaAnswer;
	}


	public boolean isSignOut() {
		return signOut;
	}


	public int getAnswerPoints() {
		return answerPoints;
	}

}
