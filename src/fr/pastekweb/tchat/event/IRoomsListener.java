package fr.pastekweb.tchat.event;

public interface IRoomsListener
{
	/**
	 * Notify the listener that the room list has changed
	 * @param model The observable model
	 */
	public void roomsListChanged(IRoomsObservable model);
}
