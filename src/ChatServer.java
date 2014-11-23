import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class ChatServer {
	public static void main(String[] args) throws Exception
	{
		ServerSocket server = new ServerSocket(1207);
		Socket client = server.accept();
		ChatServerFrame csf = new ChatServerFrame(client);
		csf.in.append("LinkStart!\n");
		
		BufferedReader clientin = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		while(true)																								//Accept message
		{
			csf.in.append("Client: " + clientin.readLine() +"\n");
		}
	}
}

class ChatServerFrame extends JFrame
{
	JTextArea out = new JTextArea(1, 30);
	JTextArea in = new JTextArea(20, 30);
	String read = null;
	Socket client;
	PrintWriter serverout;
	ChatServerFrame(Socket client)
	{
		super("ChatServer");
	    in.setBorder(BorderFactory.createRaisedBevelBorder());
	    out.setLineWrap(true);
	    in.setLineWrap(true);
		Button send = new Button("Send");
		send.addActionListener(new SendServerMonitor());
	    add(out, BorderLayout.SOUTH);
		add(in, BorderLayout.CENTER);
		add(send, BorderLayout.EAST);
		pack();
		setVisible(true);
		
		this.client = client;
		try {
			serverout = new  PrintWriter(client.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	class SendServerMonitor implements ActionListener
	{
		public void actionPerformed(ActionEvent e)											//Send message
		{
			String s = out.getText();
			in.append("Server: " + s + "\n");
			serverout.println(s);
			serverout.flush();
			out.setText("");
			}
		}
	}