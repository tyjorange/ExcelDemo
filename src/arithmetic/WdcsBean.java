package arithmetic;

/**
 * 温度测试结果
 * 
 * @author add by tss 2017.07.27
 *
 */
public class WdcsBean {
	// 零偏
	private Double zoff;
	// 稳定性
	private Double stab;
	// 零偏最大值
	private Double zoffmax;
	// 零偏变化
	private Double zoffstdev;
	// 零偏温度系数
	private Double zofftmp;

	public Double getZoff() {
		return zoff;
	}

	public void setZoff(Double zoff) {
		this.zoff = zoff;
	}

	public Double getStab() {
		return stab;
	}

	public void setStab(Double stab) {
		this.stab = stab;
	}

	public Double getZoffmax() {
		return zoffmax;
	}

	public void setZoffmax(Double zoffmax) {
		this.zoffmax = zoffmax;
	}

	public Double getZoffstdev() {
		return zoffstdev;
	}

	public void setZoffstdev(Double zoffstdev) {
		this.zoffstdev = zoffstdev;
	}

	public Double getZofftmp() {
		return zofftmp;
	}

	public void setZofftmp(Double zofftmp) {
		this.zofftmp = zofftmp;
	}

	@Override
	public String toString() {
		return "WdcsBean [zoff=" + zoff + ", stab=" + stab + ", zoffmax=" + zoffmax + ", zoffstdev=" + zoffstdev + ", zofftmp=" + zofftmp + "]";
	}

}
