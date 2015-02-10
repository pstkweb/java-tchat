package fr.pastekweb.tchat.model;

import java.awt.Point;

/**
 * The client position in a Cartesian plan
 * @author Thomas TRIBOULT
 *
 */
public class Position extends Point {
	/**
	 * 
	 */
	private static final long serialVersionUID = 818877188439997924L;

	/**
	 * Instantiate a new Position with its coordinate
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public Position(int x, int y) {
		super(x, y);
	}
	
	/**
	 * A String representation of this coordinate
	 * 
	 * @return A String representing the x,y coordinates
	 */
	public String toString() {
		return x + ":" + y;
	}
}
