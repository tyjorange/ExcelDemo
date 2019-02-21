package arithmetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import test.util.MathUtil;

/**
 * 算法工具类
 * 
 * @author add by tss 2017.07.27
 *
 */
public class Arithmetic {
	private static final int INDEX_SRATR_10S = 11;
	private static final int INDEX_END_10S = 3610;
	private static final int INDEX_SRATR_10M = 601;
	private static final int INDEX_END_10M = 4200;

	/**
	 * 常温精度测试算法
	 * 
	 * @param cwjdcsList
	 *            常温精度测试原始数据列表
	 * @return cwjdcsBean 常温精度测试结果
	 */
	public static CwjdcsBean cwjdcs(List<Double> cwjdcsList) {
		if (cwjdcsList == null) return null;
		CwjdcsBean cb = new CwjdcsBean();
		double zoff_10s = getZoff(cwjdcsList, INDEX_SRATR_10S, INDEX_END_10S);
		cb.setZoff10s(zoff_10s);
		double zoff_10m = getZoff(cwjdcsList, INDEX_SRATR_10M, INDEX_END_10M);
		cb.setZoff10m(zoff_10m);
		double stab_10s = getStab(cwjdcsList, INDEX_SRATR_10S, INDEX_END_10S);
		cb.setStab10s(stab_10s);
		double stab_10m = getStab(cwjdcsList, INDEX_SRATR_10M, INDEX_END_10M);
		cb.setStab10m(stab_10m);
		return cb;
	}

	/**
	 * 温度测试算法
	 * 
	 * @param wdcsList
	 *            温度测试原始数据列表
	 * @return wdcsBean 温度测试结果
	 */
	public static WdcsBean wdcs(List<Double> wdcsList) {
		if (wdcsList == null) return null;
		WdcsBean wb = new WdcsBean();
		double zoff = getZoff(wdcsList);
		wb.setZoff(zoff);
		double stab = getStab(wdcsList);
		wb.setStab(stab);
		double zoffmax = getZoffmax(wdcsList);
		wb.setZoffmax(zoffmax);
		double zoffStdev = getZoffStdev(wdcsList);
		wb.setZoffstdev(zoffStdev);
		double zofftmp = getZofftmp(zoffmax);
		wb.setZofftmp(zofftmp);
		return wb;
	}

	/**
	 * 计算零偏值
	 * 
	 * @param cwjdcsList
	 *            常温精度测试原始数据列表
	 * @param start
	 *            数据起始位
	 * @param end
	 *            数据结束位
	 * @return
	 */
	private static double getZoff(List<Double> cwjdcsList, int start, int end) {
		if (cwjdcsList == null) return 0;
		int fromIndex = start - 1;
		int toIndex = end;
		// 取Column A数据分段 计算Zoff值
		List<Double> mColumnA = new ArrayList<Double>(cwjdcsList.subList(fromIndex, toIndex));
		return getZoff(mColumnA);
	}

	/**
	 * 计算偏零值
	 * 
	 * @param wdcsList
	 *            原始数据列表
	 * @return
	 */
	private static double getZoff(List<Double> wdcsList) {
		if (wdcsList == null) return 0;
		return MathUtil.avg(wdcsList) * 1000 / 15;
	}

	/**
	 * 计算零偏稳定性
	 * 
	 * @param cwjdcsList
	 *            常温精度测试原始数据列表
	 * @param start
	 *            数据起始位
	 * @param end
	 *            数据结束位
	 * @return
	 */
	private static double getStab(List<Double> cwjdcsList, int start, int end) {
		if (cwjdcsList == null) return 0;
		List<Double> mColumnB = getAvgByStep(cwjdcsList);
		int fromIndex = start - 1;
		int toIndex = end;
		// 取Column B数据分段 计算Stab值
		List<Double> subColumnB = new ArrayList<Double>(mColumnB.subList(fromIndex, toIndex));
		return MathUtil.stdev(subColumnB) / 15 * 3600;
	}

	/**
	 * 计算零偏稳定性
	 * 
	 * @param wdcsList
	 *            原始数据列表
	 * @return
	 */
	private static double getStab(List<Double> wdcsList) {
		if (wdcsList == null) return 0;
		List<Double> mColumnB = getAvgByStep(wdcsList);
		return MathUtil.stdev(mColumnB) / 15 * 3600;
	}

	private static final int Step = 10;// 取数据分段的个数

	/**
	 * Step 个数据为一组 计算平均值
	 * 
	 * @param mList
	 *            原始数据列表
	 * @return
	 */
	private static List<Double> getAvgByStep(List<Double> mList) {
		List<Double> result = new ArrayList<Double>();
		// 计算Column B 的数据
		int maxSize = mList.size();
		for (int i = 0; i < maxSize; i++) {
			int fromIndex = i;
			int toIndex = i + Step;
			// 取 setp 个 Column A 的数据为一组
			List<Double> subColumn = new ArrayList<Double>(mList.subList(fromIndex, toIndex > maxSize ? maxSize : toIndex));
			result.add(MathUtil.avg(subColumn) * 1000);
		}
		return result;
	}

	/**
	 * 计算零偏最大差值
	 * 
	 * @param wdcsList
	 *            原始数据列表
	 * @return
	 */
	private static double getZoffmax(List<Double> wdcsList) {
		if (wdcsList == null) return 0;
		List<Double> avgByStep = getAvgByStep(wdcsList);
		LinkedList<Double> linkedList = new LinkedList<Double>(avgByStep);
		Collections.sort(linkedList, new Comparator<Double>() {
			public int compare(Double d1, Double d2) {
				// 升序排序
				return d1.compareTo(d2);
			}
		});
		double max = linkedList.getLast();
		double min = linkedList.getFirst();
		return (max - min) / 15;
	}

	/**
	 * 计算零偏变化
	 * 
	 * @param wdcsList
	 *            原始数据列表
	 * @return
	 */
	private static double getZoffStdev(List<Double> wdcsList) {
		if (wdcsList == null) return 0;
		List<Double> avgByStep = getAvgByStep(wdcsList);
		return MathUtil.stdev(avgByStep) / 15;
	}

	/**
	 * 计算零偏温度系数
	 * 
	 * @param zoffmax
	 *            零偏最大差值
	 * @return
	 */
	private static double getZofftmp(Double zoffmax) {
		return zoffmax * 3600 / 100;
	}

	/**
	 * 整机 温补 零偏补偿能力
	 * 
	 * @param map
	 *            通道原始数据
	 * @return
	 */
	public static BcnlBean lpbcnlTest(Map<String, List<Double>> map) {
		BcnlBean bb = new BcnlBean();
		double m0000 = getK(map.get("1__0000"));
		bb.setM0000(m0000);
		double m07ff = getK(map.get("2__07ff"));
		bb.setM07ff(m07ff);
		double m0fff = getK(map.get("3__0fff"));
		bb.setM0fff(m0fff);
		double m0 = getN(m07ff, m0000);
		bb.setM0(m0);
		double m2047 = getN(m0fff, m07ff);
		bb.setM2047(m2047);
		return bb;
	}

	private static double getK(List<Double> d) {
		return MathUtil.avg(d) * 1000;
	}

	private static double getN(Double a, Double b) {
		return a - b;
	}

	private static final int INDEX_SRATR_SF1 = 1;
	private static final int INDEX_END_SF1 = 30;
	private static final int INDEX_SRATR_SF2 = 31;
	private static final int INDEX_END_SF2 = 60;

	/**
	 * 整机 温补 标度因数补偿能力
	 * 
	 * @param map
	 *            通道原始数据
	 * @return
	 */
	public static BcnlBean bdysbcnlTest(Map<String, List<Double>> map) {
		BcnlBean bb = new BcnlBean();
		double m0000 = getL(map.get("1__0000"));
		bb.setM0000(m0000);
		double m07ff = getL(map.get("2__07ff"));
		bb.setM07ff(m07ff);
		double m0fff = getL(map.get("3__0fff"));
		bb.setM0fff(m0fff);
		double m0 = getN(m0000, m07ff);
		bb.setM0(m0);
		double m2047 = getN(m07ff, m0fff);
		bb.setM2047(m2047);
		return bb;

	}

	private static double getL(List<Double> d) {
		List<Double> subList1 = new ArrayList<Double>(d.subList(INDEX_SRATR_SF1 - 1, INDEX_END_SF1));
		List<Double> subList2 = new ArrayList<Double>(d.subList(INDEX_SRATR_SF2 - 1, INDEX_END_SF2));
		double avg1 = MathUtil.avg(subList1);
		double avg2 = MathUtil.avg(subList2);
		return MathUtil.abs(avg1 - avg2) * 1000 / 2;
	}

	/**
	 * 整机 温补 标度因数补偿
	 * 
	 * @param map
	 * @return
	 */
	public static BdysBean bdysbcTest(Map<String, List<Double>> map) {
		BdysBean bb = new BdysBean();
		double high = getL(map.get("1__高温"));
		bb.setHighTemperature(high);
		double normal = getL(map.get("2__常温（07ff）"));
		bb.setNormalTemperature(normal);
		double low = getL(map.get("3__低温"));
		bb.setLowTemperature(low);
		return bb;

	}

	/**
	 * 整机 温补 零偏补偿
	 * 
	 * @param map
	 * @return
	 */
	public static BdysBean lpbcTest(Map<String, List<Double>> map) {

		return null;

	}
}
