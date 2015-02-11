package fr.pastekweb.tchat.event;

/**
 * IListener interface used handle the model change
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IListener
{	
	/**
	 * Notify that listener that the model has a new private message
	 * @param model The {@link IObservable} model
	 */
	public void hasNewMessage(String from, String message);
	
	/**
	 * Notify that listener that the clients positions have changed 
	 * @param model The {@link IObservable} model
	 */
	public void positionsChanged(IObservable model);
}
