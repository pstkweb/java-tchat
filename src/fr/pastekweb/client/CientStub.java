package fr.pastekweb.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import fr.pastekweb.server.Protocol;

public class CientStub {
	private final int PORT = 1337;
	private Socket socket;
	private PrintWriter write;
	private BufferedReader read;
	private ArrayList<String> users;
	
	public CientStub() {
		users = new ArrayList<String>();
		
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
		write.flush();
		write.println(pseudo);
		write.flush();
		
		try {
			System.out.println("Waiting for server response on pseudo <" + pseudo + ">");
			String result = read.readLine();
			
			System.out.println("Response : ~" + result + "~");
			if (Protocol.OK.toString().equals(result)) {
				return true;
			}
		} catch (IOException e) {
			System.out.println("Erreur de lecture du flux.");
		}
		
		return false;
	}
	
	public void receiveUsersList() {
		write.println(Protocol.USERS_LIST);
		write.flush();

		String token;
		try {
			System.out.println("Waiting for server response");
			
			String result = read.readLine();
			if (result.equals(Protocol.USERS_LIST.toString())) {
				users.clear();
				
				token = read.readLine();
				String user;
				while (!(user = read.readLine()).equals(token)) {
					users.add(user);
				}
			} else {
				System.out.println("Colision de trames");
			}
		} catch (IOException e) {
			System.out.println("Problème d'E/S : " + e.getMessage());
		}
	}
	
	public void sendMessage(String msg) {
		write.println(Protocol.SEND_MSG);
		write.flush();
		
		write.println(msg);
		write.flush();
	}
	
	public ArrayList<String> getUsers() {
		return users;
	}
	
	public void waitForMessage() {
		try {
			String msg = read.readLine();
			System.out.println(msg);
			
			switch (Protocol.createProtocol(msg)) {
				case NEW_USER:
					String pseudo = read.readLine();
					
					users.add(pseudo);
					
					System.out.println("Liste users " + users.size() + " :");
					for (String u : users) {
						System.out.println("- " + u);
					}
					break;
				case RECEIVE_MSG:
					String emetteur = read.readLine();
					String writedMsg = read.readLine();
					
					System.out.println(emetteur + " a écrit : " + writedMsg);
					break;
				default:
					System.out.println("Receive : " + msg);
			}
		} catch (IOException e) {
			System.out.println("Problème d'E/S : " + e.getMessage());
		}
	}
}
