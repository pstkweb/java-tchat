package fr.pastekweb.tchat.tests;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import fr.pastekweb.tchat.ui.TchatGUI;

public class FrameTest
{

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Tchat");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		TchatGUI tchatGUI = new TchatGUI();
		frame.add(tchatGUI.getView(), BorderLayout.CENTER);
		
		frame.pack();
	}

}
