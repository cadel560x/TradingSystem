package ie.gmit.sw.fyp.order;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.me.MarketOrder;
import ie.gmit.sw.fyp.me.OrderMatch;
import ie.gmit.sw.fyp.me.PostOrderCondition;
import ie.gmit.sw.fyp.me.PostRequest;
import ie.gmit.sw.fyp.notification.Notification;
import ie.gmit.sw.fyp.services.MarketOrderService;
import ie.gmit.sw.fyp.services.OrderMatchService;




@Service
public class OrderBookService {
//	Fields
	@Autowired
	private OrderMatchService orderMatchService;
	
	@Autowired
	private MarketOrderService marketOrderService;
	
	private Map<String, OrderBook> orderBooks;
	
//	Data members
	private final Logger logOrder = LoggerFactory.getLogger("ie.gmit.sw.fyp.order");
	private final Logger logRequest = LoggerFactory.getLogger("ie.gmit.sw.fyp.request");
	
	
	
	
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
			logRequest.info("Request accepted for user: " + postRequest.getUserId() + " in market: " + postRequest.getStockTag());
			logRequest.debug(postRequest.toString());
			
			marketOrder = orderBook.createOrder(postRequest);
			logOrder.info("Order created: " + marketOrder.getId());
			logOrder.debug(marketOrder.toString());
			
			// Save the newly created 'marketOrder' in DB
			marketOrderService.save(marketOrder);
			
			notification.setMessage("Request accepted: OrderId " + (marketOrder).getId());
		}
		else {
			logRequest.info("Request rejected: " + postRequest.getUserId() + " " + postRequest.getStockTag() + " - invalid request properties");
			logRequest.debug(postRequest.toString());
			
			notification.updateMessage("Invalid request properties");
			return notification;
		}
		
		//
		if ( orderBook.matchOrder(marketOrder) ) {
			logOrder.info("Order matched: " + marketOrder.getId());
			notification.updateMessage("\nMATCHED");
			
			// Do some housekeeping and save order matches into the DB
			OrderMatch orderMatch = orderBook.getMatchedQueue().poll();
			if ( orderMatch != null ) {
				orderMatchService.save(orderMatch);
			}
			
			return notification;
		}
		else {
			// TODO Remove this! Use an Observable for notifications
			marketOrder.attachTo(orderBook);
			logOrder.info("Order not matched: " + marketOrder.getId());
			logOrder.info("Order " + marketOrder.getId() + " pacled in market " + marketOrder.getStockTag() + ", queued in " + marketOrder.getType() + " " + marketOrder.getOrderCondition() + " queue");
			
			if ( marketOrder.getOrderCondition() == PostOrderCondition.MARKET ) {
				marketOrder.setStatus(OrderStatus.ERROR);
				
				logOrder.error(marketOrder.getOrderCondition() + " order " + marketOrder.getId() + " not matched. Discarded");
				notification.updateMessage("\nNOT MATCHED");
			}
			
		} // if ( matchOrder(postOrder) )
		
		return notification;
		
	} // end addPostOrder
	
} // end class OrderBookService
