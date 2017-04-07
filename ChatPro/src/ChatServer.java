import java.awt.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	// 判断是否接受客户端连接请求
	boolean isStarted = false;
	ServerSocket ss = null;
	Socket s = null;
	// 客户端的集合
	ArrayList<Client> cliens = new ArrayList<Client>();

	/**
	 * @param args
	 */
	// 主方法
	public static void main(String[] args) {
		new ChatServer().start();

	}

	// 服务器运行方法
	public void start() {
		try {
			ss = new ServerSocket(4444);
			isStarted = true;
		} catch (BindException e) {
			System.out.println("端口使用中!");
			System.out.println("请关闭后重新使用!");
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (isStarted) {
			try {
				s = ss.accept();
				System.out.println("A client connected");
				Client c = new Client(s);
				cliens.add(c);
				// 开启线程接受客户端消息
				new Thread(c).start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 线程
	class Client implements Runnable {
		private Socket s = null;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean isConnect = false;

		// 构造方法
		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				isConnect = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				// 接受消息并发送给每一个客户端
				while (isConnect) {
					String str = dis.readUTF();
					System.out.println(str);
					for (int i = 0; i < cliens.size(); i++) {
						cliens.get(i).sendMessage(str);
					}
				}
			} catch (IOException e) {
				System.out.println("客户端退出.");
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

		// 发送消息的请求
		public void sendMessage(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
