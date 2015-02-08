package fr.pastekweb.tchat.tests;

import java.math.BigInteger;
import java.security.SecureRandom;

import fr.pastekweb.tchat.client.DefaultClient;
import fr.pastekweb.tchat.client.IClient;

public class Test {
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
		cl.askClientsList();
		
		// Envoi d'un MSG
		String msg = new BigInteger(130, new SecureRandom()).toString(32);
		cl.sendPublicMessage(msg);
	}
}
