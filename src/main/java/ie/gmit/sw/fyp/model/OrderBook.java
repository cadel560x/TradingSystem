package ie.gmit.sw.fyp.model;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.MarketOrder;
import ie.gmit.sw.fyp.matchengine.OrderMatch;
import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.matchengine.PostOrderType;
import ie.gmit.sw.fyp.matchengine.PostRequest;
import ie.gmit.sw.fyp.matchengine.StopLossOrder;



//@Component
@Entity
public class OrderBook {
//	Fields
	@Id
	private String stockTag;
	
	@Transient
	private final Map<Float, Queue<LimitOrder>> buyLimitOrders;
	@Transient
	private final Map<Float, Queue<LimitOrder>> sellLimitOrders;
	@Transient
	private final Map<Float, Queue<StopLossOrder>> buyStopLossOrders;
	@Transient
	private final Map<Float, Queue<StopLossOrder>> sellStopLossOrders;
	@Transient
	private final BlockingQueue<OrderMatch> matchedQueue;
	
	private String description;
	
//	Data members
	@Transient
	private final Logger logOrder = LoggerFactory.getLogger("ie.gmit.sw.fyp.OrderBook");
	
	
	
	
//	Constructors
	public OrderBook() {
		buyLimitOrders = new ConcurrentSkipListMap<>();
		sellLimitOrders = new ConcurrentSkipListMap<>();
		
		buyStopLossOrders = new ConcurrentSkipListMap<>();
		sellStopLossOrders = new ConcurrentSkipListMap<>();
		
		matchedQueue = new LinkedBlockingQueue<>();
		
	}
	
	public OrderBook(String stockTag) {
		this();
		this.stockTag = stockTag;
		
	}




//	Accessors and mutators
	public String getStockTag() {
		return stockTag;
	}

	public void setStockTag(String stockTag) {
		this.stockTag = stockTag;
	}
	
	@Transient
	public Map<Float, Queue<LimitOrder>> getBuyLimitOrders() {
		return buyLimitOrders;
	}

	@Transient
	public Map<Float, Queue<LimitOrder>> getSellLimitOrders() {
		return sellLimitOrders;
	}

	@Transient
	public Map<Float, Queue<StopLossOrder>> getBuyStopLoss() {
		return buyStopLossOrders;
	}

	@Transient
	public Map<Float, Queue<StopLossOrder>> getSellStopLoss() {
		return sellStopLossOrders;
	}

	@Transient
	public BlockingQueue<OrderMatch> getMatchedQueue() {
		return matchedQueue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

	
//	Methods
	public boolean checkRequest(PostRequest postRequest) throws IllegalArgumentException {
		List<String> listProperties = new ArrayList<>();
		
		PostOrderCondition condition = postRequest.getOrderCondition();
		if ( condition == null ) {
			throw new IllegalArgumentException("The 'condition' parameter must not be null or empty");
		}
		
		switch(postRequest.getOrderCondition()) {
			case STOPLOSS:
				listProperties.add("stopPrice");
			case LIMIT:
				listProperties.add("expirationTime");
				listProperties.add("price");
			case MARKET:
		} // end switch
		
		logOrder.debug("Inside checkRequest method: " + postRequest.toString());
		
		// If this fails, it throws an exception
		postRequest.checkProperties(listProperties);
		
		return true;
		
	} // end checkRequest(PostRequest postRequest)
	
	
	public MarketOrder createOrder(PostRequest postRequest) {
		MarketOrder marketOrder = null;
		
		// Factory pattern		
		switch(postRequest.getOrderCondition()) {
		case STOPLOSS:
			marketOrder = new StopLossOrder(postRequest);
			break;
		case LIMIT:
			marketOrder = new LimitOrder(postRequest);
			break;
		case MARKET:
			marketOrder = new MarketOrder(postRequest);
			break;
		} // end switch
		
		logOrder.debug(marketOrder.toString());
		
		return marketOrder;
		
	} // end createOrder(PostRequest postRequest)
	
	
	public MarketOrder createOrder(MarketOrder otherMarketOrder) {
		// Factory pattern
		switch(otherMarketOrder.getOrderCondition()) {
		case STOPLOSS:
			otherMarketOrder = new StopLossOrder((StopLossOrder)otherMarketOrder);
			break;
		case LIMIT:
			otherMarketOrder = new LimitOrder((LimitOrder)otherMarketOrder);
			break;
		case MARKET:
			otherMarketOrder = new MarketOrder(otherMarketOrder);
			break;
		} // end switch
		
		logOrder.debug(otherMarketOrder.toString());
		
		return otherMarketOrder;
		
	} // end createOrder(MarketOrder otherMarketOrder)
	
	
	public LimitOrder matchOrder(MarketOrder marketOrder) {
		ConcurrentSkipListMap<Float, Queue<StopLossOrder>> stopLossOrders = (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) this.buyStopLossOrders;
		ConcurrentSkipListMap<Float, Queue<LimitOrder>> offerOrders = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) this.buyLimitOrders;
		
		Entry<Float, Queue<StopLossOrder>> bestStopLossEntry = stopLossOrders.lastEntry();
		Entry<Float, Queue<LimitOrder>> bestOfferEntry = offerOrders.lastEntry();
		
		StopLossOrder bestStopLoss = null;
		LimitOrder bestOffer = null;
		
		StringBuilder collectionTypeString = new StringBuilder("STOPLOSS ");
		
		// Select the counterpart maps and queues
		if ( marketOrder.isBuy() ) {
			stopLossOrders = (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) this.sellStopLossOrders;
			offerOrders = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) this.sellLimitOrders;
			
			bestStopLossEntry = stopLossOrders.firstEntry();
			bestOfferEntry = offerOrders.firstEntry();	
		}
		
		
		if ( bestStopLossEntry == null ) {
			collectionTypeString.setLength(0);
			collectionTypeString.append("BUY");
			
			if ( stopLossOrders == this.sellStopLossOrders ) {
				collectionTypeString.setLength(0);
				collectionTypeString.append("SELL");
			}
			System.err.println(collectionTypeString + " collection in stock market " + stockTag + " is empty." );
			logOrder.warn(collectionTypeString + " collection in stock market " + stockTag + " is empty." );
			
		}
		else {
			synchronized ( stopLossOrders ) {
				bestStopLoss = bestStopLossEntry.getValue().peek();
				if ( bestStopLoss == null ) {
					// Order queue is empty, what's the point of keeping the order map entry? Let's remove it.
					stopLossOrders.remove(bestStopLossEntry.getKey());
					
					// Set the condition to cancel this match
					bestStopLossEntry = null;
					
				} // end if
				
			} // end synchronized
			if ( bestStopLoss != null ) {
//				else {
				logOrder.debug("bestStopLoss: " + bestStopLoss.toString());
				
			} // end if ( bestStopLoss == null ) - else
			
		} // end if ( bestStopLossEntry == null ) - else
		
		
		if ( bestOfferEntry == null ) {
			collectionTypeString.setLength(0);
			collectionTypeString.append("BUY");
			
			if ( offerOrders == this.sellLimitOrders ) {
				collectionTypeString.setLength(0);
				collectionTypeString.append("SELL");
			}
			System.err.println("LIMIT " + collectionTypeString + " collection in stock market " + stockTag + " is empty." );
			logOrder.warn("LIMIT " + collectionTypeString + " collection in stock market " + stockTag + " is empty." );
			
		}
		else {
			synchronized ( offerOrders ) {
				bestOffer = bestOfferEntry.getValue().peek();
				if ( bestOffer == null ) {
					// Order queue is empty, what's the point of keeping the order map entry? Let's remove it.
					synchronized (offerOrders) {
						offerOrders.remove(bestOfferEntry.getKey());
					}
					
					// Set the condition to cancel this match
					bestOfferEntry = null;
					
				} // end if
				
			} // end synchronized
			if ( bestOffer != null ) {
//				else {
				logOrder.debug("bestOffer: " + bestOffer.toString());
				
			} // end  if ( bestOffer == null ) - else 
			
		} // end if ( bestOfferEntry == null )
		
		if ( bestOfferEntry == null && bestStopLossEntry == null ) {
			logOrder.warn("No offers in market (empty market?)");
			
			return null;
		}
		
		LimitOrder bestOption = null;
		
		// Try to match to limit orders first
		if ( marketOrder.matches(bestOffer) ) {
			bestOption = bestOffer;
			
			logOrder.debug("bestOption: " + bestOption.toString());
		}
		else if ( bestStopLoss != null &&  ( marketOrder instanceof LimitOrder ) ) {
			LimitOrder limitOrder = (LimitOrder)marketOrder;
			if ( bestStopLoss.matches(limitOrder) ) {
				bestOption = bestStopLoss;
				
				logOrder.debug("bestOption: " + bestOption.toString());
			}
		} // if - else if
			
		String user = marketOrder.getUserId();
		if ( bestOption != null && user.equals(bestOption.getUserId()) ) {
			logOrder.debug("Cannot match order from the same user");
			bestOption = null;
		}
		
		return bestOption;
		
	} // end matchOrder(MarketOrder marketOrder)
	
	
	public void pruneOrderQueue(LimitOrder limitOrder) {
		Queue<? extends LimitOrder> orderQueue = null;
		
		String stockTag = limitOrder.getStockTag();
		PostOrderType type = limitOrder.getType();
		
		try {
			orderQueue = this.getLimitOrderQueue(limitOrder);
			if ( orderQueue == null ) {
				throw new NullPointerException("Order queue is null");
			}
			
			// Maintain the StopLossOrder queue
			if ( limitOrder instanceof StopLossOrder ) {
				if (  orderQueue.isEmpty() ) {
					float stopPrice = ((StopLossOrder) limitOrder).getStopPrice();;
					synchronized (orderQueue) {
						Map<Float, Queue<StopLossOrder>> orderMap = this.getStopLossOrderMap((StopLossOrder) limitOrder);
						orderMap.remove(stopPrice);
					}
					logOrder.warn(type + " STOPLOSS queue at " + stopPrice + " removed from market " + stockTag);
					
				} // end if
				
				// Make a 'LimitOrder' from this 'StopLossOrder'
				StopLossOrder stopLossOrder = (StopLossOrder) limitOrder;
				LimitOrder limitOrderClone = new LimitOrder(stopLossOrder);
				
				// Now switch to the LimitOrdeQueue
				orderQueue = this.getLimitOrderQueue(limitOrderClone);
				if ( orderQueue == null ) {
					throw new NullPointerException("Order queue is null");
				}
				
			} // end if
			
			// Maintain the LimitOrder queue
			if (  orderQueue.isEmpty() ) {
				float price = ((LimitOrder) limitOrder).getPrice();
				synchronized (orderQueue) {
					Map<Float, Queue<LimitOrder>> orderMap = this.getLimitOrderMap(limitOrder);
					orderMap.remove(price);
				}
				
				logOrder.warn(type + " LIMIT queue at " + price + " removed from market " + stockTag);
	
			} // end if
		}
		catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} // try - catch
		
	} // end pruneOrderQueue(LimitOrder limitOrder)
	
	
	public void pollOrder(LimitOrder limitOrder) {
		Queue<? extends LimitOrder> orderQueue = this.getLimitOrderQueue(limitOrder);
		boolean result;
		try {
			String msg;
			String stockTag = limitOrder.getStockTag();
			PostOrderType type = limitOrder.getType();
			PostOrderCondition condition = limitOrder.getOrderCondition();
			float price = ( condition == PostOrderCondition.LIMIT )? limitOrder.getPrice(): ((StopLossOrder) limitOrder).getStopPrice();
			
			synchronized (orderQueue)  {
				result = orderQueue.remove(limitOrder); // IT HAS TO BE 'remove' and not 'poll'
				// Read below
			}
			
			if ( ! result ) {
				msg = "Order " + limitOrder.getId() + " not removed from queue " + limitOrder.getType() + " LIMIT at price " + limitOrder.getPrice();
				logOrder.error(msg);
				throw new IllegalAccessError(msg);
			}
			logOrder.debug("Order " + limitOrder.getId() + " polled from " + type + " " + condition + " queue at " + price + " from market " + stockTag);
			
			if ( limitOrder instanceof StopLossOrder ) {
				StopLossOrder stopLossOrder = (StopLossOrder) limitOrder;
				
				// Make a 'LimitOrder' from this 'StopLossOrder'
				LimitOrder limitOrderClone = new LimitOrder(stopLossOrder);
				
				// Switch queue to 'LimitOrder' queue
				orderQueue = this.getLimitOrderQueue(limitOrderClone);
				synchronized (orderQueue)  {
				    result = orderQueue.remove(limitOrder); // IT HAS TO BE 'remove' and not 'poll'
				    // There are some cases that the order to be removed is behind the first element of the queue.
				    // Remember, we are here trying to remove a 'StopLossOrder' from a 'LimitOrder' queue.
				    // Case: let 'StopLossOrder' and 'LimitOrder' and have the same nominal price, and 'StopLossOrder'
				    //       is after 'LimitOrder' because it arrived later. If 'StopLossOrder' is selected 'bestOption'
				    //       when trying to dequeue from its 'LimitOrder' queue, 'poll' is going to get the first order
				    //       that is not 'StopLoss'. The first order is a 'LimitOrder'!
				} // end synchronized
//				if ( ! polledOrder.equals(limitOrder) ) {
				if ( ! result ) {
//					throw new IllegalAccessError("Different order polled from the queue: polledOrder: " + polledOrder.getId() + " limitOder: " + limitOrder.getId() );
					msg = "Order " + limitOrder.getId() + " not removed from queue " + limitOrder.getType() + " LIMIT at price " + limitOrder.getPrice();
					logOrder.error(msg);
					throw new IllegalAccessError(msg);
				}
				
				logOrder.debug("Order " + limitOrder.getId() + " polled from " + type + " LIMIT queue at " + limitOrderClone.getPrice() + " from market " + stockTag);
				
			} // end if
		}
		catch ( NullPointerException npe ) {
			npe.printStackTrace();
			throw new IllegalArgumentException();
		} 
		catch ( IllegalAccessError e ) {
//			e.printStackTrace();
			System.out.println(e.getMessage());
		} // try - catch - catch
		
	} // end pollOrder
	
	
	public Map<Float, Queue<StopLossOrder>> getStopLossOrderMap(StopLossOrder stopLossOrder) {
		if ( stopLossOrder.getType() == PostOrderType.SELL ) {
			return this.sellStopLossOrders;
		}
		
		return this.buyStopLossOrders;
		
	} // end getStopLossOrderMap(StopLossOrder stopLossOrder)
	
	
	public Map<Float, Queue<LimitOrder>> getLimitOrderMap(LimitOrder limitOrder) {
		if ( limitOrder.getType() == PostOrderType.SELL ) {
			return this.sellLimitOrders;
		}
		
		return this.buyLimitOrders;
		
	} // end getLimitOrderMap(LimitOrder limitOrder)
	
	
	public Queue<StopLossOrder> getStopLossOrderQueue(StopLossOrder stopLossOrder) {
		Map<Float, Queue<StopLossOrder>> stopLossOrderMap = this.getStopLossOrderMap(stopLossOrder);
		return stopLossOrderMap.get(stopLossOrder.getStopPrice());
		
	} // end getStopLossOrderQueue(StopLossOrder stopLossOrder)
	
	
	public Queue<? extends LimitOrder> getLimitOrderQueue(LimitOrder limitOrder) {
		if ( limitOrder instanceof StopLossOrder ) {
			return this.getStopLossOrderQueue((StopLossOrder) limitOrder);
		}
		
		Map<Float, Queue<LimitOrder>> limitOrderMap = this.getLimitOrderMap(limitOrder);
		return limitOrderMap.get(limitOrder.getPrice());
		
	} // end getStopLossOrderQueue(LimitOrder limitOrder)
	
} // end class OrderBook
