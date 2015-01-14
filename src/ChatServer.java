/*
 * Server of LANChat
 * Run before ChatClient
 */

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.Border;

public class ChatServer extends JFrame implements ActionListener {
	BufferedReader serverin;
	JButton send;
	JTextArea out = new JTextArea(1, 30);
	JTextArea in = new JTextArea(20, 30);
	JPanel pan = new JPanel();
	String read = null;
	Socket client;
	PrintWriter serverout;
	ChatServer(ServerSocket server)
	{
		//UI
		super("ChatServer");
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
		addWindowListener(new ServerWindowListener());
		setSize(350,370);
		setVisible(true);
		
		try {
			client = server.accept();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try {
			serverin = new BufferedReader(new InputStreamReader(client.getInputStream()));
			serverout = new  PrintWriter(client.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		in.append("LinkStart!\n");
		while(true)					//Accept message
		{
			try {
				String s = serverin.readLine();
				in.append("Client: " + s +"\n");
				if(s.equals("Client connection is broken!"))	//Exit1
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

	//ActionEvent of Button "Send"
	public void actionPerformed(ActionEvent e)											//Send message
	{
			String s = out.getText();
			in.append("Server: " + s + "\n");
			serverout.println(s);		//Send message
			serverout.flush();
			out.setText("");
	}
	
	//WindowAdapter
	class ServerWindowListener extends WindowAdapter
	{
		 public void windowClosing(WindowEvent e) 			//Exit2
		 {
			 serverout.println("Server connection is broken!");
			 serverout.flush();
			 try {
				 serverin.close();
				 serverout.close();
				 client.close();
			 } catch (IOException e1) {
				 e1.printStackTrace();
			 }
			 System.exit(0);
		 }
	}
	
	public static void main(String[] args) throws Exception
	{
		ServerSocket server = new ServerSocket(1207);
		ChatServer cs = new ChatServer(server);
	}
}