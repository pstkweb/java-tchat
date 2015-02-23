package fr.pastekweb.tchat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JList;

import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.model.Position;
import fr.pastekweb.tchat.server.Server;
import fr.pastekweb.tchat.ui.RoomView;
import fr.pastekweb.tchat.ui.TchatGUI;

import javax.swing.*;

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
	 * @param client The client to control
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
		if (e.getSource() == roomView.getNewMessageView().getSendButton()) {
			sendMessage(roomView);
		} else if (e.getSource() == roomView.getNewRoomButton()) {
			openNewRoom(roomView);
		} else if (e.getSource() == tchatGUI.getUsernameView().getSubmitButton()) {
			connect();
		}
	}
	
	/**
	 * Sends a new message
	 * @param roomView The room which the message is sent
	 */
	private void sendMessage(RoomView roomView)
	{
		String messageContent = roomView.getNewMessageView().getNewMessageContent().getText();
		if (messageContent.length() > 0) {
			client.sendMessage(messageContent, roomView.getRoom().getId());
			roomView.getNewMessageView().getNewMessageContent().setText("");
		}	
	}
	
	/**
	 * Opens a new room on the server and the other clients
	 * @param roomView
	 */
	private void openNewRoom(RoomView roomView)
	{
		JList<String> userList = roomView.getUserList();
		int[] selectedItems = userList.getSelectedIndices();
		ArrayList<String> userNames = new ArrayList<>();
		
		for (int i = 0; i < selectedItems.length; i++) {
			userNames.add(userList.getModel().getElementAt(selectedItems[i]));
		}
		
		client.newRoom(userNames);
	}
	
	/**
	 * Connects the user to the server by using the given
	 * user name.
	 */
	private void connect()
	{
		String username = tchatGUI.getUsernameView().getUsernameField().getText();
		
		if (client.connect(username)) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(tchatGUI.getView());
            frame.setTitle("Tchat - " + username);

            client.getTchat().setPseudo(username);

			tchatGUI.createView();
			client.askClientsList(Server.ROOM_PUBLIC_KEY);

            client.askPositionsList(Server.ROOM_PUBLIC_KEY);
		} else {
			tchatGUI.getUsernameView().setErrorMessage("Ce pseudo est déjà utilisé. Veuillez en choisir un autre.");
		}
	}

    /**
     * Return the client thread
     * @return The thread running on this interface
     */
    public IClient getClient()
    {
        return client;
    }
}
