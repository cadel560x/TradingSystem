package ie.gmit.sw.fyp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.matchengine.OrderMatch;
import ie.gmit.sw.fyp.repositories.OrderMatchRepository;




@Service
public class OrderMatchService {
//	Fields
	@Autowired
	OrderMatchRepository orderMatchRepository;
	
	
	
	
//	Methods
	public void save(OrderMatch orderMatch) {
		orderMatchRepository.save(orderMatch.getId(), orderMatch.getTimestamp(), orderMatch.getSellOrder().getId(),
				orderMatch.getBuyOrder().getId(), orderMatch.getFilledShares());
		
	} // end save(OrderMatch orderMatch)

} // end class OrderMatchService
