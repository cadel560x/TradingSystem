package ie.gmit.sw.fyp.services;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.MarketOrder;
import ie.gmit.sw.fyp.matchengine.OrderMatch;
import ie.gmit.sw.fyp.matchengine.PostOrderType;
import ie.gmit.sw.fyp.matchengine.PostRequest;
import ie.gmit.sw.fyp.matchengine.StopLossOrder;
import ie.gmit.sw.fyp.model.OrderBook;
import ie.gmit.sw.fyp.model.OrderStatus;
import ie.gmit.sw.fyp.notification.Notification;
import ie.gmit.sw.fyp.repositories.OrderBookRepository;




@Service
public class OrderBookService {
//	Fields
	@Autowired
	private OrderMatchService orderMatchService;
	
	@Autowired
	private MarketOrderService marketOrderService;
	
	@Autowired
	private LimitOrderService limitOrderService;
	
	@Autowired
	private StopLossOrderService stopLossOrderService;
	
	private Map<String, OrderBook> orderBooks;
	private static OrderBookRepository orderBookRepository;
	
//	Data members
	private final Logger logOrder = LoggerFactory.getLogger("ie.gmit.sw.fyp.OrderBookService");
	private final Logger logRequest = LoggerFactory.getLogger("ie.gmit.sw.fyp.request");
	private final Logger logMatch = LoggerFactory.getLogger("ie.gmit.sw.fyp.match");
	
	
	
	
//  Constructors
	public OrderBookService() {
		
	}
	
	
	
	
//	Accessors and mutators
	@Autowired
	public void setOrderBookRepository(OrderBookRepository orderBookRepository) {
		OrderBookService.orderBookRepository = orderBookRepository;
	}
	
	public Map<String, OrderBook> getOrderBooks() {
		return orderBooks;
	}




//	Methods
	@PostConstruct
	private void initService() {
		// Get the list of order books
		Iterable<OrderBook> orderBookList = this.findAll();
		
		// Java8 way to convert 'List' into 'Map'
		orderBooks = ((Collection<OrderBook>) orderBookList).stream().collect(
                Collectors.toMap(OrderBook::getStockTag, orderBook -> orderBook));
		
		// Populate the order queues with unmatched queues
		if ( ! orderBooks.isEmpty() ) {
			for ( OrderBook orderBook: orderBooks.values() ) {
				// Find expired limit orders and invalid them
				Iterable<String> expiredLimitOrderIds = limitOrderService.findExpired();
				for ( String Id : expiredLimitOrderIds ) {
					limitOrderService.updateByIdStatusExpired(Id);
				}
				
				// Find expired stop loss orders and invalid them
				expiredLimitOrderIds = stopLossOrderService.findExpired();
				for ( String Id : expiredLimitOrderIds ) {
					stopLossOrderService.updateByIdStatusExpired(Id);
				}
				
				// Recreate limit orders 
				Iterable<LimitOrder> limitOrderList = limitOrderService.findByStockTagAndStatusAccepted(orderBook.getStockTag());
				for ( LimitOrder limitOrder: limitOrderList ) {
					limitOrder.attachTo(orderBook);
				}
				
				// Recreate stop loss orders 
				Iterable<StopLossOrder> stopLossOrderList = stopLossOrderService.findByStockTagAndStatusAccepted(orderBook.getStockTag());
				for ( StopLossOrder stopLossOrder: stopLossOrderList ) {
					stopLossOrder.attachTo(orderBook);
				}
				
			} // end for ( OrderBook orderBook: orderBooks.values() )
			
		} // end if ( ! orderBooks.isEmpty() )
		
	} // end initialize()
	
	
	public Notification addPostOrder(String stockTag, PostRequest postRequest) {
		Notification notification = new Notification("REJECTED: ");
		MarketOrder marketOrder;
		
		//Get 'orderBook'
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
		
		// Do the match
		LimitOrder bestOption = orderBook.matchOrder(marketOrder);
		
		while ( bestOption != null && bestOption.hasExpired() ) {
			// Remove order from market
			bestOption.setStatus(OrderStatus.EXPIRED);
			marketOrderService.updateByIdStatusExpired(bestOption);
			
			// Poll the 'bestOption' and prune the queues
			orderBook.pollOrder(bestOption);
			orderBook.pruneOrderQueue(bestOption);
			logOrder.debug(bestOption.getStatus() + " order " + bestOption.getId() + " dequeued from " + bestOption.getStockTag() + " " + bestOption.getType() + " " + bestOption.getOrderCondition() +  " queue");
			
			bestOption = orderBook.matchOrder(marketOrder);
			
		} // end while ( bestOption != null && bestOption.hasExpired() )
		
		if ( bestOption != null ) {
			// Poll the 'bestOption' and rune the queues
			orderBook.pollOrder(bestOption);
			orderBook.pruneOrderQueue(bestOption);
			
			marketOrder.setStatus(OrderStatus.MATCHED);
			bestOption.setStatus(OrderStatus.MATCHED);
			logOrder.info("Order " + marketOrder.getId() + " matched with " + bestOption.getId());
			
			OrderMatch match = null;
			if( marketOrder.getType() == PostOrderType.SELL && bestOption.getType() == PostOrderType.BUY ) {
				match = new OrderMatch(marketOrder, bestOption);
				
				logMatch.info("Match created: " + match.getId());
				logMatch.debug("Match sell order: " + marketOrder.toString());
				logMatch.debug("Match buy order: " + bestOption.toString());
			}
			else if ( bestOption.getType() == PostOrderType.SELL && marketOrder.getType() == PostOrderType.BUY ) {
				match = new OrderMatch(bestOption, marketOrder);
				
				logMatch.info("Match created: " + match.getId());
				logMatch.debug("Match sell order: " + bestOption.toString());
				logMatch.debug("Match buy order: " + marketOrder.toString());
			}
			
			// A way to say that 'postOrder' and 'bestOption' don't have the same volume of shares
			if ( marketOrder.getVolume() != match.getFilledShares() && bestOption.getVolume() != match.getFilledShares() ) {
				MarketOrder spawnPostOrder = null;
				
				if ( marketOrder.getVolume() > bestOption.getVolume() ) {
					marketOrder.setStatus(OrderStatus.PARTIALLYMATCHED);
					logMatch.info("Order " + marketOrder.getId() + marketOrder.getStatus());
					logMatch.debug(marketOrder.toString());
					logOrder.debug(marketOrder.toString());
					
					spawnPostOrder = orderBook.createOrder(marketOrder);
				}
				else if ( marketOrder.getVolume() < bestOption.getVolume() ) {
					bestOption.setStatus(OrderStatus.PARTIALLYMATCHED);
					logMatch.info("Order " + bestOption.getId() + bestOption.getStatus());
					logMatch.debug(bestOption.toString());
					logOrder.debug(bestOption.toString());
					
					spawnPostOrder = orderBook.createOrder(bestOption);
				}
				//
				spawnPostOrder.setVolume(match.getRemainingShares());
				spawnPostOrder.attachTo(orderBook);
				logOrder.info("Order " + spawnPostOrder.getId() + " resubmitted into the market with " + spawnPostOrder.getVolume() + " shares");
				logMatch.info("Order " + spawnPostOrder.getId() + " resubmitted into the market with " + spawnPostOrder.getVolume() + " shares");
				
				match.setVolumes();
				logMatch.info("Setting match " + match.getId() + " filled shares to: " + match.getFilledShares());
				
			} // end if ( marketOrder.getVolume() != match.getFilledShares() && bestOption.getVolume() != match.getFilledShares() )
			
			// Update 'marketOrder' and 'bestOption' status in the respective DB table
			marketOrderService.save(marketOrder);
			marketOrderService.save(bestOption);
			
			//Update the 'matchedQueue'
			Queue<OrderMatch> matchedQueue = orderBook.getMatchedQueue();
			matchedQueue.offer(match);
			logMatch.info("Match " + match.getId() + " inserted into que match queue");
			
			// Do some housekeeping and save order matches into the DB
			OrderMatch orderMatch = orderBook.getMatchedQueue().poll();
			if ( orderMatch != null ) {
				orderMatchService.save(orderMatch);
			}
			
			notification.updateMessage("\nMATCHED");
			
			return notification;
		}
		else {
			marketOrder.attachTo(orderBook);
			logOrder.info("Order not matched: " + marketOrder.getId());
			
			if ( ! (marketOrder instanceof StopLossOrder) && ! (marketOrder instanceof LimitOrder) ) {
				marketOrder.setStatus(OrderStatus.REJECTED);
				logOrder.error(marketOrder.getOrderCondition() + " order " + marketOrder.getId() + " not matched. Discarded");
				marketOrderService.save(marketOrder);
				notification.updateMessage("\nNOT MATCHED");
			}
			else {
				marketOrderService.save(marketOrder);
				logOrder.info("Order " + marketOrder.getId() + " placed in market " + marketOrder.getStockTag() + ", queued in " + marketOrder.getType() + " " + marketOrder.getOrderCondition() + " queue");
				
			} // end if - else
			
		} // if ( bestOption != null ) - else
		
		return notification;
		
	} // end addPostOrder
	
	
	public boolean checkStockTag(String stockTag) {
		return orderBookRepository.existsById(stockTag);
		
	} // end checkStockTag
	
	
	public static boolean checkStockTagStatic(String stockTag) {
		return orderBookRepository.existsById(stockTag);
		
	} // end checkStockTagStatic
	
	
	public Iterable<OrderBook> findAll() {
		return orderBookRepository.findAll();
		
	} // end findAll
	
	
	public OrderBook findById(String stockTag) {
		return orderBookRepository.findById(stockTag).get();
		
	} // findById(String stockTag)
	
	
	public OrderBook save(OrderBook orderBook) {
		return orderBookRepository.save(orderBook);
		
	} // save(OrderBook orderBook)
	
} // end class OrderBookService
