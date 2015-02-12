package fr.pastekweb.tchat.model;

/**
 * Class to represent a tchat user
 * 
 * @author Thomas TRIBOULT
 *
 */
public class User {
	/**
	 * The pseudo chosen by the user
	 */
	private String pseudo;
	
	/**
	 * Initialize the user with its pseudo
	 * @param name The pseudo chosen by the user
	 */
	public User(String name) {
		pseudo = name;
	}
	
	/**
	 * Initialize a User without pseudo
	 */
	public User() {
		this("");
	}
	
	/**
	 * Define the user's pseudo
	 * @param name The new pseudo
	 */
	public void setPseudo(String name) {
		pseudo = name;
	}
	
	/**
	 * Get the user's pseudo
	 * @return The user's pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}
	
	@Override
	public String toString() {
		return pseudo;
	}
}
