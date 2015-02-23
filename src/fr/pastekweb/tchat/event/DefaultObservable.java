package fr.pastekweb.tchat.event;

import java.util.HashSet;

/**
 * The DefaultObservable class implementation.
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public abstract class DefaultObservable implements IMessageObservable
{
	/**
	 * The HashSet of the message listeners
	 */
	private HashSet<IMessageListener> messageListeners;

	/**
	 * Initialize the list of listeners
	 */
	public DefaultObservable()
	{
		messageListeners = new HashSet<>();
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

	/**
	 * Notify listener that there's a new message
	 */
	public void notifyHasNewMessage(String from, String message)
	{
		for (IMessageListener listener : messageListeners) {
			listener.hasNewMessage(from, message);
		}
	}
}
