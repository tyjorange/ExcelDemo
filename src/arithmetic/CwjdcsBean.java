package arithmetic;

/**
 * 常温精度测试结果
 * 
 * @author add by tss 2017.07.27
 *
 */
public class CwjdcsBean {
	// 10m稳定性
	private Double stab10m;
	// 10s稳定性
	private Double stab10s;
	// 10m零偏
	private Double zoff10m;
	// 10s零偏
	private Double zoff10s;

	public Double getStab10m() {
		return stab10m;
	}

	public void setStab10m(Double stab10m) {
		this.stab10m = stab10m;
	}

	public Double getStab10s() {
		return stab10s;
	}

	public void setStab10s(Double stab10s) {
		this.stab10s = stab10s;
	}

	public Double getZoff10m() {
		return zoff10m;
	}

	public void setZoff10m(Double zoff10m) {
		this.zoff10m = zoff10m;
	}

	public Double getZoff10s() {
		return zoff10s;
	}

	public void setZoff10s(Double zoff10s) {
		this.zoff10s = zoff10s;
	}

	@Override
	public String toString() {
		return "CwjdcsBean [stab10m=" + stab10m + ", stab10s=" + stab10s + ", zoff10m=" + zoff10m + ", zoff10s=" + zoff10s + "]";
	}

}
