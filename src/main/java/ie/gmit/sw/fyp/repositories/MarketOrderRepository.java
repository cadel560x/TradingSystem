package ie.gmit.sw.fyp.repositories;

import org.springframework.data.repository.CrudRepository;

import ie.gmit.sw.fyp.matchengine.MarketOrder;

public interface MarketOrderRepository extends CrudRepository<MarketOrder, String> {	
	
	
} // end interface MarketOrderRepository
