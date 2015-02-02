package fr.pastekweb.server;

public enum Protocol {
	CONNECT("connexion"),
	OK("ok"),
	KO("ko"),
	USERS_LIST("liste"),
	MAJ_USERS_LIST("majListe"),
	SEND_MP("envoiMP"),
	RECEIVE_MP("receptionMP"),
	SEND_MSG("envoiMSG"),
	RECEIVE_MSG("receptionMSG");
	
	private String name;
	
	Protocol(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
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
