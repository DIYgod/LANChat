import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.Border;

public class ChatServer {
	public static void main(String[] args) throws Exception
	{
		ServerSocket server = new ServerSocket(1207);
		Socket client = server.accept();
		ChatServerFrame csf = new ChatServerFrame(client);
		csf.in.append("LinkStart!\n");
		
		BufferedReader clientin = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		while(true)					//Accept message
		{
			csf.in.append("Client: " + clientin.readLine() +"\n");
		}
	}
}

class ChatServerFrame extends JFrame
{
	JTextArea out = new JTextArea(1, 30);
	JTextArea in = new JTextArea(20, 30);
	JPanel pan = new JPanel();
	String read = null;
	Socket client;
	PrintWriter serverout;
	ChatServerFrame(Socket client)
	{
		super("ChatServer");
		Border border = BorderFactory.createLineBorder(Color.orange, 1);
	    in.setBorder(border);
	    out.setBorder(border);
		JButton send = new JButton("Send");
		send.addActionListener(new SendServerMonitor());
		pan.setLayout(new FlowLayout());
		pan.add(in);
	    pan.add(out);
		pan.add(send);
		add(pan);
		addWindowListener(new ServerWindowListener());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(350,370);
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
	
	class ServerWindowListener extends WindowAdapter
	{
		 public void windowClosing(WindowEvent e) 
		 {
				serverout.println("Server connection is broken!");
				serverout.flush();
		 }
	}
}