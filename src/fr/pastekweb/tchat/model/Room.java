package fr.pastekweb.tchat.model;


import java.util.ArrayList;

import fr.pastekweb.tchat.event.DefaultObservable;

/**
 * Class to represent a Tchat Room
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class Room extends DefaultObservable
{ 	
	/**
	 * The room's id
	 */
	private String id;
	/**
	 * The list of connected users
	 */
	private UsersList users;
    /**
     * The list of connected users position
     */
    private PositionsList positions;
	
	/**
	 * Initialize the room
	 */
	public Room(String id)
	{
		super();
		this.id = id;
		users = new UsersList();
        positions = new PositionsList();
	}

	/**
	 * Adds a user to the users list
	 * @param user The user to add
	 */
	public void addUser(User user)
	{
		users.addUser(user);
	}
	
	/**
	 * Removes a user from the users list
	 * @param user The user to remove
	 */
	public void removeUser(User user)
	{
		users.removeUser(user);
        positions.removePosition(user);
	}

    /**
     * Adds a user position to the positions list
     * @param user The user for which we add the position
     * @param position The position to add
     */
    public void addPosition(User user, Position position)
    {
        positions.addPosition(user, position);
    }

	/**
	 * Gets the list of users
	 * @return The list of users
	 */
	public UsersList getUsers()
	{
		return users;
	}

    /**
     * Gets the list of users positions
     * @return The list of positions
     */
    public PositionsList getPositions()
    {
        return positions;
    }
	
	/**
	 * Gets the users list to string
	 * @return The users list to string
	 */
	public String getUsersListToString()
	{
		ArrayList<User> usersList = users.getList();
		String ret = "";
		for (User user : usersList) {
			ret += user.getPseudo()+", ";
		}
		return ret;
	}
	
	/**
	 * Notify all users from a new message in the room
	 * @param from The user who sends the message
	 * @param message The message
	 */
	public void newMessage(String from, String message)
	{
		notifyHasNewMessage(from, message);
	}
	
	/**
	 * Gets the room's id
	 * @return the room's id
	 */
	public String getId()
	{
		return id;
	}
}
