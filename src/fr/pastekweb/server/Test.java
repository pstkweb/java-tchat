package fr.pastekweb.server;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

import fr.pastekweb.client.CientStub;

public class Test {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage : java Test <nomUser>");
			return;
		}
		
		CientStub cl = new CientStub();
		
		String pseudo = args[0];
		while (!cl.connect(pseudo)) {
			pseudo = new BigInteger(130, new SecureRandom()).toString(32);
		}
		System.out.println("Logged in as : " + pseudo);
		
		ArrayList<String> users = (ArrayList<String>) cl.usersList();
		System.out.println("Liste users " + users.size() + " :");
		for (String u : users) {
			System.out.println("- " + u);
		}
		
		while (true) {
			cl.waitForMessage();
		}
	}
}
