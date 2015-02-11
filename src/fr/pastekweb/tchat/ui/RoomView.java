package fr.pastekweb.tchat.ui;

import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JPanel;

import fr.pastekweb.tchat.event.IListener;
import fr.pastekweb.tchat.event.IObservable;
import fr.pastekweb.tchat.model.Room;
import fr.pastekweb.tchat.model.UserList;

/**
 * The {@link Room} view
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class RoomView extends JPanel implements IListener
{
	private static final long serialVersionUID = -4458295478133444427L;

	/**
	 * The {@link Room}
	 */
	private Room room;
	
	/**
	 * The user list
	 */
	private JList<String> userList;
	
	/**
	 * Initialize the {@link Room} view
	 * @param room The {@link Room}
	 */
	public RoomView(Room room)
	{
		super();
		this.room = room;
		
		userList = new JList<>();
		userList.setModel(room.getUsers());
		
		createView();
	}
	
	/**
	 * Creates the view containing all graphical components
	 */
	private void createView()
	{
		this.setLayout(new BorderLayout());
		this.add(userList, BorderLayout.WEST);
	}

	@Override
	public void hasNewMessage(IObservable model)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void positionsChanged(IObservable model)
	{
		// TODO Auto-generated method stub
		
	}
}
