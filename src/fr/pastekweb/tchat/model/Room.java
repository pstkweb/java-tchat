package fr.pastekweb.tchat.model;


import fr.pastekweb.tchat.event.DefaultObservable;

/**
 * Class to represent a Tchat Room
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class Room extends DefaultObservable
{ 	
	/**
	 * The list of connected users
	 */
	private UserList users;
	
	/**
	 * Initialize the room
	 */
	public Room()
	{
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
	 * 
	 * @param from
	 * @param message
	 */
	public void newMessage(String from, String message)
	{
		notifyHasNewMessage(from, message);
	}
	
	
}
