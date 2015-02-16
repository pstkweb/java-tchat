package fr.pastekweb.tchat.ui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessagesView extends JPanel
{
	private static final long serialVersionUID = -4096704961549943667L;
	
	private static final String NEW_LINE = "\n";
	
	/**
	 * The received message displayer
	 */
	private JTextArea messagesArea;
	/**
	 * The new message content
	 */
	private JTextArea newMessageContent;
	/**
	 * The send button
	 */
	private JButton sendButton;

	public MessagesView()
	{
		super(new BorderLayout());
		messagesArea = new JTextArea();
		messagesArea.setEditable(false);
		
		newMessageContent = new JTextArea();
		newMessageContent.setEditable(true);
		sendButton = new JButton("Envoyer");
		
		createView();
	}

	/**
	 * Creates the view containing all graphical components
	 */
	private void createView()
	{
		this.add(new JScrollPane(messagesArea), BorderLayout.CENTER);
		JPanel controls = new JPanel(new BorderLayout());
		controls.add(newMessageContent, BorderLayout.CENTER);
		controls.add(sendButton, BorderLayout.SOUTH);
		this.add(controls, BorderLayout.SOUTH);
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
