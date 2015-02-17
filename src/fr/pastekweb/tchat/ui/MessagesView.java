package fr.pastekweb.tchat.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
		super();
		messagesArea = new JTextArea();
		messagesArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret) messagesArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
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
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		messagesArea.setPreferredSize(new Dimension(100, 400));
		this.add(new JLabel("Messages reçus:"));
		JScrollPane scrollPane = new JScrollPane(messagesArea);
		scrollPane.setBackground(new Color(0,0,0,0));
		
		Border current = scrollPane.getBorder();
		Border empty = new EmptyBorder(0, 0, 20, 0);
		if (current == null) {
			scrollPane.setBorder(empty);
		} else {
			scrollPane.setBorder(new CompoundBorder(empty, current));
		}
		
		current = messagesArea.getBorder();
		empty = new EmptyBorder(10, 10, 10, 10);
		if (current == null) {
			messagesArea.setBorder(empty);
		} else {
			messagesArea.setBorder(new CompoundBorder(empty, current));
		}
		
		current = newMessageContent.getBorder();
		if (current == null) {
			newMessageContent.setBorder(empty);
		} else {
			newMessageContent.setBorder(new CompoundBorder(empty, current));
		}
		this.add(scrollPane);
		
		JPanel controls = new JPanel();
		controls.setLayout(new BorderLayout());
		controls.add(new JLabel("Nouveau message:"), BorderLayout.NORTH);
		controls.add(newMessageContent, BorderLayout.CENTER);
		newMessageContent.setPreferredSize(new Dimension(controls.getWidth(), 100));
		
		controls.add(sendButton, BorderLayout.SOUTH);
		this.add(controls);
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
	
	/**
	 * Gets the send Button
	 * @return The send Button
	 */
	public JButton getSendButton()
	{
		return sendButton;
	}
	
	/**
	 * Gets the new message content text area
	 * @return The new message content
	 */
	public JTextArea getNewMessageContent()
	{
		return newMessageContent;
	}
}
