package test.cmd.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

public class ConsoleGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField tf_cmd;
	private JButton btn_run;
	private JTextArea ta_result;

	private Process process;
	private PrintWriter out;

	public ConsoleGUI() {
		// 窗体大小
		this.setPreferredSize(new Dimension(500, 500));
		this.setTitle("命令行GUI");

		// 命令框及按钮
		TitledBorder titledBorder = new TitledBorder("Command input");
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(titledBorder);
		topPanel.add(tf_cmd = new JTextField(), BorderLayout.CENTER);
		topPanel.add(btn_run = new JButton("执行"), BorderLayout.EAST);
		tf_cmd.setFocusTraversalKeysEnabled(false);

		this.getContentPane().add(topPanel, BorderLayout.NORTH);

		// 显示结果
		JScrollPane resultPanel = new JScrollPane(ta_result = new JTextArea(10, 10));
		this.getContentPane().add(resultPanel);
		((DefaultCaret) ta_result.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 启动一个CMD
		try {
			this.process = Runtime.getRuntime().exec("cmd");
			// 获取CMD的输出流
			this.out = new PrintWriter(process.getOutputStream());
			// 将CMD的输入流绑定到显示框中
			new ConsoleIntercepter(ta_result, process.getInputStream()).start();
			new ConsoleIntercepter(ta_result, process.getErrorStream()).start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 执行按钮 将命令发送给CMD
		this.btn_run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tf_cmd.getText().equals(""))
					return;

				ta_result.setText("");
				out.println(tf_cmd.getText());
				out.flush();
			}
		});

		this.tf_cmd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// 命令框中按下回车键的事件
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btn_run.doClick();
					tf_cmd.setText("");
				}
				// 命令框中按下TAB键的事件
				if (e.getKeyCode() == KeyEvent.VK_TAB) {
					if (!tf_cmd.getText().equals("")) {
						TabResult result = pressTab(tf_cmd.getText());
						if (result != null) {
							if (result.multi) {
								StringBuilder sb = new StringBuilder();
								for (int i = 0; i < result.filenames.length; ++i)
									sb.append(result.filenames[i]).append('\t').append((i + 1) % 5 == 0 ? "\n" : "");
								ta_result.setText(sb.toString());
							} else
								tf_cmd.setText(result.result + " ");
						} else
							Toolkit.getDefaultToolkit().beep();
					}
				}
			}
		});
	}

	// TAB 查找命令的结果
	private class TabResult {
		boolean multi;
		String result;
		String[] filenames;

		public TabResult(String result) {
			this.multi = false;
			this.result = result;
		}

		public TabResult(File[] files) {
			this.multi = true;
			String[] filenames = new String[files.length];
			for (int i = 0; i < filenames.length; ++i)
				filenames[i] = files[i].getName();
			this.filenames = filenames;
		}
	}

	// 按下TAB时的动作
	private TabResult pressTab(String startwith) {
		FilenameFilter fileFilter = new CmdFileFilter(startwith);

		// 首先查找执行路径
		File currentDir = new File("./");
		File file[] = currentDir.listFiles(fileFilter);
		if (file.length > 0)
			return file.length > 1 ? new TabResult(file) : new TabResult(file[0].getName());

		// 搜索系统PATH路径
		String[] systemEnvPath = System.getenv().get("Path").split(";");
		return checkSystemEnvPath(fileFilter, systemEnvPath, 0);
	}

	// 遍历系统路径
	private TabResult checkSystemEnvPath(FilenameFilter fileFilter, String[] systemEnvPath, int index) {
		if (index >= systemEnvPath.length)
			return null;

		File file[] = new File(systemEnvPath[index]).listFiles(fileFilter);
		if (file.length > 0)
			return file.length > 1 ? new TabResult(file) : new TabResult(file[0].getName());
		else
			return checkSystemEnvPath(fileFilter, systemEnvPath, ++index);
	}

	// TAB查找时用来过滤文件名
	private class CmdFileFilter implements FilenameFilter {
		private String startwith;

		public CmdFileFilter(String startwith) {
			this.startwith = startwith;
		}

		@Override
		public boolean accept(File dir, String name) {
			// TODO 谁知道怎么匹配系统内部命令 比如DIR这种?
			return name.startsWith(startwith) && (name.endsWith(".exe") || name.endsWith(".com") || name.endsWith(".bat"));
		}

	}

	// 显示窗体
	public void showFrame() {
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		// JDK 6U10以上版本,采用Nimbus显示风格
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			System.out.println("没有安装jre6u10,尝试当前系统的风格");
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		// 启动程序
		ConsoleGUI gui = new ConsoleGUI();
		gui.showFrame();
	}
}
