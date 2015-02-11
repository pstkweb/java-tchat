package fr.pastekweb.tchat.model;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

/**
 * Represents the users list of a {@link Room}
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class UserList extends AbstractListModel<String>
{
	private static final long serialVersionUID = -4217087444721505988L;
	
	/**
	 * The list of users
	 */
	private ArrayList<String> users;
	
	/**
	 * Initialize the user list with empty list
	 */
	public UserList()
	{
		users = new ArrayList<>();
	}
	
	/**
	 * Adds a user to the list
	 * @param username The username to add
	 */
	public void addUser(String username)
	{
		users.add(username);
		fireContentsChanged(this, getSize(), getSize()+1);
	}
	
	/**
	 * Removes a user from the list
	 * @param username The username to remove
	 */
	public void removeUser(String username)
	{
		// int index = users.indexOf(username);
		users.remove(username);
		fireContentsChanged(this, 0, getSize());
	}
	
	@Override
	public int getSize()
	{
		return users.size();
	}

	@Override
	public String getElementAt(int index)
	{
		return users.get(index);
	}
}
