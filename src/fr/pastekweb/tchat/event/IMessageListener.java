package fr.pastekweb.tchat.event;

/**
 * A listener for the messages
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IMessageListener
{
	/**
	 * Notify that listener that the model has a new private message
	 * @param model The {@link IObservable} model
	 */
	public void hasNewMessage(String from, String message);
}
