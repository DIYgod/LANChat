import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.Border;

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
	JPanel pan = new JPanel();
	Socket client;
	PrintWriter clientout;
	ChatFrame(Socket client)
	{
		super("ChatClient");
		Border border = BorderFactory.createLineBorder(Color.orange, 1);
	    in.setBorder(border);
	    out.setBorder(border);
		JButton send = new JButton("Send");
		send.addActionListener(new SendMonitor());
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		pan.add(in);
	    pan.add(out);
		pan.add(send);
		add(pan);
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