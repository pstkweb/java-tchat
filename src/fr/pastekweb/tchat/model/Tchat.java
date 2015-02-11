package fr.pastekweb.tchat.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.pastekweb.tchat.server.Server;

/**
 * The model that represents the Tchat
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class Tchat
{	
	/**
	 * The user 's pseudo
	 */
	private String username;
	/**
	 * The Map of the tchat rooms
	 */
	private HashMap<String, Room> rooms;
	
	/**
	 * Initialize the Tchat with the default public room
	 */
	public Tchat()
	{
		rooms = new HashMap<>();
		rooms.put(Server.ROOM_PUBLIC_KEY, new Room());
	}
	
	/**
	 * Sets the user pseudo
	 * @param username The user pseudo
	 */
	public void setPseudo(String username)
	{
		this.username = username;
	}
	
	/**
	 * Gets the user pseudo
	 * @return The user name
	 */
	public String getUsername() 
	{
		return username;
	}
	
	/**
	 * Adds a {@link Room} to the list of rooms
	 * @param room The {@link Room} to add
	 */
	public void addRoom(String roomID, Room room)
	{
		rooms.put(roomID, room);
	}
	
	/**
	 * Removes a {@link Room} from the list of rooms
	 * @param room The {@link Room} to remove
	 */
	public void removeRoom(String roomID)
	{
		rooms.remove(roomID);
	}
	
	/**
	 * Gets the list of rooms
	 * @return The ArrayList of {@link Room}
	 */
	public HashMap<String, Room> getRooms()
	{
		return rooms;
	}
	
	/**
	 * Adds a user to the given room
	 * @param roomID The id of the room
	 * @param username The pseudo of the user
	 */
	public void addUser(String roomID, String username)
	{
		rooms.get(roomID).addUser(username);
	}
	
	/**
	 * Adds a new message to the tchat
	 * @param roomID The room's id
	 * @param from The user who sent the message
	 * @param message The message
	 */
	public void addMessage(String roomID, String from, String message)
	{
		rooms.get(roomID).newMessage(from, message);
	}
	
	/**
	 * Removes a user from the given room.
	 * If the given room is the public room, also remove the user
	 * from the other rooms
	 * @param roomID The room id
	 * @param username The user name
	 */
	public void removeUser(String roomID, String username)
	{
		rooms.get(roomID).removeUser(username);
		
		// If the user left the public room, he must left the other rooms
		if (roomID == Server.ROOM_PUBLIC_KEY) {
			Iterator<Entry<String, Room>> it = rooms.entrySet().iterator();
			while (it.hasNext()) {
				Room room = it.next().getValue();
				room.removeUser(username);
			}
		}
	}
}
