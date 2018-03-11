package ie.gmit.sw.fyp.order;

import java.util.HashMap;
//import java.util.List;
import java.util.Map;
//import java.util.NoSuchElementException;
//import java.util.TreeMap;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.PathVariable;

import ie.gmit.sw.fyp.me.MatchOrder;
import ie.gmit.sw.fyp.me.PostOrder;
//import ie.gmit.sw.fyp.me.PostOrderType;
import ie.gmit.sw.fyp.me.PostRequest;
import ie.gmit.sw.fyp.notification.Notification;




@Service
public class OrderBookService {
//	Fields
	private Map<String, OrderBook> orderBooks;
	
	
	
	
//  Constructors
	public OrderBookService() {
		initialize();
	}




//	Methods
	private void initialize() {
		Map<String, OrderBook> orderBookDAO = new HashMap<>();
		orderBookDAO.put("AAPL", new OrderBook("AAPL"));
		orderBookDAO.put("GOOGL", new OrderBook("GOOGL"));
		
		// TODO Replace DAO call with JPA Repository
		orderBooks = orderBookDAO;
		
	} // end initialize()
	
	
	public Notification addPostOrder(String stockTag, PostRequest postRequest) {
		Notification notification = new Notification("REJECTED: ");
		PostOrder postOrder;
		
		//
		OrderBook orderBook = orderBooks.get(stockTag);
		if ( orderBook == null || ! orderBook.getStockTag().equals(postRequest.getStockTag()) ) {
			notification.updateMessage("Invalid stock market");
			
			return notification;
		}
		
		//
		if ( postRequest.checkProperties() ) {
			// Factory pattern
			postOrder = orderBook.createOrder(postRequest);
//			postOrder = new PostOrder(postRequest);
			
			notification.setMessage("ACCEPTED: OrderId " + (postOrder).getId());
		}
		else {
			notification.updateMessage("Invalid request");
			
			return notification;
		}
		
		
		//
		if ( orderBook.matchOrder(postOrder) ) {
			notification.updateMessage("\nMATCHED");
			
			return notification;
		}
		else {
			// TODO Remove this! Use an Observable for notifications
			if ( postOrder instanceof MatchOrder ) {
				notification.setMessage("\nNOT MATCHED");
				
				return notification;
			}
			
			orderBook.place(postOrder);
			
		} // if ( matchOrder(postOrder) )
		
		return notification;
		
	} // end addPostOrder
	
} // end class OrderBookService
