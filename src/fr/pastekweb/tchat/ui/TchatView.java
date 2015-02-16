package fr.pastekweb.tchat.ui;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JPanel;

import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.model.Room;
import fr.pastekweb.tchat.server.Server;

public class TchatView extends JPanel
{
	private static final long serialVersionUID = 4461840737144380610L;
	/**
	 * The client of the tchat
	 */
	private IClient client;
	/**
	 * The hashMap of room views mapped by rooms' id
	 */
	private HashMap<String, RoomView> roomViews;
	
	/**
	 * Initialize the view
	 * @param client The Tchat's client
	 */
	public TchatView(IClient client)
	{
		this.client = client;
		roomViews = new HashMap<>();
		
		// Creates a RoomView for each tchat room
		Iterator<Entry<String, Room>> it = client.getTchat().getRooms().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Room> entry = it.next();
			roomViews.put(entry.getKey(), new RoomView(entry.getValue()));
		}
		
		createView();
	}

	/**
	 * Creates the view containing all graphical components
	 */
	private void createView()
	{
		this.setLayout(new BorderLayout());
		this.add(roomViews.get(Server.ROOM_PUBLIC_KEY), BorderLayout.CENTER);
	}
}
