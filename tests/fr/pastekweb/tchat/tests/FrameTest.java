package fr.pastekweb.tchat.tests;

import fr.pastekweb.tchat.client.DefaultClient;
import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.ui.TchatGUI;

import javax.swing.*;
import java.awt.*;

public class FrameTest
{

	public static void main(String[] args)
	{
		IClient client = new DefaultClient();
		new Thread(client).start();
		
		JFrame frame = new JFrame("Tchat");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(1000, 700));
		
		TchatGUI tchatGUI = new TchatGUI(client);
		frame.add(tchatGUI.getView(), BorderLayout.CENTER);
		
		frame.pack();
	}

}
