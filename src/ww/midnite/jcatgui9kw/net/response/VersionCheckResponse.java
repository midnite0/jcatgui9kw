package ww.midnite.jcatgui9kw.net.response;

import ww.midnite.util.http.HttpGetResponse;
import ww.midnite.util.update.Version;


public class VersionCheckResponse extends Response {

	private final Version version;


	public VersionCheckResponse(final HttpGetResponse response) {
		super(response);

		if (!isError()) {
			version = new Version(getString().trim());
		} else {
			version = null;
		}
	}


	public Version getVersion() {
		return version;
	}

}
