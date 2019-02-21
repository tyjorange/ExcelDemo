package test.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import arithmetic.Arithmetic;
import arithmetic.BcnlBean;
import arithmetic.BdysBean;
import arithmetic.CwjdcsBean;
import arithmetic.WdcsBean;

/**
 * 自动测试结果计算工具
 * 
 * @author add by ts 2017.07.27
 *
 */
public class AutoTestUtil {
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// double res = new BigDecimal(1.23656).setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue();
		// System.out.println(res);
		jingdu();
		wendu();
		lpbcnl();
		bdysbcnl();
		bdysbc();
		lpbc();
	}

	/**
	 * 精度
	 */
	public static void jingdu() {
		String src_xlsPath = "C:\\Users\\vostor\\Documents\\TestFile\\srcFile";
		// String dist_xlsFile =
		// "C:\\Users\\vostor\\Documents\\TestFile\\distFile\\1-表头-1-老练前精度.xls";
		List<File> fileList = FileUtil.readFileOrPath(src_xlsPath, true);
		for (File channelFile : fileList) {
			try {
				String src_xlsFile = channelFile.getPath().replace("\\", "\\\\");
				String channel = channelFile.getName();
				List<Double> src_rowList = ExcelUtil.readExcel(src_xlsFile);
				CwjdcsBean bean = Arithmetic.cwjdcs(src_rowList);
				System.out.println(channel + " --> " + bean);
				// boolean writeExcel = ExcelUtil.writeExcel(src_rowList,
				// src_xlsFile, dist_xlsFile, channel);
				// if (writeExcel) {
				// System.out.println("[" + channel + "] --> [" + dist_xlsFile
				// + "] 完成");
				// } else {
				// System.out.println("[" + channel + "] --> [" + dist_xlsFile
				// + "] 失败");
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 温度
	 */
	public static void wendu() {
		String src_xlsPath = "C:\\Users\\vostor\\Documents\\TestFile\\srcFile2\\降温";
		List<File> fileList = FileUtil.readFileOrPath(src_xlsPath, true);
		for (File channelFile : fileList) {
			try {
				String src_xlsFile = channelFile.getPath().replace("\\", "\\\\");
				String channel = channelFile.getName();
				List<Double> src_rowList = ExcelUtil.readExcel(src_xlsFile);
				WdcsBean bean = Arithmetic.wdcs(src_rowList);
				System.out.println(channel + " --> " + bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 整机 温补 零偏补偿能力
	 */
	public static void lpbcnl() {
		String src_xlsPath = "C:\\Users\\vostor\\Documents\\TestFile\\srcFile3\\1__零偏补偿能力";
		List<File> fileList = FileUtil.readFileOrPath(src_xlsPath, true);
		Map<String, Map<String, List<Double>>> fileMap = new HashMap<String, Map<String, List<Double>>>();
		// 读取目录文件 按channel整理排序
		for (File channelFile : fileList) {
			try {
				String src_xlsFile = channelFile.getPath().replace("\\", "\\\\");
				String[] split = src_xlsFile.split("\\\\");
				String channel = split[split.length - 1];
				String channelName = split[split.length - 3];
				List<Double> src_rowList = ExcelUtil.readExcel(src_xlsFile);
				if (!fileMap.containsKey(channel)) {
					fileMap.put(channel, new HashMap<String, List<Double>>());
				}
				fileMap.get(channel).put(channelName, src_rowList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 开始处理文件
		for (Entry<String, Map<String, List<Double>>> entrySet : fileMap.entrySet()) {
			BcnlBean bean = Arithmetic.lpbcnlTest(entrySet.getValue());
			System.out.println(entrySet.getKey() + " --> " + bean);
		}
	}

	/**
	 * 整机 温补 标度因数补偿能力
	 */
	public static void bdysbcnl() {
		String src_xlsPath = "C:\\Users\\vostor\\Documents\\TestFile\\srcFile3\\2__标度因数补偿能力";
		List<File> fileList = FileUtil.readFileOrPath(src_xlsPath, true);
		Map<String, Map<String, List<Double>>> fileMap = new HashMap<String, Map<String, List<Double>>>();
		// 读取目录文件 按channel整理排序
		for (File channelFile : fileList) {
			try {
				String src_xlsFile = channelFile.getPath().replace("\\", "\\\\");
				String[] split = src_xlsFile.split("\\\\");
				String channel = split[split.length - 1];
				String channelName = split[split.length - 3];
				List<Double> src_rowList = ExcelUtil.readExcel(src_xlsFile);
				if (!fileMap.containsKey(channel)) {
					fileMap.put(channel, new HashMap<String, List<Double>>());
				}
				fileMap.get(channel).put(channelName, src_rowList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 开始处理文件
		for (Entry<String, Map<String, List<Double>>> entrySet : fileMap.entrySet()) {
			BcnlBean bean = Arithmetic.bdysbcnlTest(entrySet.getValue());
			System.out.println(entrySet.getKey() + " --> " + bean);
		}
	}

	/**
	 * 整机 温补 标度因数补偿
	 */
	public static void bdysbc() {
		String src_xlsPath = "C:\\Users\\vostor\\Documents\\TestFile\\srcFile3\\3__标度因数补偿";
		List<File> fileList = FileUtil.readFileOrPath(src_xlsPath, true);
		Map<String, Map<String, List<Double>>> fileMap = new HashMap<String, Map<String, List<Double>>>();
		// 读取目录文件 按channel整理排序
		try {
			for (File channelFile : fileList) {
				String src_xlsFile = channelFile.getPath().replace("\\", "\\\\");
				String[] split = src_xlsFile.split("\\\\");
				String channel = split[split.length - 1];
				String channelName = split[split.length - 3];
				List<Double> src_rowList = ExcelUtil.readExcel(src_xlsFile);
				if (!fileMap.containsKey(channel)) {
					fileMap.put(channel, new HashMap<String, List<Double>>());
				}
				fileMap.get(channel).put(channelName, src_rowList);
			}
			// 开始处理文件
			for (Entry<String, Map<String, List<Double>>> entrySet : fileMap.entrySet()) {
				BdysBean bean = Arithmetic.bdysbcTest(entrySet.getValue());
				System.out.println(entrySet.getKey() + " --> " + bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 整机 温补 零偏补偿
	 */
	public static void lpbc() {
		String src_xlsPath = "C:\\Users\\vostor\\Documents\\TestFile\\srcFile3\\4__零偏补偿";
		List<File> fileList = FileUtil.readFileOrPath(src_xlsPath, true);
		Map<String, Map<String, List<Double>>> fileMap = new HashMap<String, Map<String, List<Double>>>();
		// 读取目录文件 按channel整理排序
		try {
			for (File channelFile : fileList) {
				String src_xlsFile = channelFile.getPath().replace("\\", "\\\\");
				String[] split = src_xlsFile.split("\\\\");
				String channel = split[split.length - 1];
				String channelName = split[split.length - 3];
				List<Double> src_rowList = ExcelUtil.readExcel(src_xlsFile);
				if (!fileMap.containsKey(channel)) {
					fileMap.put(channel, new HashMap<String, List<Double>>());
				}
				fileMap.get(channel).put(channelName, src_rowList);
			}
			// 开始处理文件
			for (Entry<String, Map<String, List<Double>>> entrySet : fileMap.entrySet()) {
				BdysBean bean = Arithmetic.lpbcTest(entrySet.getValue());
				System.out.println(entrySet.getKey() + " --> " + bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
