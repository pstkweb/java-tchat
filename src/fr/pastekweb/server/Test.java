package fr.pastekweb.server;

import java.math.BigInteger;
import java.security.SecureRandom;

import fr.pastekweb.client.CientStub;

public class Test {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage : java Test <nomUser>");
			return;
		}
		
		CientStub cl = new CientStub();
		
		// Connexion au serveur
		String pseudo = args[0];
		while (!cl.connect(pseudo)) {
			pseudo = new BigInteger(130, new SecureRandom()).toString(32);
		}
		System.out.println("Logged in as : " + pseudo);
		
		// Récupération de la liste des utilisateurs
		cl.receiveUsersList();
		System.out.println("Liste users " + cl.getUsers().size() + " :");
		for (String u : cl.getUsers()) {
			System.out.println("- " + u);
		}
		
		// Envoi d'un MSG
		String msg = new BigInteger(130, new SecureRandom()).toString(32);
		cl.sendMessage(msg);
		
		// Wait for server messages
		while (true) {
			cl.waitForMessage();
		}
	}
}
