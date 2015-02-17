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
	private UserList users;
	
	/**
	 * Initialize the room
	 */
	public Room(String id)
	{
		super();
		this.id = id;
		users = new UserList();
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
	}
	
	/**
	 * Gets the list of users
	 * @return The ArrayList of users
	 */
	public UserList getUsers()
	{
		return users;
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
	 * 
	 * @param from
	 * @param message
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
