package fr.pastekweb.tchat.event;

/**
 * Interface to implements the Observable pattern on the positions' state
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IPositionsObservable
{
	/**
	 * Adds a position listener to the Observable Object
	 * @param listener The {@link IPositionsListener} to add
	 */
	public void addPositionListener(IPositionsListener listener);
	
	/**
	 * Removes a position listener from the Observable's {@link IPositionsListener} list
	 * @param listener The {@link IPositionsListener} to remove
	 */
	public void removePositionListener(IPositionsListener listener);
}
