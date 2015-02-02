package fr.pastekweb.server;

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

public class ClientHandler implements Runnable {
	private Socket socket;
	private Server server;
	private String name;
	private BufferedReader in;
	private PrintWriter out;
	
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
							
							out.println(Protocol.CONNECT_OK);
							out.flush();
							
							server.addClient(name, this);
						} else {
							System.out.println("Pseudo : <" + pseudo + "> unavailable");
							
							out.println(Protocol.CONNECT_KO);
							out.flush();
						}

						break;
					case USERS_LIST:
						sendMessage(Protocol.USERS_LIST.toString());
						
						String token = new BigInteger(130, new SecureRandom()).toString(32);
						System.out.println("Write token[" + token + "] for users list");
						sendMessage(token);
						for (String user : server.getUsernames()) {
							sendMessage(user);
						}
						sendMessage(token);
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
							client.getOut().println(Protocol.RECEIVE_MP);
							client.getOut().println(name);
							client.getOut().println(msg);
							client.getOut().flush();
						}
						break;
					case SEND_MSG:
						String mess = in.readLine();
						
						Iterator<Map.Entry<String, ClientHandler>> it = server.getClients().entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry<String, ClientHandler> client = it.next();
							
							client.getValue().getOut().println(Protocol.RECEIVE_MSG);
							client.getValue().getOut().println(name);
							client.getValue().getOut().println(mess);
							client.getValue().getOut().flush();
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
	
	public void sendNewUser(String pseudo) {
		sendMessage(Protocol.NEW_USER.toString());
		sendMessage(pseudo);
	}
	
	public void getUsersList() {
		String token = new BigInteger(130, new SecureRandom()).toString(32);
		

		sendMessage(Protocol.USERS_LIST.toString());
		sendMessage(token);
		for (String user : server.getUsernames()) {
			sendMessage(user);
		}
		sendMessage(token);
	}
	
	private void sendMessage(String msg) {
		out.println(msg);
		out.flush();
	}

	public PrintWriter getOut() {
		return out;
	}
	
	public Socket getSocket() {
		return socket;
	}
}
