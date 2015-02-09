package fr.pastekweb.tchat.client;

import java.util.List;



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
	 * @return Whether the user list have been updated well
	 */
	public void askClientsList();
	
	/**
	 * Sends a private message to the given receivers
	 * @param clients The list of receivers
	 * @param message The message to send
	 */
	public void sendPrivateMessage(List<String> clients, String message);
	
	/**
	 * Sends a public message to everyone (broadcast)
	 * @param message The message to send
	 */
	public void sendPublicMessage(String message);
}
