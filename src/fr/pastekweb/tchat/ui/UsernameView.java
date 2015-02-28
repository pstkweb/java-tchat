package fr.pastekweb.tchat.ui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.pastekweb.tchat.controller.TchatController;

/**
 * A view to ask the user name
 */
public class UsernameView extends JPanel
{
	private static final long serialVersionUID = -132155128090940934L;

	/**
	 * The username field
	 */
	private JTextField usernameField;
	/**
	 * The submit button
	 */
	private JButton submitButton;
	/**
	 * 
	 */
	private JLabel errorMessage;
	
	/**
	 * Initialize the view
	 */
	public UsernameView(TchatController controller)
	{
		super();
		usernameField = new JTextField();
		submitButton = new JButton("Connexion");
		submitButton.addActionListener(controller);
		errorMessage = new JLabel();
		
		createView();
	}
	
	/**
	 * Creates the view containing all graphical components
	 */
	private void createView()
	{
		this.add(new JLabel("Votre pseudo:"));
		this.add(usernameField);
		usernameField.setPreferredSize(new Dimension(200, 30));
		this.add(submitButton);
		this.add(errorMessage);
	}
	
	/**
	 * Gets the username field
	 * @return The field to fill the username
	 */
	public JTextField getUsernameField()
	{
		return usernameField;
	}
	
	/**
	 * Gets the submit button
	 * @return The button to connect to the tchat
	 */
	public JButton getSubmitButton()
	{
		return submitButton;
	}
	
	/**
	 * Sets an error message
	 * @param message The message to display
	 */
	public void setErrorMessage(String message)
	{
		errorMessage.setText(message);
	}
}
