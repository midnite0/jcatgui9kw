package ww.midnite.util.model;

public class Zoom {

	private final double factor;
	private final String name;


	public Zoom(final double factor) {
		this(factor, Math.round(factor * 100) + "%");
	}


	public Zoom(final double factor, final String name) {
		this.factor = factor;
		this.name = name;
	}


	public double getFactor() {
		return factor;
	}


	@Override
	public String toString() {
		return name;
	}


	@Override
	public int hashCode() {
		return 31 + (int) Double.doubleToLongBits(factor);
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return Math.abs(factor - ((Zoom) obj).factor) < 0.001;
	}

}
