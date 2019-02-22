package ww.midnite.jcatgui9kw.net.response;

public interface ResponseListener {

	void versionCheckResponse(final VersionCheckResponse response);


	void checkApiKeyResponse(final CheckApiKeyResponse response);


	void balanceQueryResponse(final BalanceQueryResponse response);


	void getCaptchaResponse(final GetCaptchaResponse response);


	void getCaptchaOkResponse(final GetCaptchaOkResponse response);


	void showCaptchaResponse(final ShowCaptchaResponse response);


	void captchaSkipResponse(final CaptchaSkipResponse response);


	void captchaAnswerResponse(final CaptchaAnswerResponse response);


	void servercheckResponse(final ServercheckResponse servercheckResponse);


	void unratedCaptchasResponse(final UnratedCaptchasResponse unratedCaptchasResponse);


	void captchaPauseResponse(final CaptchaPauseResponse captchaPauseResponse);

}
