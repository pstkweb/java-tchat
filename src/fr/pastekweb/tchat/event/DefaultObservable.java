package fr.pastekweb.tchat.event;

import java.util.HashSet;

/**
 * The DefaultObservable class implementation.
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 * 
 */
public abstract class DefaultObservable implements IObservable
{
	/**
	 * The HashSet of the Observable listeners
	 */
	private HashSet<IListener> listeners;

	/**
	 * Initialize the list of listeners
	 */
	public DefaultObservable()
	{
		listeners = new HashSet<>();
	}

	@Override
	public void addListener(IListener listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeListener(IListener listener)
	{
		listeners.remove(listener);
	}
	
	/**
	 * Notify listener that there's a new message
	 */
	public void notifyHasNewMessage()
	{
		for (IListener listener : listeners) {
			listener.hasNewMessage(this);
		}
	}
	
	/**
	 * Notify listeners that users' positions have changed
	 */
	public void notifyPositionsChanged()
	{
		for (IListener listener : listeners) {
			listener.positionsChanged(this);
		}
	}
}
