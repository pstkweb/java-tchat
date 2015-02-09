package fr.pastekweb.tchat.event;

/**
 * IListener interface used handle the model change
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IListener
{
	/**
	 * Notify the listener that the user list has changed
	 * @param model The {@link IObservable} model
	 */
	public void userListChanged(IObservable model);
	
	/**
	 * Notify the listener that the model has a new private message
	 * @param model The {@link IObservable} model
	 */
	public void hasNewPrivateMessage(IObservable model);
	
	/**
	 * Notify the listener that the model has a new public message
	 * @param model The {@link IObservable} model
	 */
	public void hasNewPublicMessage(IObservable model);
}
