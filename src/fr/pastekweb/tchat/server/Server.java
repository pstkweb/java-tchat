package fr.pastekweb.tchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Server main class
 * 
 * @author Thomas TRIBOULT
 */
public class Server {
	/**
	 * Port used by the client/server
	 */
	private final int PORT = 1337;
	/**
	 * The socket representing the server
	 */
	private ServerSocket socket;
	/**
	 * Connected clients list
	 */
	private HashMap<String, ClientHandler> clients;
	
	/**
	 * Create an instance of the server with 
	 * a default port (1337)
	 */
	public Server() {
		clients = new HashMap<String, ClientHandler>();
		
		try {
			socket = new ServerSocket(PORT);
			
			while (true) {
				Socket client = socket.accept();
				System.out.println("Nb users : " + clients.size());
				new ClientHandler(client, this);
			}
		} catch (IOException e) {
			System.out.println("Problème d'E/S : " + e.getMessage());
		}
	}
	
	/**
	 * Check if a pseudo is already used by a connected user
	 * @param pseudo The pseudo to check
	 * @return True if the pseudo is used
	 */
	public boolean isUsed(String pseudo) {
		return clients.containsKey(pseudo);
	}
	
	/**
	 * Return the list of clients pseudo
	 * @return The list of connected users pseudo
	 */
	public Set<String> getUsernames() {
		return clients.keySet();
	}
	
	/**
	 * Return the list of clients position
	 * @return The list of connected users position
	 */
	public HashMap<String, Position> getPositions() {
		HashMap<String, Position> positions = new HashMap<>();

		Iterator<Entry<String, ClientHandler>> it = clients.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ClientHandler> clientTo = (Entry<String, ClientHandler>) it.next();
			
			positions.put(clientTo.getKey(), clientTo.getValue().getPosition());
		}
		
		return positions;
	}
	
	/**
	 * Return the list of clients
	 * @return The list of connected users
	 */
	public HashMap<String, ClientHandler> getClients() {
		return clients;
	}
	
	/**
	 * Get a client by its pseudo
	 * @param pseudo The pseudo to find
	 * @return The client thread using this pseudo
	 */
	public ClientHandler getClient(String pseudo) {
		return clients.get(pseudo);
	}
	
	/**
	 * Add a client to the list of connected users
	 * @param pseudo The pseudo of the client
	 * @param client The client thread using the given pseudo
	 */
	public void addClient(String pseudo, ClientHandler client) {
		clients.put(pseudo, client);
		client.setPosition(new Position(0, 0));

		// Notify all clients of a new user, except the new one
		Iterator<Entry<String, ClientHandler>> it = clients.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ClientHandler> clientTo = (Entry<String, ClientHandler>) it.next();
			
			if (!pseudo.equals(clientTo.getKey())) {
				clientTo.getValue().sendNewUser(pseudo);
				
				clientTo.getValue().sendPosition(clients.get(pseudo).getPosition());
			}
		}
	}
	
	/**
	 * The main method to launch the server
	 * @param args Not used
	 */
	public static void main(String[] args) {
		new Server();
	}
}
