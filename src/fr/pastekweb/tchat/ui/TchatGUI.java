package fr.pastekweb.tchat.ui;

import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.controller.TchatController;

import javax.swing.*;
import java.awt.*;

/**
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 *
 */
public class TchatGUI
{
	/**
	 * The GUI view
	 */
	private JPanel view;
	/**
	 * The hashMap of room views mapped by rooms' id
	 */
	private TchatView tchatView;
	/**
	 * The view to ask the user's name
	 */
	private UsernameView usernameView;

    /**
	 * Initialize the GUI
	 */
	public TchatGUI(IClient client)
	{
		view = new JPanel();
        TchatController controller = new TchatController(client, this);
		tchatView = new TchatView(client, controller);
		client.getTchat().addRoomsListener(tchatView);
		usernameView = new UsernameView(controller);
		
		if (client.isConnected()) {
			createView();
		} else {
			createUsernameView();
		}
		
	}
		
	/**
	 * Creates the view containing all graphical components
	 */
	public void createView()
	{
		clearView();
		view.setLayout(new BorderLayout());
		view.add(tchatView, BorderLayout.CENTER);
	}
	
	/**
	 * Creates the view that asks the user's name
	 */
	public void createUsernameView()
	{
		clearView();
		view.setLayout(new BorderLayout());
		view.add(usernameView, BorderLayout.CENTER);
	}
	
	private void clearView()
	{
		view.removeAll();
		view.revalidate();
		view.repaint();
	}
	
	/**
	 * Gets the GUI view
	 * @return The GUI
	 */
	public JPanel getView()
	{
		return view;
	}
	
	/**
	 * Gets the GUI view
	 * @return The view
	 */
	public TchatView getTchatView()
	{
		return tchatView;
	}
	
	/**
	 * Gets the username view
	 * @return The username view
	 */
	public UsernameView getUsernameView()
	{
		return usernameView;
	}
}
