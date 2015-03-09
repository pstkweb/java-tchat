package fr.pastekweb.tchat.ui;

import fr.pastekweb.tchat.controller.TchatController;
import fr.pastekweb.tchat.model.Position;
import fr.pastekweb.tchat.model.PositionsList;
import fr.pastekweb.tchat.model.Room;
import fr.pastekweb.tchat.model.User;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

/**
 * Display a map of room connected users
 *
 * @author Thomas TRIBOULT <thomas.triboult@free.fr>
 */
public class MapView extends JPanel implements ListDataListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = -1459439574516713078L;

    /**
     * The room where users are
     */
    private Room room;
    /**
     * The connected user
     */
    private User currentUser;
    /**
     * The list of all room users positions
     */
	private PositionsList usersPosition;
    /**
     * The window controller
     */
    private TchatController controller;
    /**
     * The user view state
     */
    private boolean isDragged;
    /**
     * The user view
     */
    private CurrentUserView view;
    /**
     * The Location of the user before dragging
     */
    private Point previousLocation;

    /**
     * Instantiate a view for a room and a controller
     * @param ctrl The controller used to manage the main view
     * @param room The room in which it's displayed
     */
	public MapView(TchatController ctrl, Room room) {
        controller = ctrl;
        currentUser = controller.getClient().getTchat().getUser();
        this.room = room;
        isDragged = false;

        addMouseListener(this);
        addMouseMotionListener(this);

		setLayout(null);
        setBorder(UIManager.getBorder("TextField.border"));
	}

    /**
     * Set the positions list used as a model.
     * @param list A list of users
     */
	public void setModel(PositionsList list) {
        usersPosition = list;
        usersPosition.addListDataListener(this);
		
		refreshDisplay();
	}

    /**
     * Refresh this view components
     */
	private void refreshDisplay() {
		removeAll();

        // TODO : remove logs
        System.out.println(currentUser + " " + usersPosition.getHashMap());
        for (Map.Entry<User, Position> userPosition : usersPosition.getHashMap().entrySet()) {
            if (userPosition.getKey().getPseudo().equals(currentUser.getPseudo())) {
                view = new CurrentUserView(userPosition.getKey());

                view.setLocation(
                    (int) userPosition.getValue().getX() - CurrentUserView.FIELD_OF_LISTEN / 2,
                    (int) userPosition.getValue().getY() - CurrentUserView.FIELD_OF_LISTEN / 2
                );
                add(view);
            } else {
                UserView v = new UserView(userPosition.getKey());

                v.setLocation(
                    (int) userPosition.getValue().getX() - UserView.USER_DOT_SIZE / 2,
                    (int) userPosition.getValue().getY() - UserView.USER_DOT_SIZE / 2
                );
                add(v);
            }
        }

        repaint();
	}

	@Override
	public void intervalAdded(ListDataEvent e) {
        refreshDisplay();
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
		refreshDisplay();
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
        refreshDisplay();
	}

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDragged) {
            int dx = (int) (e.getX() - previousLocation.getX());
            int dy = (int) (e.getY() - previousLocation.getY());

            Point pos = view.getLocation();
            pos.translate(dx, dy);

            view.setLocation(pos);
            previousLocation = e.getPoint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // User dot rectangle
        Rectangle rect = new Rectangle(
                view.getX() + (CurrentUserView.FIELD_OF_LISTEN - CurrentUserView.USER_DOT_SIZE) / 2,
                view.getY() + (CurrentUserView.FIELD_OF_LISTEN - CurrentUserView.USER_DOT_SIZE) / 2,
                CurrentUserView.USER_DOT_SIZE,
                CurrentUserView.USER_DOT_SIZE
        );

        if (rect.contains(e.getPoint())) {
            isDragged = true;
            previousLocation = e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDragged) {
            isDragged = false;

            controller.getClient().sendPosition(new Position(view.getPosition()), room.getId());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}
}
