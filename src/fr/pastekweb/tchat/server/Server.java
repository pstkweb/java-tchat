package fr.pastekweb.tchat.server;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.pastekweb.tchat.model.Position;

/**
 * Server main class
 * 
 * @author Thomas TRIBOULT
 */
public class Server 
{
	public static final String ROOM_PUBLIC_KEY = "##PUBLIC##";
	
	/**
	 * Port used by the client/server
	 */
	private final int PORT = 1337;
	/**
	 * The socket representing the server
	 */
	private ServerSocket socket;
	/**
	 * The HashMap of differents opened rooms
	 */
	private HashMap<String, HashMap<String, ClientHandler> > rooms;
	
	/**
	 * Create an instance of the server with 
	 * a default port (1337)
	 */
	public Server() 
	{
		rooms = new HashMap<>();
		rooms.put(ROOM_PUBLIC_KEY, new HashMap<>());
		
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
	
	/**
	 * Check if a pseudo is already used by a connected user
	 * @param pseudo The pseudo to check
	 * @return True if the pseudo is used
	 */
	public boolean isUsed(String pseudo) {
		return rooms.get(ROOM_PUBLIC_KEY).containsKey(pseudo);
	}
	
	/**
	 * Return the list of clients pseudo
	 * @param roomID The room 's id of the requested list
	 * @return The list of connected users pseudo
	 */
	public Set<String> getUsernames(String roomID) {
		return rooms.get(roomID).keySet();
	}
	
	/**
	 * Return the list of clients position
	 * @return The list of connected users position
	 */
	public HashMap<String, Position> getPositions() {
		HashMap<String, Position> positions = new HashMap<>();

		Iterator<Entry<String, ClientHandler>> it = rooms.get(ROOM_PUBLIC_KEY).entrySet().iterator();
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
		return rooms.get(ROOM_PUBLIC_KEY);
	}
	
	/**
	 * Gets the list of the rooms
	 * @return The list of the rooms
	 */
	public HashMap<String, HashMap<String, ClientHandler>> getRooms()
	{
		return rooms;
	}
	
	/**
	 * Gets the hash map of the given room's id
	 * @param roomID The room's id
	 * @return The HashMap of the given room's id
	 */
	public HashMap<String, ClientHandler> getRoom(String roomID)
	{
		return rooms.get(roomID);
	}
	
	/**
	 * Get a client by its pseudo
	 * @param pseudo The pseudo to find
	 * @return The client thread using this pseudo
	 */
	public ClientHandler getClient(String pseudo) {
		return rooms.get(ROOM_PUBLIC_KEY).get(pseudo);
	}
	
	/**
	 * Add a client to the list of connected users (in the public room)
	 * @param pseudo The pseudo of the client
	 * @param client The client thread using the given pseudo
	 */
	public void addClient(String pseudo, ClientHandler client)
	{
		addClient(pseudo, client, ROOM_PUBLIC_KEY);
	}
	
	/**
	 * Add a client to the given room.
	 * Also check whether the client have been added to the public room yet.
	 * If not, adds the client to the public room before perform the add action.
	 * 
	 * @param pseudo The pseudo of the client
	 * @param client The client thread using the given pseudo
	 * @param roomID The room id
	 */
	public void addClient(String pseudo, ClientHandler client, String roomID) 
	{
		addClient(pseudo, client, roomID, true);
	}
	
	/**
	 * Add a client to the given room.
	 * Also check whether the client have been added to the public room yet.
	 * If not, adds the client to the public room before perform the add action.
	 * 
	 * @param pseudo The pseudo of the client
	 * @param client The client thread using the given pseudo
	 * @param roomID The room id
	 * @param notifyUsers Whether the users should be notify about this
	 */
	public void addClient(String pseudo, ClientHandler client, String roomID, boolean notifyUsers)
	{
		// If the client have not been added to the public room yet 
		if (!isUsed(pseudo) && roomID != ROOM_PUBLIC_KEY) {
			addClientToRoom(pseudo, client, ROOM_PUBLIC_KEY, notifyUsers);
		}
		
		if (!rooms.containsKey(roomID)) {
			openNewRoom(roomID);
		}
		addClientToRoom(pseudo, client, roomID, notifyUsers);
	}
	
	/**
	 * Add a client to given room.
	 * @param username The pseudo of the client
	 * @param client The client thread using the given pseudo
	 * @param roomID The room id
	 */
	private void addClientToRoom(String username, ClientHandler client, String roomID, boolean notifyUsers)
	{
		rooms.get(roomID).put(username, client);
		client.setPosition(new Position(0, 0));

		if (notifyUsers) {
			// Notify all clients of a new user, except the new one
			Iterator<Entry<String, ClientHandler>> it = rooms.get(roomID).entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, ClientHandler> clientTo = (Entry<String, ClientHandler>) it.next();
				
				if (!username.equals(clientTo.getKey())) {
					clientTo.getValue().sendNewUser(username, roomID);
					
					clientTo.getValue().sendPosition(rooms.get(roomID).get(username).getPosition(), roomID);
				}
			}
		}
	}
	
	/**
	 * Open a new room on the server
	 * @param roomID The room 's id
	 */
	private void openNewRoom(String roomID)
	{
		rooms.put(roomID, new HashMap<>());
	}
	
	/**
	 * Open a new room on the server by
	 * generating a new roomID
	 * @return the room id
	 */
	public String openNewRoom()
	{
		String roomID;
		do {
			roomID = new BigInteger(130, new SecureRandom()).toString(32);
		} while (rooms.containsKey(roomID));
		openNewRoom(roomID);
		return roomID;
	}
	
	/**
	 * Notitfies all user in a room that this room have been opened
	 * @param roomID The room's Id
	 */
	public void notifyRoomOpened(String roomID)
	{
		HashMap<String, ClientHandler> room = getRoom(roomID);
		
		for (Entry<String, ClientHandler> client : room.entrySet()) {
			client.getValue().sendNewRoomOpened(roomID);
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
