package ww.midnite.util.model;

public class Failure {

	private final String code;
	private final String text;


	public Failure(final String text) {
		this(null, text);
	}


	public Failure(final String code, final String text) {
		this.code = code;
		this.text = text;
	}


	public String getCode() {
		return code;
	}


	public String getText() {
		return text;
	}


	@Override
	public String toString() {
		return code != null ? code + " " + text : text;
	}

}
