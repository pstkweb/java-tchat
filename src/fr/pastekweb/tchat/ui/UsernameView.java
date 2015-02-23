package fr.pastekweb.tchat.ui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.pastekweb.tchat.controller.TchatController;

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
	 * @return
	 */
	public JTextField getUsernameField()
	{
		return usernameField;
	}
	
	/**
	 * Gets the submit button
	 * @return
	 */
	public JButton getSubmitButton()
	{
		return submitButton;
	}
	
	/**
	 * Sets an error message
	 * @param message
	 */
	public void setErrorMessage(String message)
	{
		errorMessage.setText(message);
	}
}
