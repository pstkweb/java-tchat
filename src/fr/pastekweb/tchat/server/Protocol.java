package fr.pastekweb.tchat.server;

/**
 * A list of different messages sends between 
 * clients and server of this application
 * 
 * @author Thomas TRIBOULT
 */
public enum Protocol {
	CONNECT("connexion"),
	CONNECT_OK("connect_ok"),
	CONNECT_KO("connect_ko"),
	USERS_LIST("listUsers"),
	NEW_USER("addUser"),
	USER_LEAVE("removeUser"),
	NEW_ROOM("addRoom"),
	SEND_MSG("sendMSG"),
	RECEIVE_MSG("receiveMSG"),
	POSITIONS_LIST("listPositions"),
	SEND_POS("sendPos"),
	RECEIVE_POS("receivePos");
	
	/**
	 * The actual message sanded between sockets
	 */
	private String name;
	
	/**
	 * Instantiate a Protocol
	 * 
	 * @param name The message sanded by the protocol
	 */
	Protocol(String name) {
		this.name = name;
	}
	
	/**
	 * The String sanded
	 * 
	 * @return A String
	 */
	public String toString() {
		return "#"+name+"#";
	}
	
	/**
	 * Factory pattern to create a protocol by
	 * its String representation
	 * @param str The String representation of the Protocol
	 * 
	 * @return A Protocol
	 */
	public static Protocol createProtocol(String str) {
		for (Protocol p : Protocol.values()) {
			if (p.toString().equals(str)) {
				return p;
			}
		}
		
		return null;
	}
}
