package fr.pastekweb.tchat.ui;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import fr.pastekweb.tchat.controller.TchatController;
import fr.pastekweb.tchat.event.IPositionsListener;
import fr.pastekweb.tchat.model.Position;
import fr.pastekweb.tchat.model.PositionsList;
import fr.pastekweb.tchat.model.Room;
import fr.pastekweb.tchat.model.User;

import java.awt.*;
import java.util.Map;

/**
 * Display a map of room connected users
 *
 * @author Thomas TRIBOULT <thomas.triboult@free.fr>
 */
public class MapView extends JPanel implements ListDataListener, IPositionsListener {
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
     * Instantiate a view for a room and a controller
     * @param ctrl The controller used to manage the main view
     * @param room The room in which it's displayed
     */
	public MapView(TchatController ctrl, Room room) {
        controller = ctrl;
        currentUser = controller.getClient().getTchat().getUser();
        this.room = room;

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

        System.out.println(usersPosition.getHashMap());
        for (Map.Entry<User, Position> userPosition : usersPosition.getHashMap().entrySet()) {
            if (userPosition.getKey().getPseudo().equals(currentUser.getPseudo())) {
                CurrentUserView v = new CurrentUserView(userPosition.getKey());
                v.addPositionListener(this);

                v.setLocation(userPosition.getValue());
                add(v);
            } else {
                UserView v = new UserView(userPosition.getKey());

                v.setLocation(userPosition.getValue());
                add(v);
            }
        }
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
    public void positionChanged(Point point) {
        System.out.println("Position changed");
        controller.getClient().sendPosition(new Position(point), room.getId());
    }
}
