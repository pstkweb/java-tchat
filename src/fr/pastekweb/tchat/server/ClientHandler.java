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
import java.util.Iterator;
import java.util.Map;

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
	private String name;
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
	 * Instantiate a new client thread
	 * @param s The socket used by that client
	 * @param serv The main server class reference
	 */
	public ClientHandler(Socket s, Server serv) {
		server = serv;
		socket = s;
		name = "";
		
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
		while (true) {
			try {
				System.out.println("Waiting for user");
				String s = in.readLine();
				System.out.println("--------------------");
				System.out.println("msg : " + s);
				System.out.println("proto: " + Protocol.createProtocol(s));
				switch (Protocol.createProtocol(s)) {
					case CONNECT:
						System.out.println("User connexion");
						String pseudo = in.readLine();
						if (!server.isUsed(pseudo)) {
							System.out.println("Pseudo : <" + pseudo + "> free");
							name = pseudo;
							
							System.out.println("Send "+Protocol.CONNECT_OK);
							sendMessage(Protocol.CONNECT_OK.toString());
							
							server.addClient(name, this);
						} else {
							System.out.println("Pseudo : <" + pseudo + "> unavailable");
							
							System.out.println("Send "+Protocol.CONNECT_KO);
							sendMessage(Protocol.CONNECT_KO.toString());
						}

						break;
					case USERS_LIST:
						String tokenUsers = new BigInteger(130, new SecureRandom()).toString(32);
						System.out.println("Write token[" + tokenUsers + "] for users list");
						sendMessage(tokenUsers);
						for (String user : server.getUsernames()) {
							sendMessage(user);
						}
						sendMessage(tokenUsers);
						break;
					case SEND_MP:
						ArrayList<ClientHandler> users = new ArrayList<ClientHandler>();
						String taken = in.readLine();
						String user;
						while(!(user = in.readLine()).equals(taken)) {
							users.add(server.getClient(user));
						}
						String msg = in.readLine();
						
						for (ClientHandler client : users) {
							client.sendMessage(Protocol.RECEIVE_MP.toString());
							client.sendMessage(name);
							client.sendMessage(msg);
						}
						break;
					case SEND_MSG:
						String mess = in.readLine();
						
						Iterator<Map.Entry<String, ClientHandler>> it = server.getClients().entrySet().iterator();
						while (it.hasNext()) {
							ClientHandler client = it.next().getValue();
							
							System.out.println("Send: "+Protocol.RECEIVE_MSG+" to ["+client.name+"]");
							client.sendMessage(Protocol.RECEIVE_MSG.toString());
							System.out.println("from: "+name);
							client.sendMessage(name);
							System.out.println("message: "+mess);
							client.sendMessage(mess);
						}
						break;
					case POSITIONS_LIST:
						String tokenPos = new BigInteger(130, new SecureRandom()).toString(32);
						System.out.println("Write token[" + tokenPos + "] for positions list");
						sendMessage(tokenPos);

						Iterator<Map.Entry<String, Position>> positionsIt = server.getPositions().entrySet().iterator();
						while (positionsIt.hasNext()) {
							Map.Entry<String, Position> client = positionsIt.next();
							
							sendMessage(client.getKey());
							sendMessage(client.getValue().toString());
						}
						
						sendMessage(tokenPos);
						
						break;
					case SEND_POS:
						String pos = in.readLine();
						String[] coordinates = pos.split(":");
						
						position = new Position(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));

						Iterator<Map.Entry<String, ClientHandler>> destIt = server.getClients().entrySet().iterator();
						while (destIt.hasNext()) {
							ClientHandler client = destIt.next().getValue();
							
							System.out.println("Send: "+Protocol.RECEIVE_POS+" to ["+client.getName()+"]");
							client.sendMessage(Protocol.RECEIVE_POS.toString());
							System.out.println("from: "+name);
							client.sendMessage(name);
							System.out.println("position: "+position);
							client.sendMessage(position.toString());
						}
						
						break;
					default:
						System.out.println("Message non supporté par le serveur.");
				}
			} catch (IOException e) {
				System.out.println("Erreur d'E/S : " + e.getMessage());
			}
		}
	}
	
	/**
	 * Send the new user to that client
	 * @param pseudo The pseudo of the new user
	 */
	public void sendNewUser(String pseudo) {
		sendMessage(Protocol.NEW_USER.toString());
		sendMessage(pseudo);
	}
	
	/**
	 * Send the new position of a user to that client
	 * @param pos The psotion of the new user
	 */
	public void sendPosition(Position pos) {
		sendMessage(Protocol.RECEIVE_POS.toString());
		sendMessage(pos.toString());
	}
	
	/**
	 * Send the user list to that client
	 */
	public void getUsersList() {
		String token = new BigInteger(130, new SecureRandom()).toString(32);
		
		sendMessage(Protocol.USERS_LIST.toString());
		sendMessage(token);
		for (String user : server.getUsernames()) {
			sendMessage(user);
		}
		sendMessage(token);
	}
	
	/**
	 * Send a message to the client side through the output stream
	 * @param msg The message to send
	 */
	private void sendMessage(String msg) {
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
		return name;
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
