package ww.midnite.jcatgui9kw.net.request;

import java.util.Date;


public abstract class Request {

	private final Date createTime;
	private final RequestFactory factory;


	protected Request(final RequestFactory factory0) {
		createTime = new Date();
		factory = factory0;
	}


	public Date getCreateTime() {
		return createTime;
	}


	protected RequestFactory getFactory() {
		return factory;
	}
}
