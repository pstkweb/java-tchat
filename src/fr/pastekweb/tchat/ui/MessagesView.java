package fr.pastekweb.tchat.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class MessagesView extends JPanel
{
	private static final long serialVersionUID = -4096704961549943667L;
	
	private static final String NEW_LINE = "\n";
	
	/**
	 * The received message displayer
	 */
	private JTextArea messagesArea;

	public MessagesView()
	{
		super();
		messagesArea = new JTextArea();
		messagesArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret) messagesArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		createView();
	}

	/**
	 * Creates the view containing all graphical components
	 */
	private void createView()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(new JLabel("Messages reçus:"));

		JScrollPane scrollPane = new JScrollPane(messagesArea);
		this.add(scrollPane);
	}
	
	/**
	 * Adds a message to the messages area
	 * @param from The clients who sent the message
	 * @param message The message
	 */
	public void addMessage(String from, String message)
	{
		messagesArea.append(from+": "+NEW_LINE);
		messagesArea.append(message+NEW_LINE+NEW_LINE);
	}
}
