package test.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Terminal {

	class ReaderConsole implements Runnable {
		private InputStream is;

		public ReaderConsole(InputStream is) {
			this.is = is;
		}

		public void run() {
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(is, "gbk");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			BufferedReader br = new BufferedReader(isr);

			int c = -1;
			try {
				while ((c = br.read()) != -1) {
					System.out.print((char) c);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class WrittenConsole implements Runnable {
		private OutputStream os;

		public WrittenConsole(OutputStream os) {
			this.os = os;
		}

		public void run() {
			try {
				while (true) {
					String line = this.getConsoleLine();
					line += System.lineSeparator();
					os.write(line.getBytes());
					os.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private String getConsoleLine() throws IOException {
			String line = null;
			InputStreamReader input = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(input);
			line = br.readLine();
			return line;
		}
	}

	public void execute() throws Exception {
		Process process = Runtime.getRuntime().exec("cmd");
		InputStream is = process.getInputStream();
		OutputStream os = process.getOutputStream();
		InputStream iss = process.getErrorStream();

		Thread t1 = new Thread(new ReaderConsole(is));
		Thread t2 = new Thread(new WrittenConsole(os));
		Thread t3 = new Thread(new ReaderConsole(iss));
		t1.start();
		t2.start();
		t3.start();
	}

	public static void main(String[] args) {
		Terminal t = new Terminal();
		try {
			t.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
