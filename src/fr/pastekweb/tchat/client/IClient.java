package fr.pastekweb.tchat.client;

import java.util.ArrayList;

import fr.pastekweb.tchat.model.Position;
import fr.pastekweb.tchat.model.Tchat;

/**
 * Interface to represent a Client
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IClient extends Runnable
{
	/**
	 * Connects the Client on the Server
	 * @param pseudo The client pseudo
	 * 
	 * @return Whether the client is connected
	 */
	public boolean connect(String pseudo);

	/**
	 * Whether the user is connected
	 * 
	 * @return boolean whether the user is connected.
	 */
	public boolean isConnected();
	
	/**
	 * Updates the user list
	 * @param roomID The room 's id of the asked list
	 */
	public void askClientsList(String roomID);

    /**
     * Updates the user's positions list
     * @param roomID The room 's id of the asked list
     */
    public void askPositionsList(String roomID);
	
	/**
	 * Sends a public message to the given room
	 * @param message The message to send
	 */
	public void sendMessage(String message, String roomID);
	
	/**
	 * Sends a public message to everyone (broadcast)
	 * @param message The message to send
	 */
	public void sendMessage(String message);

    /**
     * Sends the current user position to the server
     * @param position The position of the user
     * @param roomID The room's id of the current room
     */
    public void sendPosition(Position position, String roomID);
	
	/**
	 * Opens a new rooms on the server
	 * @param userNames The list of user names
	 */
	public void newRoom(ArrayList<String> userNames);
	
	/**
	 * Return the tchat model
	 * @return The {@link Tchat}
	 */
	public Tchat getTchat();
}
