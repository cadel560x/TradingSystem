package ie.gmit.sw.fyp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.matchengine.OrderMatch;
import ie.gmit.sw.fyp.repositories.OrderMatchRepository;




@Service
public class OrderMatchService {
//	Fields
	@Autowired
	private OrderMatchRepository orderMatchRepository;
	
	
	
	
//	Methods
	public void save(OrderMatch orderMatch) {
		orderMatchRepository.save(orderMatch);
		
	} // end save(OrderMatch orderMatch)
	
	
	public void deleteAll() {
		orderMatchRepository.deleteAll();
		
	} // end void deleteAll()

} // end class OrderMatchService
