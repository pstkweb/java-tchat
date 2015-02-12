package fr.pastekweb.tchat.ui;

import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import fr.pastekweb.tchat.model.Position;
import fr.pastekweb.tchat.model.User;

/**
 * 
 * @author Thomas TRIBOULT <thomas.triboult@free.fr>
 *
 */
public class MapView extends JPanel implements ListDataListener {
	private static final long serialVersionUID = -1459439574516713078L;
	
	private ListModel<String> users;
	
	public MapView() {
		setLayout(null);
	}
	
	public void setModel(ListModel<String> list) {
		users = list;
		users.addListDataListener(this);
		
		refreshDisplay();
	}
	
	private void refreshDisplay() {
		removeAll();
		
		for (int i=0; i<users.getSize(); i++) {
			if (i == 0) {
				CurrentUserView v = new CurrentUserView(
					new User(users.getElementAt(i))
				);
				v.setLocation(Position.getRand());
				add(v);
			} else {
				UserView v = new UserView(
					new User(users.getElementAt(i))
				);
				v.setLocation(Position.getRand());
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
}
