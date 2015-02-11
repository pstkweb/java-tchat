package fr.pastekweb.tchat.client;

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
	 * @return Whether the user list have been updated well
	 */
	public void askClientsList(String roomID);
	
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
	 * Return the tchat model
	 * @return The {@link Tchat}
	 */
	public Tchat getTchat();
}
