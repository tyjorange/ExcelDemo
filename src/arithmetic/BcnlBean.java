package arithmetic;

/**
 * 补偿能力结果
 * 
 * @author add by ts 2017.07.31
 *
 */
public class BcnlBean {
	private Double m0000;
	private Double m07ff;
	private Double m0fff;
	private Double m0;
	private Double m2047;
	private Double m4095;

	public Double getM0000() {
		return m0000;
	}

	public void setM0000(Double m0000) {
		this.m0000 = m0000;
	}

	public Double getM07ff() {
		return m07ff;
	}

	public void setM07ff(Double m07ff) {
		this.m07ff = m07ff;
	}

	public Double getM0fff() {
		return m0fff;
	}

	public void setM0fff(Double m0fff) {
		this.m0fff = m0fff;
	}

	public Double getM0() {
		return m0;
	}

	public void setM0(Double m0) {
		this.m0 = m0;
	}

	public Double getM2047() {
		return m2047;
	}

	public void setM2047(Double m2047) {
		this.m2047 = m2047;
	}

	public Double getM4095() {
		return m4095;
	}

	public void setM4095(Double m4095) {
		this.m4095 = m4095;
	}

	@Override
	public String toString() {
		return "BcnlBean [m0000=" + m0000 + ", m07ff=" + m07ff + ", m0fff=" + m0fff + ", m0=" + m0 + ", m2047=" + m2047 + ", m4095=" + m4095 + "]";
	}

}
