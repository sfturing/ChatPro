import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatClient extends Frame {
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	TextField textField = new TextField();
	TextArea textArea = new TextArea();
	String accStr = "";
	String writeStr = "";

	/* 感觉自己仍然需要很多的努力 */
	/**
	 * @param args
	 */

	// 主方法
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new ChatClient().launchFrame();
		} catch (Exception e) {
			System.out.println("连接服务器失败，请重新连接 .");
		}
	}

	// 窗口加载类
	public void launchFrame() {
		this.setLocation(300, 300);
		this.setSize(300, 300);
		this.add(textField, BorderLayout.SOUTH);
		this.add(textArea, BorderLayout.NORTH);
		this.pack();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				disConnect();
			}

		});
		textField.addActionListener(new TFListener());
		this.setVisible(true);
		connect();

	}

	// 连接方法
	public void connect() {
		try {
			s = new Socket("127.0.0.1", 4444);
			Server server = new Server(s);
			new Thread(server).start();
			System.out.println("Connect!");
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		} catch (UnknownHostException e) {
			System.out.println("连接服务器失败，请重新连接 .");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("连接服务器失败，请重新连接 .");
			System.exit(0);
		}
	}

	// 断开连接
	public void disConnect() {
		try {
			dos.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 输入监听内部类
	private class TFListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 输入字符
			writeStr = textField.getText().trim();
			sendStr(writeStr);
			// 获取当前时间

		}

	}

	// 线程
	class Server implements Runnable {
		private Socket s = null;
		/*
		 * private DataInputStream dis = null; private DataOutputStream dos =
		 * null;
		 */
		private boolean isConnect = false;

		Server(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			isConnect = true;

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isConnect) {
				try {
					accStr = dis.readUTF();
					textArea.setText(accStr);
				} catch (IOException e) {
					System.out.println("读取中");
				} finally {
					try {
						if (dis == null)
							dis.close();
						if (dos == null)
							dos.close();
						if (s == null)
							s.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

	}

	// 发送字符
	public void sendStr(String writeStr) {
		// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yy/mm/dd hh:mm:ss");
		// 时间
		String timeStr = sdf.format(calendar.getTime()) + "\n" + writeStr;
		accStr = accStr + "\n" + timeStr + "\n";
		sendMessage(accStr);

		textField.setText("");
		System.out.println(accStr);
	}

	public void sendMessage(String str) {
		try {
			dos.writeUTF(str);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
