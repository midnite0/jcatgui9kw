package ww.midnite.jcatgui9kw.net.request;

import java.time.LocalDateTime;


public abstract class Request {

	private final LocalDateTime createTime;
	private final RequestFactory factory;


	protected Request(final RequestFactory factory0) {
		createTime = LocalDateTime.now();
		factory = factory0;
	}


	public LocalDateTime getCreateTime() {
		return createTime;
	}


	protected RequestFactory getFactory() {
		return factory;
	}
}
