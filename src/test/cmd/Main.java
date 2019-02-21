package test.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 打靠
 * 
 * @author tyj
 *
 *         2018年8月2日 - 上午11:17:57
 */
public class Main {

	public static void main(String[] args) {
		String s1 = "adb shell input tap 400 1500";
		// String s1 = "ipconfig";
		String s2 = "adb shell input tap 600 1500";
		Runtime run = Runtime.getRuntime();
		try {
			for (int i = 0; i < 10; i++) {
				if (i % 2 == 0) {
					Process process = run.exec(s1);
					process.waitFor();
					System.out.println(printCmd(process));
					process.destroy();
				} else {
					Process process = run.exec(s2);
					process.waitFor();
					System.out.println(printCmd(process));
					process.destroy();
				}
				Thread.sleep(100);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static String printCmd(Process process) throws IOException {
		BufferedReader bri = new BufferedReader(new InputStreamReader(process.getInputStream(), "GB2312"));
		BufferedReader bre = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GB2312"));
		String line = null;
		StringBuffer b = new StringBuffer();
		while ((line = bri.readLine()) != null) {
			b.append(line + "\n");
		}
		while ((line = bre.readLine()) != null) {
			b.append(line + "\n");
		}
		return b.toString();
	}
}