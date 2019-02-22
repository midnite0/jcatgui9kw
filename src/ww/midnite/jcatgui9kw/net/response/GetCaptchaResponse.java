package ww.midnite.jcatgui9kw.net.response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ww.midnite.util.http.HttpGetResponse;


public class GetCaptchaResponse extends Response {

	public static final Pattern SPLIT = Pattern.compile("|", Pattern.LITERAL);
	public static final Pattern MIN_LEN = Pattern.compile("min_len=(\\d+)");
	public static final Pattern COORDINATE = Pattern.compile("(\\d+)x(\\d+)");

	public final CaptchaDetails captchaDetails;


	public GetCaptchaResponse(final HttpGetResponse response) {

		super(response);

		if (isError()) {
			captchaDetails = CaptchaDetails.DUMMY;

		} else {

			final String content = getString();
			if (content.equals("NO CAPTCHA") || content.trim().isEmpty()) {
				captchaDetails = CaptchaDetails.NO_CAPTCHA;

			} else {

				final String[] responseParts = SPLIT.split(content);
				final String captchaIdTxt = responseParts[0].trim();

				if (captchaIdTxt.matches("\\d+")) {

					final int captchaId = Double.valueOf(captchaIdTxt).intValue();

					final String contentLow = content.toLowerCase();
					final boolean mouse = contentLow.contains("|mouse=1");
					final boolean confirm = contentLow.contains("|confirm=1");

					if (mouse) {
						if (confirm) {
							captchaDetails = new CaptchaDetails(captchaId, CaptchaType.CONFIRM_MOUSE);
							if (responseParts.length > 3) {
								final Matcher m = COORDINATE.matcher(responseParts[3]);
								if (m.matches()) {
									captchaDetails.setMouseAnswer(Double.valueOf(m.group(1)).intValue(), Double.valueOf(m.group(2)).intValue());
								}
							}
						} else {
							captchaDetails = new CaptchaDetails(captchaId, CaptchaType.MOUSE);
						}
					} else {
						if (confirm) {
							captchaDetails = new CaptchaDetails(captchaId, CaptchaType.CONFIRM_TEXT);
							captchaDetails.setTextAnswer(responseParts[3]);
						} else {
							captchaDetails = new CaptchaDetails(captchaId, CaptchaType.TEXT);
							final Matcher m = MIN_LEN.matcher(contentLow);
							if (m.find()) {
								captchaDetails.setMinLength(Double.valueOf(m.group(1)).intValue());
							}
							captchaDetails.setNoSpace(contentLow.contains("|nospace=1"));
							captchaDetails.setNumeric(contentLow.contains("|numeric=1"));
						}
					}

				} else {
					captchaDetails = CaptchaDetails.DUMMY;
				}
			}
		}
	}


	public CaptchaDetails getCaptchaDetails() {
		return captchaDetails;
	}

}