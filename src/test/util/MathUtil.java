package test.util;

import java.util.List;

/**
 * 数学计算工具
 * 
 * @author add by ts 2017.07.27
 *
 */
public class MathUtil {
	/**
	 * 标准差
	 * 
	 * @param subList
	 * @return
	 */
	public static double stdev(List<Double> subList) {
		double rval = 0;
		double avg = 0;
		for (Double d : subList) {
			avg += d;
		}
		avg /= subList.size();
		for (int i = 0; i < subList.size(); i++) {
			rval += Math.pow((subList.get(i) - avg), 2);
		}
		rval /= subList.size() - 1;
		rval = Math.sqrt(rval);
		return rval;
	}

	/**
	 * 平均值
	 * 
	 * @param subList
	 * @return
	 */
	public static double avg(List<Double> subList) {
		double value = 0;
		for (Double d : subList) {
			value += d;
		}
		return value / subList.size();
	}

	/**
	 * 绝对值
	 * 
	 * @param subList
	 * @return
	 */
	public static double abs(Double d) {
		return Math.abs(d);
	}
}
