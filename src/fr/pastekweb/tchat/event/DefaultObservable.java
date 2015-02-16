package fr.pastekweb.tchat.event;

import java.util.HashSet;

/**
 * The DefaultObservable class implementation.
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public abstract class DefaultObservable implements IMessageObservable, IPositionsObservable
{
	/**
	 * The HashSet of the message listeners
	 */
	private HashSet<IMessageListener> messageListeners;
	/**
	 * The HashSet of the position listeners
	 */
	private HashSet<IPositionsListener> positionsListeners;

	/**
	 * Initialize the list of listeners
	 */
	public DefaultObservable()
	{
		messageListeners = new HashSet<>();
		positionsListeners = new HashSet<>();
	}

	@Override
	public void addMessageListener(IMessageListener listener)
	{
		messageListeners.add(listener);
	}

	@Override
	public void removeMessageListener(IMessageListener listener)
	{
		messageListeners.remove(listener);
	}
	
	@Override
	public void addPositionListener(IPositionsListener listener)
	{
		positionsListeners.add(listener);
	}
	
	@Override
	public void removePositionListener(IPositionsListener listener)
	{
		positionsListeners.remove(listener);
	}
	
	/**
	 * Notify listener that there's a new message
	 */
	public void notifyHasNewMessage(String from, String message)
	{
		for (IMessageListener listener : messageListeners) {
			listener.hasNewMessage(from, message);
		}
	}
	
	/**
	 * Notify listeners that users' positions have changed
	 */
	public void notifyPositionsChanged()
	{
		for (IPositionsListener listener : positionsListeners) {
			listener.positionsChanged(this);
		}
	}
}
