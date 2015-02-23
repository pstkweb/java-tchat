package fr.pastekweb.tchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import fr.pastekweb.tchat.model.Position;

/**
 * The client thread used by the server
 * 
 * @author Thomas TRIBOULT
 */
public class ClientHandler implements Runnable {
	/**
	 * The socket used by that client
	 */
	private Socket socket;
	/**
	 * A reference to the server main class
	 */
	private Server server;
	/**
	 * The pseudo of this user
	 */
	private String username;
	/**
	 * The position of this user
	 */
	private Position position;
	/**
	 * The socket input stream
	 */
	private BufferedReader in;
	/**
	 * The socket output stream
	 */
	private PrintWriter out;
	/**
	 * Whether connection is alive
	 */
	private boolean isAlive;
	
	/**
	 * Instantiate a new client thread
	 * @param s The socket used by that client
	 * @param serv The main server class reference
	 */
	public ClientHandler(Socket s, Server serv) {
		server = serv;
		socket = s;
		username = "";
		isAlive = true;
		
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

			new Thread(this).start();
		} catch (IOException e) {
			System.out.println("Problème d'E/S : " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (isAlive) {
			try {
				// TODO Removes logs
				String s = in.readLine();
				
				System.out.println("-------------------");
				System.out.println("s: "+s);
				switch (Protocol.createProtocol(s)) {
				
					case CONNECT:
						handleConnectionRequest();
						break;
					
					case NEW_ROOM:
						handleNewRoomRequest();
						break;
						
					case USERS_LIST:
						sendUsersList();
						break;
						
					case SEND_MSG:
						sendMessage();
						break;
						
					case POSITIONS_LIST:
						sendPositionsList();
						break;
						
					case SEND_POS:
						sendUserPosition();
						break;
						
					default:
						System.out.println("Message non supporté par le serveur.");
				}
			} catch (IOException e) {
				System.out.println("Erreur d'E/S : " + e.getMessage());
			} catch(NullPointerException e) {
				// Means that the client left the application
				notifyUserLeft(Server.ROOM_PUBLIC_KEY);
				isAlive = false;
			}
		}
	}
	
	/**
	 * Handles the user's connection request
	 * @throws IOException If an input/output error occurs
	 */
	private void handleConnectionRequest() throws IOException
	{
		String pseudo = in.readLine();
		if (!server.isUsed(pseudo)) {
			username = pseudo;
			send(Protocol.CONNECT_OK.toString());
			server.addClient(username, this);
		} else {
			send(Protocol.CONNECT_KO.toString());
		}
	}
	
	/**
	 * Handles the user's new room request
	 * @throws IOException If an input/output error occurs
	 */
	private void handleNewRoomRequest() throws IOException
	{
		// TODO Remove logs
		
		String roomID = server.openNewRoom();
		String token = in.readLine();
		String pseudo;
		ArrayList<String> usernames = new ArrayList<>();
		
		System.out.println("Room opened: "+roomID);
		System.out.println("Token: "+token);
		
		while (!(pseudo = in.readLine()).equals(token)) {
			usernames.add(pseudo);
			System.out.println("User: "+pseudo);
		}
		
		System.out.println("End Token: "+pseudo);
		
		for (String username : usernames) {
			ClientHandler ch = server.getRoom(Server.ROOM_PUBLIC_KEY).get(username);
			server.addClient(username, ch, roomID, false);
		}
		server.notifyRoomOpened(roomID);
	}
	
	/**
	 * Sends the users list of the asked room
	 * @throws IOException Whether an input/output error occurs
	 */
	private void sendUsersList() throws IOException
	{
		// Gets the room 's id of the requested list
		String roomID = in.readLine();
		sendUsersList(roomID);
	}
	
	/**
	 * Sends the users list of the asked room
	 * @param roomID
	 */
	private void sendUsersList(String roomID)
	{
		// TODO Removes logs
		
		String tokenUsers = new BigInteger(130, new SecureRandom()).toString(32);
		
		send(Protocol.USERS_LIST);
		send(roomID);
		send(tokenUsers);
		
		System.out.println("Protocol: "+Protocol.USERS_LIST);
		System.out.println("Room id: "+roomID);
		System.out.println("Token: "+tokenUsers);
		
		for (String user : server.getUsernames(roomID)) {
			send(user);
			System.out.println("User: "+user);
		}
		send(tokenUsers);
		System.out.println("End token: "+tokenUsers);
	}
	
	/**
	 * Notify others clients handler that the user left
	 */
	private void notifyUserLeft(String roomID)
	{
		HashMap<String, ClientHandler> clients = server.getRooms().get(roomID);
		
		// Notify others clients connected to this room that the user left
		Iterator<Entry<String, ClientHandler>> clientIt = clients.entrySet().iterator();
		while (clientIt.hasNext()) {
			Entry<String, ClientHandler> client = clientIt.next();
			String username = client.getKey();
			ClientHandler handler = client.getValue();
			if (this.username != username) {
				handler.sendUserLeft(roomID, this.username);
			}
		}
		
		/* 
		 * If the concerned room is the public room
		 * removes this user from all other rooms
		 */
		if (roomID == Server.ROOM_PUBLIC_KEY) {
			Iterator<Entry<String, HashMap<String, ClientHandler>>> roomsIt = server.getRooms().entrySet().iterator();
			while (roomsIt.hasNext()) {
				roomsIt.next().getValue().remove(this.username);
			}
		}
	}
	
	/**
	 * Notify the client that a user left a room
	 * @param roomID 
	 * @param username
	 */
	private void sendUserLeft(String roomID, String username)
	{
		send(Protocol.USER_LEAVE);
		send(roomID);
		send(username);
	}
	
	/**
	 * Sends a message
	 * @throws IOException Whether an input/output error occurs
	 */
	private void sendMessage() throws IOException
	{
		String roomID = in.readLine();
		String mess = in.readLine();
		
		System.out.println("Room id: "+roomID);
		System.out.println("Message: "+mess);
		
		Iterator<Map.Entry<String, ClientHandler>> it = server.getClients().entrySet().iterator();
		while (it.hasNext()) {
			ClientHandler client = it.next().getValue();
			
			client.send(Protocol.RECEIVE_MSG.toString());
			client.send(roomID);
			client.send(username);
			client.send(mess);
		}
	}
	
	/**
	 * Sends the list of the users position
	 */
	private void sendPositionsList()
	{
		// TODO Removes logs
		// TODO Handle the room 's id
		
		String tokenPos = new BigInteger(130, new SecureRandom()).toString(32);
		send(tokenPos);

		Iterator<Map.Entry<String, Position>> positionsIt = server.getPositions().entrySet().iterator();
		while (positionsIt.hasNext()) {
			Map.Entry<String, Position> client = positionsIt.next();
			
			send(client.getKey());
			send(client.getValue().toString());
		}
		
		send(tokenPos);
	}
	
	/**
	 * Sends the user position
	 * @throws IOException Whether an input/output error occurs
	 */
	private void sendUserPosition() throws IOException
	{
		// TODO Removes logs
		// TODO Handle the room 's id
		
		String pos = in.readLine();
		String[] coordinates = pos.split(":");
		
		position = new Position(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));

		Iterator<Map.Entry<String, ClientHandler>> destIt = server.getClients().entrySet().iterator();
		while (destIt.hasNext()) {
			ClientHandler client = destIt.next().getValue();
			
			System.out.println("Send: "+Protocol.RECEIVE_POS+" to ["+client.getName()+"]");
			client.send(Protocol.RECEIVE_POS);
			System.out.println("from: "+username);
			client.send(username);
			System.out.println("position: "+position);
			client.send(position.toString());
		}
	}
	
	/**
	 * Send the new user to that client
	 * @param pseudo The pseudo of the new user
	 */
	public void sendNewUser(String pseudo, String roomID) {
		send(Protocol.NEW_USER);
		send(roomID);
		send(pseudo);
	}
	
	/**
	 * Send the new position of a user to that client
	 * @param pos The psotion of the new user
	 */
	public void sendPosition(Position pos, String roomID) {
		send(Protocol.RECEIVE_POS);
		send(roomID);
		send(username);
		send(pos.toString());
	}
	
	/**
	 * Notifies the client that a new room is opened
	 * @param roomID The room's Id
	 * @param usernames the list of user's names
	 */
	public void sendNewRoomOpened(String roomID)
	{
		send(Protocol.NEW_ROOM);
		send(roomID);
		sendUsersList(roomID);
	}
	
	/**
	 * Send a message to the client side through the output stream
	 * @param msg The message to send
	 */
	private void send(Object msg) {
		out.println(msg);
		out.flush();
	}
	
	/**
	 * Get the socket of this client
	 * @return The socket of the client
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * Get the name of this client
	 * @return The name of the client
	 */
	public String getName() {
		return username;
	}
	
	/**
	 * Get the position of this client
	 * @return The position of the client
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Set the position of this client
	 * @param p The new position of the client
	 */
	public void setPosition(Position p) {
		position = p;
	}
}
