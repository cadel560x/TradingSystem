package ie.gmit.sw.fyp.repositories;

import org.springframework.data.repository.CrudRepository;

import ie.gmit.sw.fyp.model.User;




public interface UserRepository extends CrudRepository<User, String> {

	
} // end interface LimitOrderRepository
