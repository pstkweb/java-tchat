package fr.pastekweb.tchat.ui;

import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.pastekweb.tchat.controller.TchatController;
import fr.pastekweb.tchat.event.IMessageListener;
import fr.pastekweb.tchat.model.Room;

/**
 * The {@link Room} view
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class RoomView extends JPanel implements IMessageListener
{
	private static final long serialVersionUID = -4458295478133444427L;

	/**
	 * The {@link Room}
	 */
	private Room room;
	
	/**
	 * The user list
	 */
	private JList<String> userList;
	/**
	 * Button to create a new room
	 */
	private JButton newRoomButton;
	/**
	 * The messages view
	 */
	private MessagesView messagesView;
	/**
	 * The connected users map
	 */
	private MapView mapView;

    private MessageInputView newMessageView;
	
	/**
	 * Initialize the {@link Room} view
	 * @param room The {@link Room}
	 */
	public RoomView(Room room, TchatController controller)
	{
		super();
		this.room = room;
		this.room.addMessageListener(this);
		
		userList = new JList<>();
		userList.setModel(room.getUsers());
		newRoomButton = new JButton("Ouvrir un nouveau salon");
		newRoomButton.addActionListener(controller);
		
		messagesView = new MessagesView();
		
		mapView = new MapView(controller, room);
		mapView.setModel(room.getPositions());

        newMessageView = new MessageInputView();
		
		createView();
	}
	
	/**
	 * Creates the view containing all graphical components
	 */
	private void createView()
	{
		this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        c.fill = GridBagConstraints.BOTH;

		JPanel listContainer = new JPanel();
		listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
		listContainer.add(new JLabel("Liste des utilisateurs:"));

		JScrollPane scrollPane = new JScrollPane(userList);
		listContainer.add(scrollPane);

        listContainer.add(newRoomButton);

        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 8;
		this.add(listContainer, c);

        c.gridheight = 1;
        c.gridx = 1;
        c.weightx = 48;
		this.add(messagesView, c);

        c.gridx = 2;
        c.weightx = 44;
		this.add(mapView, c);

        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 0;
        c.weightx = 100;
        this.add(newMessageView, c);
	}
	
	/**
	 * Gets the new message view
	 * @return The new message View
	 */
	public MessageInputView getNewMessageView()
	{
		return newMessageView;
	}
	
	/**
	 * Gets the Room linked to this view
	 * @return The Room
	 */
	public Room getRoom()
	{
		return room;
	}
	
	/**
	 * Gets the button to create new rooms
	 * @return The JButton
	 */
	public JButton getNewRoomButton()
	{
		return newRoomButton;
	}
	
	/**
	 * Gets the user list
	 * @return The user list (JList)
	 */
	public JList<String> getUserList()
	{
		return userList;
	}

    @Override
    public void hasNewMessage(String from, String message)
    {
        this.messagesView.addMessage(from, message);
    }
}
