package fr.pastekweb.tchat.server;

import fr.pastekweb.tchat.model.Position;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * The client thread used by the server
 * 
 * @author Thomas TRIBOULT
 */
public class ClientHandler implements Runnable {
    /**
	 * A reference to the server main class
	 */
	private Server server;
	/**
	 * The pseudo of this user
	 */
	private String username;
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
				switch (Protocol.getProtocol(s)) {

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
                server.disconnectedUser(this);
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
		if (server.isNotUsed(pseudo)) {
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
	 * @param roomID The id of room which the user leave
	 */
	private void sendUsersList(String roomID) throws IOException {
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
		System.out.println("End token: " + tokenUsers);
	}
	
	/**
	 * Notify others clients handler that the user left
	 */
	public void notifyUserLeft(String roomID) throws IOException {
		HashMap<String, ClientHandler> clients = server.getRooms().get(roomID);
		
		// Notify others clients connected to this room that the user left
        for (Entry<String, ClientHandler> client : clients.entrySet()) {
            String username = client.getKey();
            ClientHandler handler = client.getValue();
            if (!this.username.equals(username) ) {
                handler.sendUserLeft(roomID, this.username);
            }
        }
		
		/* 
		 * If the concerned room is the public room
		 * removes this user from all other rooms
		 */
		if (roomID.equals(Server.ROOM_PUBLIC_KEY)) {
            for (Entry<String, HashMap<String, ClientHandler>> stringHashMapEntry : server.getRooms().entrySet()) {
                stringHashMapEntry.getValue().remove(this.username);
            }
		}
	}
	
	/**
	 * Notify the client that a user left a room
	 * @param roomID The id of room which the user leave
	 * @param username The pseudo of the user who is leaving
	 */
	private void sendUserLeft(String roomID, String username) throws IOException {
        // TODO : remove logs
		send(Protocol.USER_LEAVE);
		send(roomID);
		send(username);

        System.out.println("Send: "+Protocol.USER_LEAVE);
        System.out.println("Room id:"+roomID);
        System.out.println("User: "+username);
    }
	
	/**
	 * Sends a message
	 * @throws IOException Whether an input/output error occurs
	 */
	private void sendMessage() throws IOException
	{
        // TODO Removes logs
		String roomID = in.readLine();
		String mess = in.readLine();
		
		System.out.println("Room id: "+roomID);
		System.out.println("Message: " + mess);

        for (Entry<String, ClientHandler> stringClientHandlerEntry : server.getClients().entrySet()) {
            ClientHandler client = stringClientHandlerEntry.getValue();

            client.send(Protocol.RECEIVE_MSG.toString());
            client.send(roomID);
            client.send(username);
            client.send(mess);
        }
	}
	
	/**
	 * Sends the list of users position for a room
	 */
	private void sendPositionsList() throws IOException
	{
        // Gets the room 's id of the requested list
        String roomID = in.readLine();

        sendPositionsList(roomID);
	}

    private void sendPositionsList(String roomID) throws IOException
    {
        // TODO Removes logs
        String tokenPos = new BigInteger(130, new SecureRandom()).toString(32);

        send(Protocol.POSITIONS_LIST);
        send(roomID);
        send(tokenPos);

        System.out.println("Room id: " + roomID);
        System.out.println("Token: " + tokenPos);

        for (Entry<String, Position> client : server.getPositions(roomID).entrySet()) {
            send(client.getKey());
            System.out.println("User: " + client.getKey());
            send(client.getValue().toString());
            System.out.println("Position: " + client.getValue());
        }

        send(tokenPos);
        System.out.println("End Token: " + tokenPos);
    }
	
	/**
	 * Sends the user position to all users in the room
	 * @throws IOException Whether an input/output error occurs
	 */
	private void sendUserPosition() throws IOException
	{
		// TODO Removes logs
        // Gets the room 's id of the requested list
        String roomID = in.readLine();
		String pos = in.readLine();
		String[] coordinates = pos.split(":");

        // Update position on server
		Position position = new Position(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
        server.getPositions(roomID).put(username, position);

        // Send new position to all clients in the room
        for (Entry<String, ClientHandler> stringClientHandlerEntry : server.getRooms().get(roomID).entrySet()) {
            ClientHandler client = stringClientHandlerEntry.getValue();

            System.out.println("Send: " + Protocol.RECEIVE_POS + " to [" + client.getName() + "]");
            client.send(Protocol.RECEIVE_POS);
            System.out.println("Room: " + roomID);
            client.send(roomID);
            System.out.println("from: " + username);
            client.send(username);
            System.out.println("position: " + position);
            client.send(position.toString());
        }
	}
	
	/**
	 * Send the new user to that client
	 * @param pseudo The pseudo of the new user
	 */
	public void sendNewUser(String pseudo, String roomID) throws IOException {
		send(Protocol.NEW_USER);
		send(roomID);
		send(pseudo);
	}
	
	/**
	 * Send the new position of a user to that client
	 * @param pos The position of the new user
	 */
	public void sendPosition(String user, Position pos, String roomID) throws IOException {
        System.out.println("Envoi la position");
        send(Protocol.RECEIVE_POS);
		send(roomID);
		send(user);
		send(pos.toString());
	}
	
	/**
	 * Notifies the client that a new room is opened
	 * @param roomID The room's Id
	 */
	public void sendNewRoomOpened(String roomID) throws IOException {
		send(Protocol.NEW_ROOM);
		send(roomID);

		sendUsersList(roomID);
        sendPositionsList(roomID);
	}
	
	/**
	 * Send a message to the client side through the output stream
	 * @param msg The message to send
	 */
	private void send(Object msg) throws IOException {
        out.println(msg);
        out.flush();
	}
	
	/**
	 * Get the name of this client
	 * @return The name of the client
	 */
	public String getName()
    {
		return username;
	}

    /**
     * Ends the thread
     */
    public void terminate()
    {
        isAlive = false;
    }
}
