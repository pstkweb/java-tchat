package fr.pastekweb.tchat.ui;

import fr.pastekweb.tchat.model.Position;
import fr.pastekweb.tchat.model.User;

import java.awt.*;

/**
 * Display the current user with it's field of listen
 * 
 * @author Thomas TRIBOULT
 */
public class CurrentUserView extends UserView {
	private static final long serialVersionUID = 7834915844961113033L;
	/**
	 * The diameter of the field of listen
	 */
	public static final int FIELD_OF_LISTEN = 350;
	/**
	 * The width of the field of listen stroke
	 */
	private static final int FIELD_STROKE_WIDTH = 2;
	/**
	 * The user dot color
	 */
	protected static final Color USER_DOT_COLOR = Color.CYAN;

    /**
     * Instantiate a user view with field of listen
     * @param user The user model
     */
	public CurrentUserView(User user) {
		super(user);
		
		setSize(
			FIELD_OF_LISTEN + FIELD_STROKE_WIDTH, 
			FIELD_OF_LISTEN + FIELD_STROKE_WIDTH
		);
	}

    /**
     * Get the position of the user
     * @return It's position in the room
     */
    public Position getPosition(){
        return new Position(
            (int) (getLocation().getX() + (FIELD_OF_LISTEN / 2)),
            (int) (getLocation().getY() + (FIELD_OF_LISTEN / 2))
        );
    }
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Draw user dot
		g2d.setColor(USER_DOT_COLOR);
		g2d.fillOval(
			(FIELD_OF_LISTEN - USER_DOT_SIZE) / 2,
			(FIELD_OF_LISTEN - USER_DOT_SIZE) / 2,
			USER_DOT_SIZE, 
			USER_DOT_SIZE
		);
		
		// Draw user field of ear
		g2d.setColor(Color.BLUE);
		g2d.setStroke(
			new BasicStroke(
				FIELD_STROKE_WIDTH, 
				BasicStroke.CAP_BUTT, 
				BasicStroke.JOIN_BEVEL, 
				0, 
				new float[]{9}, 
				0
			)
		);
		g2d.drawOval(0, 0, FIELD_OF_LISTEN, FIELD_OF_LISTEN);
		
		// Draw user name
		Font f = new Font("TimesRoman", Font.BOLD, 14);
		FontMetrics metrics = g2d.getFontMetrics(f);
		int nameStrWidth = metrics.stringWidth(model.getPseudo());
		
		g2d.setFont(f);
		g2d.setColor(Color.black);
		g2d.drawString(
			model.getPseudo(),
			FIELD_OF_LISTEN / 2 - nameStrWidth / 2,
			FIELD_OF_LISTEN / 2 + USER_DOT_SIZE + NAME_MARGIN_TOP
		);
	}
}
