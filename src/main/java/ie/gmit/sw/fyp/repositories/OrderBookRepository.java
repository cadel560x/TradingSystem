package ie.gmit.sw.fyp.repositories;

import org.springframework.data.repository.CrudRepository;

import ie.gmit.sw.fyp.model.OrderBook;




public interface OrderBookRepository extends CrudRepository<OrderBook, String> {

	
} // end interface OrderBookRepository
