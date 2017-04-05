import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends Frame {
	Socket s=null;
	DataOutputStream dos = null;
	TextField textField=new TextField();
	TextArea textArea=new TextArea();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatClient().launchFrame();
	}
	public void launchFrame(){
		this.setLocation(300, 300);
		this.setSize(300, 300);
		this.add(textField,BorderLayout.SOUTH);
		this.add(textArea,BorderLayout.NORTH);
		this.pack();
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				disConnect();
			}
			
		});
		this.textField.addActionListener(new TFListener());
		this.setVisible(true);
		connect();
	}
	public void connect(){
		try {
			s=new Socket("127.0.0.1",4444);
			dos=new DataOutputStream(s.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendMessage(String str){
		try {
			dos.writeUTF(str);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void disConnect(){
		try {
			dos.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private class TFListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String str=textField.getText().trim();
			sendMessage(str);
			textArea.setText(str);
			textField.setText("");
			
		}
		
	}

}
