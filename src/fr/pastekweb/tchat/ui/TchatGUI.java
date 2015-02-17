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
	 * The controller
	 */
	private TchatController controller;
	

	/**
	 * Initialize the GUI
	 */
	public TchatGUI(IClient client)
	{
		view = new JPanel();
		controller = new TchatController(client);
		tchatView = new TchatView(client, controller);
		
		
		
		createView();
	}
		
	/**
	 * Creates the view containing all graphical components
	 */
	private void createView()
	{
		view.setLayout(new BorderLayout());
		view.add(tchatView, BorderLayout.CENTER);
	}
	
	/**
	 * Gets the GUI view
	 * @return The view
	 */
	public JPanel getView()
	{
		return view;
	}
}
