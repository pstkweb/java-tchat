package fr.pastekweb.tchat.ui;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.controller.TchatController;

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
	 * The controller
	 */
	private TchatController controller;

	/**
	 * Initialize the GUI
	 */
	public TchatGUI(IClient client)
	{
		view = new JPanel();
		controller = new TchatController(client, this);
		tchatView = new TchatView(client, controller);
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
	 * @return
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
