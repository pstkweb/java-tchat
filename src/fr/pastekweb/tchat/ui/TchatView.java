package fr.pastekweb.tchat.ui;

import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.controller.TchatController;
import fr.pastekweb.tchat.event.IRoomsListener;
import fr.pastekweb.tchat.event.IRoomsObservable;
import fr.pastekweb.tchat.model.Room;
import fr.pastekweb.tchat.server.Server;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * The main part of the view
 */
public class TchatView extends JPanel implements IRoomsListener
{
	private static final long serialVersionUID = 4461840737144380610L;
	private static final int TAB_TITLE_LENGTH = 15;

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
		this.controller = controller;
		roomViews = new HashMap<>();
		tabbedPane = new JTabbedPane();
		
		// Creates a RoomView for each tchat room
        for (Entry<String, Room> entry : client.getTchat().getRooms().entrySet()) {
            Room room = entry.getValue();

            RoomView roomView = new RoomView(room, controller);
            roomViews.put(entry.getKey(), roomView);

            if (Server.ROOM_PUBLIC_KEY.equals(entry.getKey())) {
                addTab(roomView, "Public", "Public");
            } else {
                addTab(room, roomView);
            }
            roomView.getNewMessageView().getSendButton().addActionListener(controller);
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
	 * @param roomView The RoomView
	 * @param title The tab title
	 * @param tip The tab tip
	 */
	private void addTab(RoomView roomView, String title, String tip)
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
		if (userList.length() != 0) {
			String title = userList;
	        if (userList.length() > TAB_TITLE_LENGTH) {
				title = userList.substring(0, TAB_TITLE_LENGTH);
			}
			
			if (Server.ROOM_PUBLIC_KEY.equals(room.getId())) {
				addTab(roomView, "Public", "Public");
			} else {
				addTab(roomView, title, userList);
			}
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

    private void clearTabbedPane()
    {
        tabbedPane.removeAll();
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

	@Override
	public void roomsListChanged(IRoomsObservable model)
	{
		System.out.println("Room list changed");
		clearTabbedPane();
		HashMap<String, Room> rooms = model.getRooms();

        System.out.println("Rooms id:");
        for (Entry<String, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();
            RoomView roomView;
            /*
			 * If the room view already exists, use this one
			 * else creates a new view from the room object
			 */
			if (roomViews.containsKey(entry.getKey())) {
				roomView = roomViews.get(entry.getKey());
			} else {
				roomView = new RoomView(room, controller);
			}
			addTab(room, roomView);
			roomView.getNewMessageView().getSendButton().addActionListener(controller);
			
			System.out.println("  - "+entry.getKey());
		}
	}
}
