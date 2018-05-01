package ie.gmit.sw.fyp.services;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.model.User;
import ie.gmit.sw.fyp.repositories.UserRepository;




@Service
public class UserService {
//	Fields
	private static UserRepository userRepository;
	
	
	
	
//	Constructors
	public UserService() {

	}
	
	
	
	
//	Accessors and mutators
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		UserService.userRepository = userRepository;
	}




//	Methods
	@PostConstruct
	public void initService() {		
		Iterable<User> userList = this.findAll();
		if ( ((Collection<User>)userList).isEmpty() ) {
			User user = new User();
			user.setUserId("dfgjkaga9");
			user.setDescription("Javier");
			user.setUserPassword("verySecret");
			userRepository.save(user);
			
			user.setUserId("uiahfu938");
			user.setDescription("Martin");
			user.setUserPassword("verySecretToo");
			userRepository.save(user);
		}
		
	} // end initService
	
	
	public static boolean checkUserId(String userId) {
		return userRepository.existsById(userId);
		
	} // end checkUserId
	
	
	public User save(User user) {
		return userRepository.save(user);
		
	} // end save(User user)
	
	
	public Iterable<User> findAll() {
		return userRepository.findAll();
		
	} // end findAll
	
} // end class UserService
