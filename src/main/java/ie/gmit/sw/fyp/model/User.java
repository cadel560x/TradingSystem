package ie.gmit.sw.fyp.model;

import javax.persistence.Entity;
import javax.persistence.Id;




@Entity
public class User {
//	Fields
	@Id
	private String userId;
	private String userPassword;
	private String description;
	
	
	
	
//	Constructors
	public User() {
		
	}

	public User(String userId, String userPassword, String description) {
		this.userId = userId;
		this.userPassword = userPassword;
		this.description = description;
	}

	
	
	
//	Accessors and mutators
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	
	
	
	//	Methods
	@Override
	public String toString() {
		return "User [userId=" + userId + ", description=" + description + "]";
	}
	
} // end class User
