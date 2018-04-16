package ie.gmit.sw.fyp.order;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.me.MarketOrder;
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
		MarketOrder marketOrder;
		
		//
		OrderBook orderBook = orderBooks.get(stockTag);
		if ( orderBook == null || ! orderBook.getStockTag().equals(postRequest.getStockTag()) ) {
			notification.updateMessage("Invalid stock market");
			
			return notification;
		}
			
		if ( orderBook.checkRequest(postRequest) ) {
			marketOrder = orderBook.createOrder(postRequest);
			notification.setMessage("ACCEPTED: OrderId " + (marketOrder).getId());
		}
		else {
			notification.updateMessage("Invalid request properties");
			return notification;
		}
		
		//
		if ( orderBook.matchOrder(marketOrder) ) {
			notification.updateMessage("\nMATCHED");
			
			return notification;
		}
		else {
			// TODO Remove this! Use an Observable for notifications
			marketOrder.attachTo(orderBook);
			
			if ( marketOrder instanceof MarketOrder ) {
				marketOrder.setStatus(OrderStatus.ERROR);
				notification.updateMessage("\nNOT MATCHED");
			}
			
		} // if ( matchOrder(postOrder) )
		
		return notification;
		
	} // end addPostOrder
	
} // end class OrderBookService
