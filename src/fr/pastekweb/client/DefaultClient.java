package fr.pastekweb.client;

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

import fr.pastekweb.server.Protocol;

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

	@Override
	public boolean connect(String pseudo)
	{
		send(Protocol.CONNECT);
		send(pseudo);
		try {
			if (reader.readLine().equals(Protocol.CONNECT_OK)) {
				return true;
			}
		} catch (IOException e) {
			System.out.println("Stream reading error: "+e.getMessage());
		}
		return false;
	}

	@Override
	public void askClientsList()
	{
		send(Protocol.USERS_LIST);
	}
	
	/**
	 * Receives the client list
	 * @return boolean whether the list have been well received
	 */
	private boolean receiveClientList()
	{
		try {
			String token = reader.readLine();
			String pseudo;
			while ((pseudo = reader.readLine()) != token) {
				addClient(pseudo);
			}
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
	
	private boolean receivePrivateMessage()
	{
		try {
			String message = reader.readLine();
			
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
				switch (Protocol.createProtocol(message)) {
					case CONNECT_OK:
						connected = true;
						break;
					case CONNECT_KO:
						connected = false;
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
						// TODO receive public message
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
