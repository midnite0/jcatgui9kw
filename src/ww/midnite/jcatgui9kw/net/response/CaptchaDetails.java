package ww.midnite.jcatgui9kw.net.response;

import java.awt.Point;


public class CaptchaDetails {

	public static final CaptchaDetails DUMMY = new CaptchaDetails(-1, null);

	public static final CaptchaDetails NO_CAPTCHA = new CaptchaDetails(0, null);

	private final int id;
	private final CaptchaType type;

	private String textAnswer;
	private Point mouseAnswer;
	private int minLength;
	private boolean noSpace;

	private boolean numeric;


	public CaptchaDetails(final int id, final CaptchaType type) {
		this.id = id;
		this.type = type;
	}


	public int getId() {
		return id;
	}


	public CaptchaType getType() {
		return type;
	}


	public CaptchaDetails setTextAnswer(final String answer) {
		this.textAnswer = answer;
		return this;
	}


	public String getTextAnswer() {
		return textAnswer;
	}


	public CaptchaDetails setMouseAnswer(final int x, final int y) {
		this.mouseAnswer = new Point(x, y);
		return this;
	}


	public Point getMouseAnswer() {
		return mouseAnswer;
	}


	public CaptchaDetails setMinLength(final int minLength) {
		this.minLength = minLength;
		return this;
	}


	public int getMinLength() {
		return minLength;
	}


	public void setNumeric(final boolean numeric) {
		this.numeric = numeric;
	}


	public boolean isNumeric() {
		return numeric;
	}


	public void setNoSpace(final boolean noSpace) {
		this.noSpace = noSpace;
	}


	public boolean isNoSpace() {
		return noSpace;
	}


	public boolean isValid() {
		return id > 0 && type != null;
	}


	public boolean isConfirm() {
		return type == CaptchaType.CONFIRM_TEXT || type == CaptchaType.CONFIRM_MOUSE;
	}


	@Override
	public String toString() {
		return "CaptchaDetails [id=" + id + ", type=" + type + ", textAnswer=" + textAnswer + ", mouseAnswer="
				+ mouseAnswer + ", minLength=" + minLength + ", noSpace=" + noSpace + ", numeric=" + numeric + "]";
	}

}
