package fr.pastekweb.tchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import fr.pastekweb.tchat.model.Tchat;
import fr.pastekweb.tchat.server.Protocol;
import fr.pastekweb.tchat.server.Server;

/**
 * The default Client class
 * 
 * @author Antoine LELAISANT <antoine.lelaisant@gmail.com>
 */
public class DefaultClient implements IClient
{	
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
	 * Creates an instance of Client with a
	 * default port (1337)
	 */
	public DefaultClient()
	{
		this(1337);
	}
	
	/**
	 * Creates an instance of Client using
	 * the given port
	 * @param port The port to use
	 */
	public DefaultClient(int port)
	{
		this.connected = false;
		this.isAlive = true;
		this.tchat = new Tchat();
		try {
			socket = new Socket("127.0.0.1", port);	
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Input/Output error : " + e.getMessage());
		}
	}

	@Override
	public boolean connect(String pseudo)
	{
		send(Protocol.CONNECT);
		tchat.setPseudo(pseudo);
		send(tchat.getUsername());
		
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
				addClient(roomID, pseudo);
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
			addClient(roomID, username);
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
			removeClient(roomID, username);
			return true;
		} catch (IOException e) {
			System.out.println("Stream reading error: "+e.getMessage());
		}
		return false;
	}

	/**
	 * Adds a client to the clients list
	 * @param pseudo The client pseudo
	 */
	public void addClient(String roomID, String pseudo)
	{
		tchat.addUser(roomID, pseudo);
	}

	/**
	 * Removes a client from the clients list
	 * @param pseudo The client pseudo
	 */
	public void removeClient(String roomID, String pseudo)
	{
		tchat.removeUser(roomID, pseudo);
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
			
			// TODO: handle the reception
			
			return true;
		} catch (IOException e) {
			System.out.println("Stream reading error: "+e.getMessage());
		}
		return false;
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
