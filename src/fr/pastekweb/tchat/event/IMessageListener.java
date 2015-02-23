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
     * @param from The pseudo of the user who send the message
     * @param message The message
	 */
	public void hasNewMessage(String from, String message);
}
