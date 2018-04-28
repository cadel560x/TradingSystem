package ie.gmit.sw.fyp.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.MarketOrder;
import ie.gmit.sw.fyp.matchengine.OrderMatch;
import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.matchengine.PostOrderType;
import ie.gmit.sw.fyp.matchengine.PostRequest;
import ie.gmit.sw.fyp.matchengine.StopLossOrder;
import ie.gmit.sw.fyp.model.OrderBook;
import ie.gmit.sw.fyp.model.OrderStatus;
import ie.gmit.sw.fyp.notification.Notification;




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
	
//	Data members
	private final Logger logOrder = LoggerFactory.getLogger("ie.gmit.sw.fyp.order");
	private final Logger logRequest = LoggerFactory.getLogger("ie.gmit.sw.fyp.request");
	private final Logger logMatch = LoggerFactory.getLogger("ie.gmit.sw.fyp.match");
	
	
	
	
//  Constructors
	public OrderBookService() {
		initialize();
	}
	
	
	
	
//	Accessors and mutators
	public Map<String, OrderBook> getOrderBooks() {
		return orderBooks;
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
		
		// Do the match
		LimitOrder bestOption = orderBook.matchOrder(marketOrder);
		while ( bestOption != null && bestOption.hasExpired() ) {
			// Remove order from market
			Queue<? extends LimitOrder> orderQueue = orderBook.getLimitOrderQueue(bestOption);
			orderQueue.poll();
			bestOption.setStatus(OrderStatus.EXPIRED);
			updateDBOrderStatus(bestOption);
			
			if ( orderQueue.isEmpty() ) {
				if ( bestOption instanceof StopLossOrder ) {
					Map<Float, Queue<StopLossOrder>> orderMap = orderBook.getStopLossOrderMap((StopLossOrder) bestOption);
					orderMap.remove(((StopLossOrder) bestOption).getStopPrice());
				}
				else {
					Map<Float, Queue<LimitOrder>> orderMap = orderBook.getLimitOrderMap(bestOption);
					orderMap.remove(((LimitOrder) bestOption).getPrice());
				} // end if ( bestOption instanceof StopLossOrder )
				
				bestOption = null;
			}
			else {
				bestOption = orderBook.matchOrder(marketOrder);
			} // end if ( orderQueue.isEmpty() )
			
		} // end while ( bestOption != null && bestOption.hasExpired() )
		
		if ( bestOption != null ) {
			OrderMatch match = null;
			marketOrder.setStatus(OrderStatus.MATCHED);
			bestOption.setStatus(OrderStatus.MATCHED);
			
			logOrder.info("Order " + marketOrder.getId() + " matched with " + bestOption.getId());
			
			
			if( marketOrder.getType() == PostOrderType.SELL && bestOption.getType() == PostOrderType.BUY ) {
				match = new OrderMatch(marketOrder, bestOption);
				
				logMatch.info("Match created: " + match.getId());
				logMatch.debug("Match sell order: " + marketOrder.toString());
				logMatch.debug("Match buy order" + bestOption.toString());
			}
			else if ( bestOption.getType() == PostOrderType.SELL && marketOrder.getType() == PostOrderType.BUY ) {
				match = new OrderMatch(bestOption, marketOrder);
				
				logMatch.info("Match created: " + match.getId());
				logMatch.debug("Match sell order: " + bestOption.toString());
				logMatch.debug("Match buy order" + marketOrder.toString());
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
	
			
			logOrder.info("Order matched: " + marketOrder.getId());
			notification.updateMessage("\nMATCHED");
			
			// Update 'marketOrder' status in the respective DB table
			this.updateDBOrderStatus(marketOrder);
			
			// Update 'bestOption' status in the respective DB table
			this.updateDBOrderStatus(bestOption);
			
			//Update the 'matchedQueue'
			Queue<OrderMatch> matchedQueue = orderBook.getMatchedQueue();
			matchedQueue.offer(match);
			
			logMatch.info("Match " + match.getId() + " inserted into que match queue");
			
			
			// Dequeue queues
			ConcurrentSkipListMap<Float, Queue<StopLossOrder>> stopLossOrders = (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) orderBook.getBuyStopLoss();
			ConcurrentSkipListMap<Float, Queue<LimitOrder>> offerOrders = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) orderBook.getBuyLimitOrders();
			Entry<Float, Queue<LimitOrder>> bestOfferEntry = offerOrders.lastEntry();
			
			// Selecting the correct queue
			if ( marketOrder.isBuy() ) {
				stopLossOrders = (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) orderBook.getSellStopLoss();
				
				offerOrders = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) orderBook.getSellLimitOrders();
				bestOfferEntry = offerOrders.firstEntry();
			}
			
			if ( bestOption.getOrderCondition() == PostOrderCondition.LIMIT) {
				bestOfferEntry.getValue().poll();
				logOrder.debug("Order " + bestOption.getId() + " dequeued from " + bestOption.getStockTag() + " market");
				
				if ( bestOfferEntry.getValue().isEmpty() ) {
					offerOrders.remove(bestOfferEntry.getKey());
					logOrder.debug(bestOption.getType() + " " + bestOption.getOrderCondition() + " queue at " + bestOfferEntry.getKey() + " removed from market " + stockTag);
				}
			}
			else if (bestOption.getOrderCondition() == PostOrderCondition.STOPLOSS) {
				Collection<StopLossOrder>stopLossQueue = stopLossOrders.get( ((StopLossOrder)bestOption).getStopPrice() );
				Collection<LimitOrder>limitQueue = offerOrders.get(bestOption.getPrice());
				
				stopLossQueue.remove(bestOption);
				logOrder.debug("Order " + bestOption.getId() + " dequeued from " + bestOption.getStockTag() + " " + bestOption.getType() + " " + bestOption.getOrderCondition() +  " queue");
				limitQueue.remove(bestOption);
				logOrder.debug("Order " + bestOption.getId() + " dequeued from " + bestOption.getStockTag() + " market");
				
				if ( stopLossQueue.isEmpty() ) {
					stopLossOrders.remove( ((StopLossOrder)bestOption).getStopPrice());
					logOrder.debug(bestOption.getType() + " " + bestOption.getOrderCondition() + " queue at " + bestOfferEntry.getKey() + " removed from market " + stockTag);
				}
				
				if (limitQueue.isEmpty() ) {
					offerOrders.remove(bestOption.getPrice());
					logOrder.debug(bestOption.getType() + " LIMIT queue at " + bestOfferEntry.getKey() + " removed from market " + stockTag);
				}
				
			} // end if ( bestOption.getCondition() == PostOrderCondition.LIMIT) - else
			
			
			// Do some housekeeping and save order matches into the DB
			OrderMatch orderMatch = orderBook.getMatchedQueue().poll();
			if ( orderMatch != null ) {
				orderMatchService.save(orderMatch);
			}
			
			return notification;
		}
		else {
			marketOrder.attachTo(orderBook);
			logOrder.info("Order not matched: " + marketOrder.getId());
			logOrder.info("Order " + marketOrder.getId() + " placed in market " + marketOrder.getStockTag() + ", queued in " + marketOrder.getType() + " " + marketOrder.getOrderCondition() + " queue");
			
			if ( marketOrder instanceof StopLossOrder ) {
				stopLossOrderService.updateByIdStatus(marketOrder.getId(), marketOrder.getStatus());
			}
			else if ( marketOrder instanceof LimitOrder ) {
				limitOrderService.updateByIdStatus(marketOrder.getId(), marketOrder.getStatus());
			}
			else {
				marketOrder.setStatus(OrderStatus.REJECTED);
				logOrder.error(marketOrder.getOrderCondition() + " order " + marketOrder.getId() + " not matched. Discarded");
				marketOrderService.updateByIdStatus(marketOrder.getId(), marketOrder.getStatus());
				notification.updateMessage("\nNOT MATCHED");
			}
			
		} // if ( matchOrder(postOrder) )
		
		return notification;
		
	} // end addPostOrder
	
	
	public void updateDBOrderStatus(MarketOrder marketOrder) {
		if ( marketOrder instanceof StopLossOrder ) {
			stopLossOrderService.updateByIdStatus(marketOrder.getId(), marketOrder.getStatus());
		}
		else if ( marketOrder instanceof LimitOrder ) {
			limitOrderService.updateByIdStatus(marketOrder.getId(), marketOrder.getStatus());
		}
		else {
			marketOrderService.updateByIdStatus(marketOrder.getId(), marketOrder.getStatus());
		}
		
	} // end updateDBOrderStatus(MarketOrder marketOrder)
	
} // end class OrderBookService
