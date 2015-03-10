package fr.pastekweb.tchat.model;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Represents the users list of a {@link Room}
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class UsersList extends AbstractListModel<String>
{
	private static final long serialVersionUID = -4217087444721505988L;
	
	/**
	 * The list of users
	 */
	private ArrayList<User> users;
	
	/**
	 * Initialize the user list with empty list
	 */
	public UsersList()
	{
		users = new ArrayList<>();
	}
	
	/**
	 * Adds a user to the list
	 * @param user The user to add
	 */
	public void addUser(User user)
	{
		users.add(user);
		fireContentsChanged(this, getSize(), getSize()+1);
	}
	
	/**
	 * Removes a user from the list
	 * @param user The user to remove
	 */
	public void removeUser(User user)
	{
        for (User u : users) {
            if (u.getPseudo().equals(user.getPseudo())) {
                users.remove(u);

                fireContentsChanged(this, 0, getSize());
                return;
            }
        }
	}
	
	/**
	 * Get the list of users
	 * @return The list of users
	 */
	public ArrayList<User> getList()
	{
		return users;
	}
	
	@Override
	public int getSize()
	{
		return users.size();
	}

	@Override
	public String getElementAt(int index)
	{
		return users.get(index).toString();
	}
}
