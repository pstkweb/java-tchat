package fr.pastekweb.tchat.tests;

import java.math.BigInteger;
import java.security.SecureRandom;

import fr.pastekweb.tchat.client.DefaultClient;
import fr.pastekweb.tchat.client.IClient;
import fr.pastekweb.tchat.server.Server;

/**
 * Test the communications between the server and clients
 * by client instantiation
 * 
 * @author Thomas TRIBOULT
 */
public class Test {
	/**
	 * Launch the tests on client side
	 * 
	 * @param args The first argument must be the client pseudo 
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage : java Test <nomUser>");
			return;
		}
		
		IClient cl = new DefaultClient();
		new Thread(cl).start();
		
		// Connexion au serveur
		String pseudo = args[0];
		while (!cl.connect(pseudo)) {
			pseudo = new BigInteger(130, new SecureRandom()).toString(32);
		}
		
		// Récupération de la liste des utilisateurs
		cl.askClientsList(Server.ROOM_PUBLIC_KEY);
		
		// Envoi d'un MSG
		String msg = "Nouveau message";
		cl.sendMessage(msg);
	}
}
