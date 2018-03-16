package ie.gmit.sw.fyp.order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;




@Service
public class UserService {
//	Fields
	private Map<String, String> users;
	
	private static UserService onlyInstance;
	
	
	
	
//	Constructors
	public UserService() {
		users = new HashMap<>();
		
		initService();
	}
	
	
	
	
//	Methods
	public void initService() {
		// Should call a real DAO
		users.put("dfgjkaga9", "Javier");
		users.put("uiahfu938", "Martin");
	}
	
	
	public boolean checkUserId(String userId) {
		if ( users.containsKey(userId) ) {
			return true;
		}
		
		return false;
	} // end checkUserId
	
	
	// Singleton
	public static UserService getInstance() {
		if ( onlyInstance == null ) {
			onlyInstance = new UserService();
		}
		
		return onlyInstance;
		
	} // end getInstance()
	
} // end class UserService
