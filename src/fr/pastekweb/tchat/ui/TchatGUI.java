package fr.pastekweb.tchat.ui;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JPanel;

import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.model.Room;
import fr.pastekweb.tchat.server.Server;

/**
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 *
 */
public class TchatGUI
{	
	/**
	 * The client to communicate with the Server
	 */
	private IClient client;

	/**
	 * The GUI view
	 */
	private JPanel view;
	/**
	 * The hashMap of room views mapped by rooms' id
	 */
	private HashMap<String, RoomView> roomViews;
	

	/**
	 * Initialize the GUI
	 */
	public TchatGUI(IClient client)
	{
		this.client = client;
		view = new JPanel();
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
		view.setLayout(new BorderLayout());
		view.add(roomViews.get(Server.ROOM_PUBLIC_KEY), BorderLayout.CENTER);
	}
	
	/**
	 * Gets the GUI view
	 * @return The view
	 */
	public JPanel getView()
	{
		return view;
	}

	/**
	 * Gets the client
	 * @return The {@link IClient}
	 */
	public IClient getClient()
	{
		return client;
	}
}
