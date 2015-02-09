package fr.pastekweb.tchat.event;

import java.util.HashSet;

/**
 * The DefaultObservable class implementation.
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
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
	 * Notify the listener that the user list have changed
	 */
	public void notifyUserListChanged()
	{
		for (IListener listener : listeners) {
			listener.userListChanged(this);
		}
	}
	
	/**
	 * Notify the listener that there's a new public message
	 */
	public void notifyHasNewPublicMessage()
	{
		for (IListener listener : listeners) {
			listener.hasNewPublicMessage(this);
		}
	}
	
	/**
	 * Notify the listener that there's a new private message
	 */
	public void notifyHasNewPrivateMessage()
	{
		for (IListener listener : listeners) {
			listener.hasNewPrivateMessage(this);
		}
	}
}
