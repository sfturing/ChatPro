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
	boolean isStarted = false;
	ServerSocket ss = null;
	Socket s = null;
	ArrayList<Client> cliens = new ArrayList<Client>();

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
				cliens.add(c);
				new Thread(c).start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	
	class Client implements Runnable {
		private Socket s = null;
		private DataInputStream dis = null;	
		private DataOutputStream dos = null;
		private boolean isConnect = false;

		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos=new DataOutputStream(s.getOutputStream());
				isConnect = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
 
		public void run() {
			try {
				while (isConnect) {
					String str = dis.readUTF();
					System.out.println(str);
					for(int i=0;i<cliens.size();i++){
						cliens.get(i).sendMessage(str);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(dis!=null) dis.close();
					if(dos!=null) dos.close();
					if(s!=null) s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
		public void sendMessage(String str){
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
