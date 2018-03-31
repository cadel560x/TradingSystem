package ie.gmit.sw.fyp.notification;

public class Notification {
//	Fields
	private StringBuilder message;

	
	
	
//	Constructors
	public Notification() {
		this.message = new StringBuilder();
	}

	public Notification(String message) {
		this.message = new StringBuilder(message);
	}

	
	
	
//	Accessors and mutators
	public String getMessage() {
		return message.toString();
	}

	public void setMessage(String message) {
		this.message.setLength(0);
		
		this.message.append(message);
	}
	
	
	
	
//	Methods
	public void updateMessage(String message) {
		this.message.append(message);
	} // updateMessage
	
} // end class Notification
