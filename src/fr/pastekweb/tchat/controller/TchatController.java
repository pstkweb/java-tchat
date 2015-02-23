package fr.pastekweb.tchat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.server.Server;
import fr.pastekweb.tchat.ui.RoomView;
import fr.pastekweb.tchat.ui.TchatGUI;

public class TchatController implements ActionListener
{
	/**
	 * The tchat view that this controller controls
	 */
	private TchatGUI tchatGUI;
	/**
	 * The tchat client
	 */
	private IClient client;

	/**
	 * Initialize the controller
	 * @param tchatView The view to control
	 */
	public TchatController(IClient client, TchatGUI tchatGUI)
	{
		this.client = client;
		this.tchatGUI = tchatGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// Gets the current messagesView (the selected panel of the tabbed pane)
		RoomView roomView = (RoomView) tchatGUI.getTchatView().getTabbedPane().getSelectedComponent();
		
		// If the event source is the send button
		if (e.getSource() == roomView.getMessagesView().getSendButton()) {
			sendMessage(roomView);
		} else if (e.getSource() == roomView.getNewRoomButton()) {
			System.out.println("New room clicked");
		} else if (e.getSource() == tchatGUI.getUsernameView().getSubmitButton()) {
			connect();
		}
	}
	
	/**
	 * Sends a new message
	 * @param messagesView The MessageView that contains the message to send 
	 */
	private void sendMessage(RoomView roomView)
	{
		String messageContent = roomView.getMessagesView().getNewMessageContent().getText();
		if (messageContent.length() > 0) {
			client.sendMessage(messageContent, roomView.getRoom().getId());
			roomView.getMessagesView().getNewMessageContent().setText("");
		}	
	}
	
	/**
	 * Connects the user to the server by using the given
	 * user name.
	 */
	private void connect()
	{
		String username = tchatGUI.getUsernameView().getUsernameField().getText();
		
		if (client.connect(username)) {
			tchatGUI.createView();
			client.askClientsList(Server.ROOM_PUBLIC_KEY);
		} else {
			tchatGUI.getUsernameView().setErrorMessage("Ce pseudo est déjà utilisé. Veuillez en choisir un autre.");
		}
	}
}
