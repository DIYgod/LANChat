import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class ChatClient {
	public static void main(String[] args) throws Exception{
			Socket client = new Socket("127.0.0.1",1207);
			ChatFrame cf = new ChatFrame(client);
			BufferedReader serverin = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while(true)																									//Accept message
			{
				cf.in.append("Server: " + serverin.readLine() + "\n");
			}
	}
}
	

class ChatFrame extends JFrame
{
	JTextArea out = new JTextArea(1, 30);
	JTextArea in = new JTextArea(20, 30);
	Socket client;
	PrintWriter clientout;
	ChatFrame(Socket client)
	{
		super("ChatClient");
	    in.setBorder(BorderFactory.createRaisedBevelBorder());
	    out.setLineWrap(true);
	    in.setLineWrap(true);
		Button send = new Button("Send");
		send.addActionListener(new SendMonitor());
	    add(out, BorderLayout.SOUTH);
		add(in, BorderLayout.CENTER);
		add(send, BorderLayout.EAST);
		pack();
		setVisible(true);
		

		this.client = client;
		try {
			clientout= new  PrintWriter(client.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	class SendMonitor implements ActionListener
	{
		public void actionPerformed(ActionEvent e)									//Send message
		{
			String s = out.getText();
			in.append("Client: " + s + "\n");
			clientout.println(s);
			clientout.flush();
			out.setText("");
		}
	}
}