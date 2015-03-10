package fr.pastekweb.tchat.model;

import fr.pastekweb.tchat.event.DefaultRoomsObservable;
import fr.pastekweb.tchat.server.Server;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * The model that represents the Tchat
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class Tchat extends DefaultRoomsObservable
{	
	/**
	 * The user's
	 */
	private User user;
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
		rooms.put(Server.ROOM_PUBLIC_KEY, new Room(Server.ROOM_PUBLIC_KEY));
		user = new User();
	}
	
	/**
	 * Sets the user pseudo
	 * @param username The user pseudo
	 */
	public void setPseudo(String username)
	{
		user.setPseudo(username);
	}
	
	/**
	 * Gets the user pseudo
	 * @return The user name
	 */
	public User getUser() 
	{
		return user;
	}
	
	/**
	 * Adds a {@link Room} to the list of rooms
	 * @param room The {@link Room} to add
	 */
	public void addRoom(Room room)
	{
		rooms.put(room.getId(), room);
		notifyRoomsListHasChanged();	
	}
	
	/**
	 * Removes a {@link Room} from the list of rooms
	 * @param room The {@link Room} to remove
	 */
	public void removeRoom(Room room)
	{
		rooms.remove(room.getId());
		notifyRoomsListHasChanged();
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
	 * @param user The user
	 */
	public void addUser(String roomID, User user)
	{
		rooms.get(roomID).addUser(user);
	}
	
	/**
	 * Adds a new message to the tchat
	 * @param roomID The room's id
	 * @param from The user who sent the message
	 * @param message The message
	 */
	public void addMessage(String roomID, String from, String message)
	{
		rooms.get(roomID).newMessage(from, user, message);
	}

    /**
     * Adds a user's position to the given room
     * @param roomID The room's id
     * @param user The user for which we add the position
     * @param position The position
     */
    public void addPosition(String roomID, User user, Position position)
    {
        rooms.get(roomID).addPosition(user, position);
    }

    /**
     * Update the position of the given user in the given room
     * @param roomID The room's id
     * @param user The user for which we update the position
     * @param position The position
     */
    public void setUserPosition(String roomID, User user, Position position)
    {
        rooms.get(roomID).setPosition(user, position);
    }

	/**
	 * Removes a user from the given room.
	 * If the given room is the public room, also remove the user
	 * from the other rooms
	 * @param roomID The room id
	 * @param user The user
	 */
	public void removeUser(String roomID, User user)
	{
		rooms.get(roomID).removeUser(user);
		
		// If the user left the public room, he must left the other rooms
		if (roomID.equals(Server.ROOM_PUBLIC_KEY)) {
            for (Entry<String, Room> stringRoomEntry : rooms.entrySet()) {
                Room room = stringRoomEntry.getValue();
                room.removeUser(user);
            }
		}
	}
}
