package fr.pastekweb.tchat.event;

import java.util.ArrayList;

public abstract class DefaultRoomsObservable implements IRoomsObservable
{
	private ArrayList<IRoomsListener> roomsListener;

	/**
	 * Initialize the room observable with an empty list of listener
	 */
	public DefaultRoomsObservable()
	{
		roomsListener = new ArrayList<>();
	}

	@Override
	public void addRoomsListener(IRoomsListener listener)
	{
		roomsListener.add(listener);	
	}

	@Override
	public void removeRoomsListener(IRoomsListener listener)
	{
		roomsListener.add(listener);
	}

	/**
	 * Notify the listeners that the rooms list has changed
	 */
	public void notifyRoomsListHasChanged() 
	{
		for (IRoomsListener listener : roomsListener) {
			listener.roomsListChanged(this);
		}
	}
}
