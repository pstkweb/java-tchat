package fr.pastekweb.tchat.ui;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import fr.pastekweb.tchat.controller.TchatController;
import fr.pastekweb.tchat.event.IPositionsListener;
import fr.pastekweb.tchat.event.IPositionsObservable;
import fr.pastekweb.tchat.model.Position;
import fr.pastekweb.tchat.model.PositionsList;
import fr.pastekweb.tchat.model.Room;
import fr.pastekweb.tchat.model.User;

import java.awt.*;
import java.util.Map;

/**
 * 
 * @author Thomas TRIBOULT <thomas.triboult@free.fr>
 *
 */
public class MapView extends JPanel implements ListDataListener, IPositionsListener {
	private static final long serialVersionUID = -1459439574516713078L;

    private Room room;
    private User currentUser;
	private PositionsList usersPosition;
    private TchatController controller;
	
	public MapView(TchatController ctrl, Room room) {
        controller = ctrl;
        currentUser = controller.getClient().getTchat().getUser();
        this.room = room;

		setLayout(null);

        setBorder(UIManager.getBorder("TextField.border"));
	}
	
	public void setModel(PositionsList list) {
        usersPosition = list;
        usersPosition.addListDataListener(this);
		
		refreshDisplay();
	}
	
	private void refreshDisplay() {
		removeAll();

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
