package fr.pastekweb.tchat.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import fr.pastekweb.tchat.event.IPositionsListener;
import fr.pastekweb.tchat.event.IPositionsObservable;
import fr.pastekweb.tchat.model.User;

/**
 * Display the current user with it's field of listen
 * 
 * @author Thomas TRIBOULT
 *
 */
public class CurrentUserView extends UserView implements MouseMotionListener, MouseListener, IPositionsObservable {
	private static final long serialVersionUID = 7834915844961113033L;
	/**
	 * The diameter of the field of listen
	 */
	private static final int FIELD_OF_LISTEN = 350;
	/**
	 * The width of the field of listen stroke
	 */
	private static final int FIELD_STROKE_WIDTH = 2;
	/**
	 * The user dot color
	 */
	protected static final Color USER_DOT_COLOR = Color.CYAN;
	
	/**
	 * The user view state
	 */
	private boolean isDragged;
	/**
	 * The Location of the user before dragging
	 */
	private Point previousLocation;
    /**
     * List of positions listeners
     */
    private ArrayList<IPositionsListener> listeners;
	
	public CurrentUserView(User u) {
		super(u);

        listeners = new ArrayList<>();
		
		setSize(
			FIELD_OF_LISTEN + FIELD_STROKE_WIDTH, 
			FIELD_OF_LISTEN + FIELD_STROKE_WIDTH
		);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		isDragged = false;
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

	@Override
	public void mouseDragged(MouseEvent e) {
		if (isDragged) {
			int dx = (int) (e.getX() - previousLocation.getX());
			int dy = (int) (e.getY() - previousLocation.getY());
			
			Point pos = getLocation();
			pos.translate(dx, dy);
			
			setLocation(pos);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		// User dot rectangle
		Rectangle rect = new Rectangle(
			(FIELD_OF_LISTEN - USER_DOT_SIZE) / 2,
			(FIELD_OF_LISTEN - USER_DOT_SIZE) / 2,
			USER_DOT_SIZE, 
			USER_DOT_SIZE
		);
		
		if (rect.contains(e.getPoint())) {
			isDragged = true;
			previousLocation = e.getPoint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isDragged = false;

        // Notify all listeners that the current user position has changed
        for (IPositionsListener listener : listeners) {
            listener.positionChanged(e.getPoint());
        }
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

    @Override
    public void addPositionListener(IPositionsListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removePositionListener(IPositionsListener listener) {
        listeners.remove(listener);
    }
}
