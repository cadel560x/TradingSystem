package ie.gmit.sw.fyp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.me.OrderMatch;
import ie.gmit.sw.fyp.repositories.OrderMatchRepository;




@Service
public class OrderMatchService {
//	Fields
	@Autowired
	OrderMatchRepository orderMatchRepository;
	
	
	
	
//	Methods
	public OrderMatch save(OrderMatch orderMatch) {
		return orderMatchRepository.save(orderMatch);
		
	} // end save(OrderMatch orderMatch)

} // end class OrderMatchService
