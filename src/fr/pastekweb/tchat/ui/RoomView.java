package fr.pastekweb.tchat.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

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
	 * The messages view
	 */
	private MessagesView messagesView;
	/**
	 * The connected users map
	 */
	private MapView mapView;
	
	/**
	 * Initialize the {@link Room} view
	 * @param room The {@link Room}
	 */
	public RoomView(Room room)
	{
		super();
		this.room = room;
		this.room.addMessageListener(this);
		
		userList = new JList<>();
		userList.setModel(room.getUsers());
		
		messagesView = new MessagesView();
		
		mapView = new MapView();
		mapView.setModel(room.getUsers());
		
		createView();
	}
	
	/**
	 * Creates the view containing all graphical components
	 */
	private void createView()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JPanel listContainer = new JPanel();
		listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
		listContainer.add(new JLabel("Liste des utilisateurs:"));
		JScrollPane scrollPane = new JScrollPane(userList);
		scrollPane.setBackground(new Color(0,0,0,0));
		Border current = listContainer.getBorder();
		Border empty = new EmptyBorder(5, 20, 5, 0);
		if (current == null) {
			listContainer.setBorder(empty);
		} else {
			listContainer.setBorder(new CompoundBorder(empty, current));
		}
		listContainer.add(scrollPane);
		
		userList.setPreferredSize(new Dimension(250, this.getHeight()));
		this.add(listContainer);
		
		this.add(messagesView);
		messagesView.setPreferredSize(new Dimension(500, 400));
		
		current = messagesView.getBorder();
		empty = new EmptyBorder(5, 20, 5, 20);
		if (current == null) {
		    messagesView.setBorder(empty);
		} else {
			messagesView.setBorder(new CompoundBorder(empty, current));
		}
		
		mapView.setPreferredSize(new Dimension(200, 200));
		this.add(mapView);
	}
	
	/**
	 * Gets the messages' view
	 * @return The Messages View
	 */
	public MessagesView getMessagesView()
	{
		return messagesView;
	}

	@Override
	public void hasNewMessage(String from, String message)
	{
		this.messagesView.addMessage(from, message);
	}
	
	/**
	 * Gets the Room linked to this view
	 * @return The Room
	 */
	public Room getRoom()
	{
		return room;
	}
}
