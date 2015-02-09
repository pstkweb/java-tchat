package fr.pastekweb.tchat.model;

import java.util.ArrayList;

public class Room
{

	/**
	 * The list of connected users
	 */
	private ArrayList<String> users;
	
	public Room()
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
