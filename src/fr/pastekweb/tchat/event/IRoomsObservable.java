package fr.pastekweb.tchat.event;

import fr.pastekweb.tchat.model.Room;

import java.util.HashMap;

/**
 * Interface to implements the Observable pattern on rooms' state
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IRoomsObservable
{
	/**
	 * Adds a rooms listener to the Observable Object
	 * @param listener The {@link IRoomsListener} to add
	 */
	public void addRoomsListener(IRoomsListener listener);
	
	/**
	 * Removes a position listener from the Observable's {@link IRoomsListener} list
	 * @param listener The {@link IRoomsListener} to remove
	 */
	public void removeRoomsListener(IRoomsListener listener);
	
	/**
	 * Gets the list of the rooms
	 * @return The list of the rooms
	 */
	public HashMap<String,Room> getRooms();
}
