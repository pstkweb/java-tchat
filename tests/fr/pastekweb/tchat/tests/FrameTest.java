package fr.pastekweb.tchat.tests;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import fr.pastekweb.tchat.client.DefaultClient;
import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.server.Server;
import fr.pastekweb.tchat.ui.TchatGUI;

public class FrameTest
{

	public static void main(String[] args)
	{
		if (args.length < 1) {
			System.out.println("Usage : java Test <nomUser>");
			return;
		}
		
		IClient client = new DefaultClient();
		new Thread(client).start();
		client.connect(args[0]);
		client.askClientsList(Server.ROOM_PUBLIC_KEY);
		
		JFrame frame = new JFrame("Tchat - "+args[0]);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(1000, 700));
		
		TchatGUI tchatGUI = new TchatGUI(client);
		frame.add(tchatGUI.getView(), BorderLayout.CENTER);
		
		frame.pack();
	}

}
