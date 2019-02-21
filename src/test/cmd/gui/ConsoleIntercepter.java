package test.cmd.gui;

import java.io.IOException;
import java.io.InputStream;
import javax.swing.JTextArea;

public class ConsoleIntercepter extends Thread {

	private JTextArea text;
	private InputStream is;

	public ConsoleIntercepter(JTextArea text, InputStream is) {
		this.text = text;
		this.is = is;
	}

	@Override
	public void run() {
		byte[] buf = new byte[1024];
		int size;
		while (true) {
			try {
				while ((size = is.read(buf)) != -1) {
					text.append(new String(buf, 0, size, "GB2312"));
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
