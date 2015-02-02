package fr.pastekweb.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class Server {
	private final int PORT = 1337;
	private ServerSocket socket;
	private HashMap<String, ClientHandler> clients;
	
	public Server() {
		clients = new HashMap<String, ClientHandler>();
		
		try {
			socket = new ServerSocket(PORT);
			
			while (true) {
				Socket client = socket.accept();
				
				new ClientHandler(client, this);
			}
		} catch (IOException e) {
			System.out.println("Problème d'E/S : " + e.getMessage());
		}
	}
	
	public boolean isUsed(String pseudo) {
		return clients.containsKey(pseudo);
	}
	
	public Set<String> getUsernames() {
		return clients.keySet();
	}
	
	public HashMap<String, ClientHandler> getClients() {
		return clients;
	}
	
	public ClientHandler getClient(String pseudo) {
		return clients.get(pseudo);
	}
	
	public void addClient(String pseudo, ClientHandler client) {
		clients.put(pseudo, client);

		// Notify all clients of a new user
		Iterator<Entry<String, ClientHandler>> it = clients.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ClientHandler> clientTo = (Entry<String, ClientHandler>) it.next();
			clientTo.getValue().majUsersList();
		}
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
