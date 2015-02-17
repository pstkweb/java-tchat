package fr.pastekweb.tchat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.ui.RoomView;
import fr.pastekweb.tchat.ui.TchatView;

public class TchatController implements ActionListener
{
	/**
	 * The tchat view that this controller controls
	 */
	private TchatView tchatView;
	/**
	 * The tchat client
	 */
	private IClient client;

	/**
	 * Initialize the controller
	 * @param tchatView The view to control
	 */
	public TchatController(IClient client)
	{
		this.client = client;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("Send button clicked");
		// Gets the current messagesView (the selected panel of the tabbed pane)
		RoomView roomView = (RoomView) tchatView.getTabbedPane().getSelectedComponent();
		
		// If the event source is the send button
		if (e.getSource() == roomView.getMessagesView().getSendButton()) {
			sendMessage(roomView);
		}
	}
	
	/**
	 * Sends a new message
	 * @param messagesView The MessageView that contains the message to send 
	 */
	private void sendMessage(RoomView roomView)
	{
		String messageContent = roomView.getMessagesView().getNewMessageContent().getText();
		client.sendMessage(messageContent, roomView.getRoom().getId());
		roomView.getMessagesView().getNewMessageContent().setText("");
	}

	/**
	 * Sets the tchat view linked to this controller
	 * @param tchatView The TchatView
	 */
	public void setTchatView(TchatView tchatView)
	{
		this.tchatView = tchatView;
	}
}
