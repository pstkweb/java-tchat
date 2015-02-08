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
import java.util.List;

import fr.pastekweb.tchat.server.Protocol;

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
	 * The clients list
	 */
	private ArrayList<String> clients;
	
	/**
	 * The client pseudo
	 */
	private String pseudo;
	
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
		this.clients = new ArrayList<>();
		try {
			socket = new Socket("127.0.0.1", port);	
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Input/Output error : " + e.getMessage());
		}
	}

	public boolean connect(String pseudo)
	{
		System.out.println("Send: "+Protocol.CONNECT);
		System.out.println("Pseudo: <"+pseudo+">");
		send(Protocol.CONNECT);
		this.pseudo = pseudo;
		send(pseudo);
		
		synchronized (this) {
			try {
				System.out.println("Waiting for server");
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
	public void askClientsList()
	{
		System.out.println("Send: "+Protocol.USERS_LIST);
		send(Protocol.USERS_LIST);
		
		System.out.println("Waiting for server");
	}
	
	/**
	 * Receives the client list
	 * @return boolean whether the list have been well received
	 */
	private boolean receiveClientList()
	{
		System.out.println("User list");
		try {
			String token = reader.readLine();
			System.out.println("Token: "+token);
			String pseudo;
			
			System.out.println("Add client :");
			while (!(pseudo = reader.readLine()).equals(token)) {
				System.out.println("- "+pseudo);
				addClient(pseudo);
			}
			System.out.println("Token end: "+pseudo);
			
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
			String pseudo = reader.readLine();
			clients.add(pseudo);
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
			String pseudo = reader.readLine();
			clients.remove(pseudo);
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
	public void addClient(String pseudo)
	{
		clients.add(pseudo);
	}

	/**
	 * Removes a client from the clients list
	 * @param pseudo The client pseudo
	 */
	public void removeClient(String pseudo)
	{
		clients.remove(pseudo);
	}
	
	@Override
	public void sendPrivateMessage(List<String> clients, String message)
	{
		send(Protocol.SEND_MP);
		String token = new BigInteger(130, new SecureRandom()).toString(32);
		send(token);
		for (String client: clients) {
			send(client);
		}
		send(token);
	}

	@Override
	public void sendPublicMessage(String message)
	{
		send(Protocol.SEND_MSG);
		send(message);
	}
	
	/**
	 * Receives a private message
	 * @return boolean whether the message have been well received
	 */
	private boolean receivePrivateMessage()
	{
		try {
			String from = reader.readLine();
			String message = reader.readLine();
			
			System.out.println("Private message from "+from+": "+message);
			// TODO: handle the reception
			
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
	private boolean receivePublicMessage()
	{
		try {
			String from = reader.readLine();
			String message = reader.readLine();
			
			System.out.println("Public message from "+from+": "+message);
			// TODO: handle the reception
			
			return true;
		} catch (IOException e) {
			System.out.println("Stream reading error: "+e.getMessage());
		}
		return false;
	}

	/**
	 * Gets the list of clients
	 * @return The list of clients
	 */
	public List<String> getClients()
	{
		return clients;
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
				System.out.println("----------------------------");
				System.out.println("msg: "+message);
				System.out.println("proto: "+Protocol.createProtocol(message));
				switch (Protocol.createProtocol(message)) {
					case CONNECT_OK:
						connected = true;
						// TODO: remove log
						System.out.println("Logged in as: "+pseudo);
						synchronized (this) { notify(); }
						break;
					case CONNECT_KO:
						connected = false;
						synchronized (this) { notify(); }
						break;
					case USERS_LIST:
						receiveClientList();
					case NEW_USER:
						receiveNewClient();
						break;
					case USER_LEAVE:
						receiveClientLeft();
						break;
					case RECEIVE_MP:
						receivePrivateMessage();
						break;
					case RECEIVE_MSG:
						receivePublicMessage();
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
