package fr.pastekweb.tchat.event;

import java.awt.*;

/**
 * A listener for the positions
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public interface IPositionsListener
{
	/**
	 * Notify that listener that the clients positions have changed 
	 * @param p The destination position
	 */
	public void positionChanged(Point p);
}
