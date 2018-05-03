package ie.gmit.sw.fyp.services;

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
	public boolean checkUserId(String userId) {
		return userRepository.existsById(userId);
		
	} // end checkUserId
	
	
	public static boolean checkUserIdStatic(String userId) {
		return userRepository.existsById(userId);
		
	} // end checkUserIdStatic
	
	
	public User save(User user) {
		return userRepository.save(user);
		
	} // end save(User user)
	
	
	public Iterable<User> findAll() {
		return userRepository.findAll();
		
	} // end findAll
	
} // end class UserService
