package fr.pastekweb.tchat.event;

/**
 * A listener for the rooms
 *
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IRoomsListener
{
	/**
	 * Notify the listener that the room list has changed
	 * @param model The observable model
	 */
	public void roomsListChanged(IRoomsObservable model);
}
