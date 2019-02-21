package arithmetic;

/**
 * 标度因数结果
 * 
 * @author add by ts 2017.07.31
 *
 */
public class BdysBean {
	private Double highTemperature;
	private Double normalTemperature;
	private Double lowTemperature;

	public Double getHighTemperature() {
		return highTemperature;
	}

	public void setHighTemperature(Double highTemperature) {
		this.highTemperature = highTemperature;
	}

	public Double getNormalTemperature() {
		return normalTemperature;
	}

	public void setNormalTemperature(Double normalTemperature) {
		this.normalTemperature = normalTemperature;
	}

	public Double getLowTemperature() {
		return lowTemperature;
	}

	public void setLowTemperature(Double lowTemperature) {
		this.lowTemperature = lowTemperature;
	}

	@Override
	public String toString() {
		return "BdysBean [highTemperature=" + highTemperature + ", normalTemperature=" + normalTemperature + ", lowTemperature=" + lowTemperature + "]";
	}

}
