package fr.pastekweb.tchat.event;

/**
 * A listener for the positions
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IPositionsListener
{
	/**
	 * Notify that listener that the clients positions have changed 
	 * @param model The {@link IObservable} model
	 */
	public void positionsChanged(IPositionsObservable model);
}
