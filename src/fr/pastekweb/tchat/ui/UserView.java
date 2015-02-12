package fr.pastekweb.tchat.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import fr.pastekweb.tchat.model.User;

/**
 * Display a user by a colored dot and its name
 * 
 * @author Thomas TRIBOULT
 *
 */
public class UserView extends JPanel {
	private static final long serialVersionUID = 7834915844961113033L;
	/**
	 * The diameter of the dot
	 */
	protected static final int USER_DOT_SIZE = 15;
	/**
	 * The user dot color
	 */
	protected static final Color USER_DOT_COLOR = Color.BLACK;
	/**
	 * The margin between the name and the dot
	 */
	protected static final int NAME_MARGIN_TOP = 5;
	
	/**
	 * The User 
	 */
	protected User model;
	
	public UserView(User u) {
		model = u;
		
		setSize(USER_DOT_SIZE, USER_DOT_SIZE);
		setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Draw user name
		Font f = new Font("TimesRoman", Font.BOLD, 14);
		FontMetrics metrics = g2d.getFontMetrics(f);
		Rectangle2D strBounds = metrics.getStringBounds(model.getPseudo(), g2d);
		
		setSize(
			(int) strBounds.getWidth(), 
			(int) (USER_DOT_SIZE + NAME_MARGIN_TOP + strBounds.getHeight())
		);

		g2d.setFont(f);
		g2d.setColor(Color.BLACK);
		g2d.drawString(
			model.getPseudo(),
			0, 
			(int) (USER_DOT_SIZE / 2 + USER_DOT_SIZE + NAME_MARGIN_TOP)
		);
		
		// Draw user dot
		g2d.setColor(USER_DOT_COLOR);
		g2d.fillOval(
			(int) ((getSize().getWidth() - USER_DOT_SIZE) / 2), 
			0, 
			USER_DOT_SIZE, 
			USER_DOT_SIZE
		);
	}
}
