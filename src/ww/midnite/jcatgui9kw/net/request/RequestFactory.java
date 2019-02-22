package ww.midnite.jcatgui9kw.net.request;

import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

import ww.midnite.jcatgui9kw.Globals;
import ww.midnite.jcatgui9kw.net.response.ResponseListener;
import ww.midnite.util.http.HttpGet;


public class RequestFactory {

	private final ResponseListener listener;

	private final ThreadPoolExecutor threadPool;


	public RequestFactory(final ThreadPoolExecutor threadPool, final ResponseListener listener) {
		this.listener = listener;
		this.threadPool = threadPool;
	}


	protected ResponseListener getListener() {
		return listener;
	}


	protected ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}


	public CheckApiKey getCheckApiKey() {
		return new CheckApiKey(this);
	}


	public GetCaptcha getGetCaptcha() {
		return new GetCaptcha(this);
	}


	public GetCaptchaOk getGetCaptchaOk() {
		return new GetCaptchaOk(this);
	}


	public ShowCaptcha getShowCaptcha() {
		return new ShowCaptcha(this);
	}


	public BalanceQuery getBalanceQuery() {
		return new BalanceQuery(this);
	}


	public UnratedCaptchas getUnratedCaptchas() {
		return new UnratedCaptchas(this);
	}


	public Servercheck getServercheck() {
		return new Servercheck(this);
	}


	public VersionCheck getVersionCheck() {
		return new VersionCheck(this);
	}


	public CaptchaSkip getCaptchaSkip() {
		return new CaptchaSkip(this);
	}


	public CaptchaPause getCaptchaPause() {
		return new CaptchaPause(this);
	}


	public CaptchaAnswer getCaptchaAnswer() {
		return new CaptchaAnswer(this);
	}


	public HttpGet getHttpGet(final URL url) {
		return new HttpGet(url, Globals.log);
	}

}
