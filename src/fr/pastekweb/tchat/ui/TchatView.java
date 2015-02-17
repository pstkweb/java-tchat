package fr.pastekweb.tchat.ui;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.controller.TchatController;
import fr.pastekweb.tchat.event.IRoomsListener;
import fr.pastekweb.tchat.event.IRoomsObservable;
import fr.pastekweb.tchat.model.Room;
import fr.pastekweb.tchat.server.Server;

public class TchatView extends JPanel implements IRoomsListener
{
	private static final long serialVersionUID = 4461840737144380610L;
	private static final int TAB_TITLE_LENGTH = 15;
	/**
	 * The client of the tchat
	 */
	private IClient client;
	/**
	 * The tabbed pane containing all the room Views
	 */
	private JTabbedPane tabbedPane;
	/**
	 * The hashMap of room views mapped by rooms' id
	 */
	private HashMap<String, RoomView> roomViews;
	/**
	 * The tchat controller
	 */
	private TchatController controller;
	
	/**
	 * Initialize the view
	 * @param client The Tchat's client
	 */
	public TchatView(IClient client, TchatController controller)
	{
		this.client = client;
		this.controller = controller;
		controller.setTchatView(this);
		roomViews = new HashMap<>();
		tabbedPane = new JTabbedPane();
		
		// Creates a RoomView for each tchat room
		Iterator<Entry<String, Room>> it = client.getTchat().getRooms().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Room> entry = it.next();
			Room room = entry.getValue();
			
			RoomView roomView = new RoomView(room);
			roomViews.put(entry.getKey(), roomView);
		
			if (entry.getKey() == Server.ROOM_PUBLIC_KEY) {
				addTab(room, roomView, "Public", "Public");
			} else {
				addTab(room, roomView);
			}
			roomView.getMessagesView().getSendButton().addActionListener(controller);
		}
		createView();
	}

	/**
	 * Creates the view containing all graphical components
	 */
	private void createView()
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane);
	}
	
	/**
	 * Add a tab to the tabbed pane
	 * @param room The Room
	 * @param roomView The RoomView
	 * @param title The tab title
	 * @param tip The tab tip
	 */
	private void addTab(Room room, RoomView roomView, String title, String tip)
	{
		ImageIcon icon = new ImageIcon("images/ic_tchat.png");
		tabbedPane.addTab(title, icon, roomView, tip);
	}
	
	/**
	 * Add a tab to the tabbed pane
	 * @param room The Room
	 * @param roomView The RoomView
	 */
	private void addTab(Room room, RoomView roomView)
	{
		String userList = room.getUsersListToString();
		String title;
		if (userList.length() > TAB_TITLE_LENGTH) {
			title = userList.substring(0, TAB_TITLE_LENGTH);
		} else {
			title = room.getUsersListToString();
		}
		
		addTab(room, roomView, title, userList);
	}

	@Override
	public void roomsListChanged(IRoomsObservable model)
	{
		tabbedPane = new JTabbedPane();
		HashMap<String, Room> rooms = model.getRooms();
		
		Iterator<Entry<String, Room>> it = rooms.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, Room> entry = it.next();
			
			Room room = entry.getValue();
			RoomView roomView;
			/*
			 * If the room view already exists, use this one
			 * else creates a new view from the room object
			 */
			if (roomViews.containsKey(entry.getKey())) {
				roomView = roomViews.get(entry.getKey());
			} else {
				roomView = new RoomView(room);
			}
			addTab(room, roomView);
			roomView.getMessagesView().getSendButton().addActionListener(controller);
		}
	}
	
	/**
	 * Gets the tabbedpane
	 * @return The tabbedpane
	 */
	public JTabbedPane getTabbedPane()
	{
		return tabbedPane;
	}
}
