package fr.pastekweb.tchat.server;

public enum Protocol {
	CONNECT("connexion"),
	CONNECT_OK("connect_ok"),
	CONNECT_KO("connect_ko"),
	USERS_LIST("listUsers"),
	NEW_USER("addUser"),
	USER_LEAVE("removeUser"),
	SEND_MP("sendMP"),
	RECEIVE_MP("receiveMP"),
	SEND_MSG("sendMSG"),
	RECEIVE_MSG("receiveMSG");
	
	private String name;
	
	Protocol(String name) {
		this.name = name;
	}
	
	public String toString() {
		return "#"+name+"#";
	}
	
	public static Protocol createProtocol(String str) {
		for (Protocol p : Protocol.values()) {
			if (p.toString().equals(str)) {
				return p;
			}
		}
		
		return null;
	}
}
