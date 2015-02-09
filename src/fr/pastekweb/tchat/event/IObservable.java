package fr.pastekweb.tchat.event;

/**
 * Observable interface used to implements the Observalbe pattern
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IObservable
{
	/**
	 * Adds a listener to the Observable Object
	 * @param listener The {@link IListener} to add 
	 */
	public void addListener(IListener listener);
	
	/**
	 * Removes a listener from the Observable 's {@link IListener} list 
	 * @param listener The {@link IListener} to remove
	 */
	public void removeListener(IListener listener);
}
