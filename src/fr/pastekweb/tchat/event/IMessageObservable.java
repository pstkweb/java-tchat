package fr.pastekweb.tchat.event;

/**
 * Interface to implements the Observable pattern on messages' state
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IMessageObservable
{
	/**
	 * Adds a message listener to the Observable Object
	 * @param listener The {@link IMessageListener} to add 
	 */
	public void addMessageListener(IMessageListener listener);
	
	/**
	 * Removes a message listener from the Observable's {@link IMessageListener} list 
	 * @param listener The {@link IMessageListener} to remove
	 */
	public void removeMessageListener(IMessageListener listener);
}
