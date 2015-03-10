package fr.pastekweb.tchat.model;

import fr.pastekweb.tchat.event.DefaultObservable;
import fr.pastekweb.tchat.ui.CurrentUserView;

import java.util.ArrayList;
import java.util.Map;

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
     * Update the position of the given user
     * @param user The user for which we update the position
     * @param position The new position
     */
    public void setPosition(User user, Position position)
    {
        positions.updatePosition(user, position);
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
		
		if (usersList.size() != 0) {
			String ret = "";
			for (User user : usersList) {
				ret += user.getPseudo()+", ";
			}
			return ret;
		} else {
			return getId();
		}
	}
	
	/**
	 * Notify all users from a new message in the room
	 * @param from The user who sends the message
	 * @param message The message
	 */
	public void newMessage(String from, User to, String message)
	{
        Position senderPosition = null;
        Position currentUserPosition = null;
        for (Map.Entry<User, Position> element : positions.getHashMap().entrySet()) {
            if (element.getKey().getPseudo().equals(from)) {
                senderPosition = element.getValue();

                if (currentUserPosition != null) {
                    break;
                }
            }

            if (element.getKey().getPseudo().equals(to.getPseudo())) {
                currentUserPosition = element.getValue();

                if (senderPosition != null) {
                    break;
                }
            }
        }

        // Print the message only if listenable
        if (senderPosition != null) {
            if (senderPosition.distance(currentUserPosition) <= CurrentUserView.FIELD_OF_LISTEN / 2) {
                notifyHasNewMessage(from, message);
            }
        }
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
