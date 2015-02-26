package fr.pastekweb.tchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;

import fr.pastekweb.tchat.model.Position;
import fr.pastekweb.tchat.model.Room;
import fr.pastekweb.tchat.model.Tchat;
import fr.pastekweb.tchat.model.User;
import fr.pastekweb.tchat.server.Protocol;
import fr.pastekweb.tchat.server.Server;

/**
 * The default Client class
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class DefaultClient implements IClient
{	
	private static final String DEFAULT_IP = "127.0.0.1";
	private static final int DEFAULT_PORT = 1337;
	
	/**
	 * The socket connected to the Server
	 */
	private Socket socket;
	/**
	 * The socket's writer
	 */
	private PrintWriter writer;
	/**
	 * The socket's reader
	 */
	private BufferedReader reader;
	/**
	 * The tchat model
	 */
	private Tchat tchat;
	/**
	 * Whether the client is connected
	 */
	private boolean connected;
	/**
	 * Boolean whether the client is alive or not
	 */
	private boolean isAlive;
	
	/**
	 * Creates an instance of Client with the default configuration:
	 * IP => 127.0.0.1
	 * PORT => 1337
	 */
	public DefaultClient()
	{
		this(DEFAULT_IP);
	}
	
	/**
	 * Creates an instance of the Client with the default port
	 * PORT => 1337
	 * @param ip The IP address
	 */
	public DefaultClient(String ip)
	{
		this(ip, DEFAULT_PORT);
	}
	
	/**
	 * Creates an instance of Client using
	 * the given port
	 * @param port The port to use
	 */
	public DefaultClient(String ip, int port)
	{
		this.connected = false;
		this.isAlive = true;
		this.tchat = new Tchat();
		try {
			socket = new Socket(ip, port);	
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Input/Output error : " + e.getMessage());
		}
	}

	@Override
	public Tchat getTchat()
	{
		return tchat;
	}

	@Override
	public boolean connect(String pseudo)
	{
		send(Protocol.CONNECT);
		tchat.setPseudo(pseudo);
		send(tchat.getUser().getPseudo());
		
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return connected;
	}
	
	@Override
	public boolean isConnected() 
	{
		return connected;
	}

	@Override
	public void askClientsList(String roomID)
	{
		send(Protocol.USERS_LIST);
		send(roomID);
		
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

    @Override
    public void askPositionsList(String roomID) {
        send(Protocol.POSITIONS_LIST);
        send(roomID);

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Adds a client to the clients list
     * @param roomID The room in which to add the user
	 * @param user The client
	 */
	public void addClient(String roomID, User user)
	{
		tchat.addUser(roomID, user);
	}

    /***
     * Adds a position to the positions list
     * @param roomID The room in which to add the user
     * @param user The user
     * @param position The position
     */
    public void addPosition(String roomID, User user, Position position)
    {
        tchat.addPosition(roomID, user, position);
    }

	/**
	 * Removes a client from the clients list
	 * @param user The client
	 */
	public void removeClient(String roomID, User user)
	{
		tchat.removeUser(roomID, user);
	}

	@Override
	public void sendMessage(String message, String roomID)
	{
		send(Protocol.SEND_MSG);
		send(roomID);
		send(message);

		System.out.println("-------------------");
		System.out.println("Send protocol: "+Protocol.SEND_MSG);
		System.out.println("Room id: "+roomID);
		System.out.println("Message: "+message);
	}
	
	@Override
	public void sendMessage(String message)
	{
		sendMessage(message, Server.ROOM_PUBLIC_KEY);
	}

	@Override
	public void newRoom(ArrayList<String> userNames)
	{
		String token = new BigInteger(130, new SecureRandom()).toString(32);
		send(Protocol.NEW_ROOM);
		send(token);

		System.out.println("-------------------");
		System.out.println("Send Protocol: "+Protocol.NEW_ROOM);
		System.out.println("Token: "+token);
		
		if (!userNames.contains(tchat.getUser().getPseudo())) {
			userNames.add(tchat.getUser().getPseudo());
		}
		
		for (String username : userNames) {
			send(username);
			System.out.println("User: "+username);
		}
		send(token);
		System.out.println("End token: "+token);
	}

    /**
     * Sends a posistion through the socket writer
     * @param position The position of the user
     * @param roomID The room's id of the current room
     */
    public void sendPosition(Position position, String roomID)
    {
        send(Protocol.SEND_POS);
        send(roomID);
        send(position);

        System.out.println("-------------------");
        System.out.println("Send protocol: "+Protocol.SEND_POS);
        System.out.println("Room id: "+roomID);
        System.out.println("Position: " + position);
    }

	/**
	 * Sends a message through the socket writer
	 * and ensures that the message is well flushed
	 * @param message The message to send
	 */
	private void send(Object message)
	{
		writer.println(message);
		writer.flush();
	}
	
	/**
	 * Receive the new roomId
	 * @return Whether the room have been well created
	 */
	private boolean receiveNewRoom()
	{
		try {
			String roomID = reader.readLine();
			System.out.println("Room id: "+roomID);
			Room room = new Room(roomID);
			tchat.addRoom(room);
			
			return true;
		} catch (IOException e) {
			System.out.println("Stream reading error: "+e.getMessage());
		}
		return false;
	}
	
	/**
	 * Receives the client list
	 * @return boolean whether the list have been well received
	 */
	private boolean receiveClientList()
	{
		try {
			String roomID = reader.readLine();
			String token = reader.readLine();
			String pseudo;
			
			System.out.println("Room id: "+roomID);
			System.out.println("Token: "+token);
			
			while (!(pseudo = reader.readLine()).equals(token)) {
				addClient(roomID, new User(pseudo));
				System.out.println("User: "+pseudo);
			}
			System.out.println("End token: "+pseudo);
			
			return true;
		} catch (IOException e) {
			System.out.println("Stream reading error: "+e.getMessage());
		}
		return false;
	}
	
	/**
	 * Receives the new client
	 * @return boolean whether the new user have been well received
	 */
	private boolean receiveNewClient()
	{
		try {
			String roomID = reader.readLine();
			String username = reader.readLine();
			addClient(roomID, new User(username));
			return true;
		} catch (IOException e) {
			System.out.println("Stream reading error: "+e.getMessage());
		}
		return false;
	}
	
	/**
	 * Receives the pseudo of the client who left the tchat
	 * @return boolean whether the pseudo have been well received
	 */
	private boolean receiveClientLeft()
	{
		try {
			String roomID = reader.readLine();
			String username = reader.readLine();
			removeClient(roomID, new User(username));
			return true;
		} catch (IOException e) {
			System.out.println("Stream reading error: "+e.getMessage());
		}
		return false;
	}
	
	/**
	 * Receives a private message
	 * @return boolean whether the message have been well received
	 */
	private boolean receiveMessage()
	{
		try {
			String roomID = reader.readLine();
			String from = reader.readLine();
			String message = reader.readLine();
			
			System.out.println("Room id: "+roomID);
			System.out.println("From: "+from);
			System.out.println("Message: "+message);
			
			tchat.addMessage(roomID, from, message);
			
			return true;
		} catch (IOException e) {
			System.out.println("Stream reading error: "+e.getMessage());
		}
		return false;
	}

    /**
     * Receives the positions list
     * @return boolean whether the list have been well received
     */
    private boolean receivePositionList()
    {
        try {
            String roomID = reader.readLine();
            String token = reader.readLine();
            String user;

            System.out.println("Room id: "+roomID);
            System.out.println("Token: "+token);

            while (!(user = reader.readLine()).equals(token)) {
                System.out.println("User: "+user);
                String position = reader.readLine();
                String[] coordinates = position.split(":");
                Position pos = new Position(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));

                addPosition(roomID, new User(user), pos);
                System.out.println("Position: "+pos);
            }
            System.out.println("End token: "+user);

            return true;
        } catch (IOException e) {
            System.out.println("Stream reading error: "+e.getMessage());
        }
        return false;
    }
	
	/**
	 * Receive the position of a user in a room
	 * @return True if position is well received
	 */
	private boolean receivePosition()
	{
		try {
			String roomID = reader.readLine();
			User from = new User(reader.readLine());
			String pos = reader.readLine();
			String[] coordinates = pos.split(":");
			
			Position position = new Position(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
			
			System.out.println("Room id: "+roomID);
			System.out.println("User: "+from);
			System.out.println("Position: "+position.toString());

            // Update the user position
            tchat.setUserPosition(roomID, from, position);

			return true;
		} catch (IOException e) {
			System.out.println("Stream reading error: "+e.getMessage());
		}
		
		return false;
	}

	@Override
	public void run()
	{
		while (isAlive) {
			try {
				String message = reader.readLine();
				System.out.println("-------------------");
				System.out.println("Protocol: "+message);
				switch (Protocol.createProtocol(message)) {
					case CONNECT_OK:
						connected = true;
						synchronized (this) { notify(); }
						break;
						
					case CONNECT_KO:
						connected = false;
						synchronized (this) { notify(); }
						break;
						
					case NEW_ROOM:
						receiveNewRoom();
						break;
						
					case USERS_LIST:
						receiveClientList();
						synchronized (this) { notify(); }
						break;
						
					case NEW_USER:
						receiveNewClient();
						break;
						
					case USER_LEAVE:
						receiveClientLeft();
						break;
					
					case RECEIVE_MSG:
						receiveMessage();
						break;

                    case POSITIONS_LIST:
                        receivePositionList();
                        synchronized (this) { notify(); }
                        break;
						
					case RECEIVE_POS:
						receivePosition();
						break;
						
					default:
						break;
				}
			} catch (IOException e) {
				System.out.println("Input/Output error : " + e.getMessage());
			} catch (NullPointerException e) {
				System.out.println("Unsupported command.");
			}
		}
	}
}
