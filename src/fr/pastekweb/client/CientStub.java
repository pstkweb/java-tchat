package fr.pastekweb.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

import fr.pastekweb.server.Protocol;

public class CientStub {
	private final int PORT = 1337;
	private Socket socket;
	private PrintWriter write;
	private BufferedReader read;
	
	public CientStub() {
		try {
			socket = new Socket("127.0.0.1", PORT);
			
			write = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Erreur d'E/S : " + e.getMessage());
		}
	}
	
	public boolean connect(String pseudo) {
		write.println(Protocol.CONNECT);
		write.println(pseudo);
		write.flush();
		
		try {
			System.out.println("Waiting for server response");
			if (Protocol.CONNECT_OK.toString().equals(read.readLine())) {
				return true;
			}
		} catch (IOException e) {
			System.out.println("Erreur de lecture du flux.");
		}
		
		return false;
	}
	
	public Collection<String> usersList() {
		write.println(Protocol.USERS_LIST);
		write.flush();

		ArrayList<String> users = new ArrayList<String>();
		String token;
		try {
			System.out.println("Waiting for server response");
			token = read.readLine();
			String user;
			while (!(user = read.readLine()).equals(token)) {
				users.add(user);
			}
		} catch (IOException e) {
			System.out.println("Problème d'E/S : " + e.getMessage());
		}
		
		return users;
	}
	
	public Collection<String> majUsersList() {
		ArrayList<String> users = new ArrayList<String>();
		
		
		return users;
	}
	
	public void sendMessage(String msg) {
		write.println(Protocol.SEND_MSG);
		write.flush();
	}
	
	public void waitForMessage() {
		try {
			String msg = read.readLine();
			System.out.println(msg);
			
			switch (Protocol.createProtocol(msg)) {
				case USERS_LIST:
					ArrayList<String> users = new ArrayList<String>();
					String token = read.readLine();

					String user;
					while (!(user = read.readLine()).equals(token)) {
						users.add(user);
					}
					
					System.out.println("Liste users " + users.size() + " :");
					for (String u : users) {
						System.out.println("- " + u);
					}
					break;
				default:
					System.out.println("Receive : " + msg);
			}
		} catch (IOException e) {
			System.out.println("Problème d'E/S : " + e.getMessage());
		}
	}
}
