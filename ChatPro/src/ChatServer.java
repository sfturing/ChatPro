import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	boolean isStarted = false;
	ServerSocket ss = null;
	Socket s = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ChatServer().start();

	}

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
				new Thread(c).start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	class Client implements Runnable {
		private Socket s = null;
		private DataInputStream dis = null;	;
		private boolean isConnect = false;

		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				Connect = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (isConnect) {
					String str = dis.readUTF();
					System.out.println(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					dis.close();
					s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
	}
}
