package fr.pastekweb.tchat.ui;


import javax.swing.*;
import java.awt.*;

/**
 * The view for sending messages
 *
 * @author Thomas TRIBOULT on 23/02/2015.
 */
public class MessageInputView extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5717673186826840422L;
	/**
     * The new message content
     */
    private JTextField newMessageContent;
    /**
     * The send button
     */
    private JButton sendButton;

    /**
     * Instantiate the view
     */
    public MessageInputView()
    {
        newMessageContent = new JTextField();
        newMessageContent.setEditable(true);

        sendButton = new JButton("Envoyer");

        createView();
    }

    private void createView()
    {
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 100;
        add(newMessageContent, c);

        c.gridx = 1;
        c.weightx = 0;
        add(sendButton, c);
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
    public JTextField getNewMessageContent()
    {
        return newMessageContent;
    }
}
