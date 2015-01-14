/*
 * Client of LANChat
 * Run after ChatServer
 */

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.Border;

public class ChatClient extends JFrame implements ActionListener {
	BufferedReader clientin;
	JButton send;
	JTextArea out = new JTextArea(1, 30);
	JTextArea in = new JTextArea(20, 30);
	JPanel pan = new JPanel();
	Socket client;
	PrintWriter clientout;
	ChatClient(Socket client)
	{
		//UI
		super("ChatClient");
		Border border = BorderFactory.createLineBorder(Color.orange, 1);
	    in.setBorder(border);
	    out.setBorder(border);
		send = new JButton("Send");
		send.addActionListener(this);
		pan.setLayout(new FlowLayout());
		pan.add(in);
	    pan.add(out);
		pan.add(send);
		add(pan);
		addWindowListener(new ClientWindowListener());
		setSize(350,370);
		setVisible(true);

		this.client = client;
		try {
			clientin = new BufferedReader(new InputStreamReader(client.getInputStream()));
			clientout= new  PrintWriter(client.getOutputStream());
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		while(true)						//Accept message
		{
			try {
				String s = clientin.readLine();
				in.append("Server: " + s + "\n");
				if(s.equals("Server connection is broken!"))		//Exit1
				{
					send.setEnabled(false);
					in.append("Exit in 5 seconds...\n");
					Thread.currentThread().sleep(5000);
					System.exit(0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//ActionListener of Button "Send"
	public void actionPerformed(ActionEvent e)
	{
			String s = out.getText();
			in.append("Client: " + s + "\n");
			clientout.println(s);			//Send message
			clientout.flush();
			out.setText("");
	}
	
	class ClientWindowListener extends WindowAdapter
	{
		 public void windowClosing(WindowEvent e) 			//Exit2
		 {
			 clientout.println("Client connection is broken!");
			 clientout.flush();
			 try {
				 clientin.close();
				 clientout.close();
				 client.close();
			 } catch (IOException e1) {
				 e1.printStackTrace();
			 }
			 System.exit(0);
		 }
	}
	
	public static void main(String[] args) throws Exception{
			Socket client = new Socket("127.0.0.1",1207);
			ChatClient cf = new ChatClient(client);
	}
}