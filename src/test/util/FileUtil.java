package test.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件、文件夹 读取工具
 * 
 * @author add by ts 2017.07.27
 *
 */
public class FileUtil {
	private static List<File> fList = null;

	/**
	 * 获取文件夹下的文件
	 * 
	 * @param filePath
	 *            文件或文件夹路径
	 * @param reset
	 *            是否 重置参数
	 * @return
	 */
	public static List<File> readFileOrPath(String filePath, boolean reset) {
		if (reset) {
			fList = new ArrayList<File>();
		}
		try {
			File file = new File(filePath);
			if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filePath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
						fList.add(readfile);
					} else if (readfile.isDirectory()) {
						readFileOrPath(filePath + "\\" + filelist[i], false);
					}
				}
			}
			return fList;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
		return null;
	}
}
