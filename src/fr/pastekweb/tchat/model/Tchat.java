package fr.pastekweb.tchat.model;

import java.util.ArrayList;

import fr.pastekweb.tchat.event.DefaultObservable;

/**
 * The model that represents the Tchat
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class Tchat extends DefaultObservable
{
	/**
	 * The list of connected users
	 */
	private ArrayList<String> users;

	/**
	 * Initialize the Tchat
	 */
	public Tchat()
	{
		users = new ArrayList<>();
	}

	/**
	 * Adds a user to the users list
	 * @param username The user 's name
	 */
	public void addUser(String username)
	{
		users.add(username);
	}
	
	/**
	 * Removes a user from the users list
	 * @param username The user 's name
	 */
	public void removeUser(String username)
	{
		users.remove(username);
	}
	
	/**
	 * Gets the list of users
	 * @return The ArrayList of users
	 */
	public ArrayList<String> getUsers()
	{
		return users;
	}
}
