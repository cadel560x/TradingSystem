package ie.gmit.sw.fyp.order;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.gmit.sw.fyp.me.LimitOrder;
import ie.gmit.sw.fyp.me.OrderMatch;
//import ie.gmit.sw.fyp.me.PostOrderCondition;
//import ie.gmit.sw.fyp.me.PostOrderType;
import ie.gmit.sw.fyp.me.MarketOrder;
import ie.gmit.sw.fyp.me.PostRequest;
import ie.gmit.sw.fyp.me.StopLossOrder;




public class OrderBook {
//	Fields
	private Map<Float, Queue<LimitOrder>> buyLimitOrders;
	private Map<Float, Queue<LimitOrder>> sellLimitOrders;
	
	private Map<Float, Queue<StopLossOrder>> buyStopLossOrders;
	private Map<Float, Queue<StopLossOrder>> sellStopLossOrders;
	
	private BlockingQueue<OrderMatch> matchedQueue;
	private String stockTag;
	
//	Data members
	private final Logger logOrder = LoggerFactory.getLogger("ie.gmit.sw.fyp.order");
//	private final Logger logMatch = LoggerFactory.getLogger("ie.gmit.sw.fyp.match");
	
	
	
	
//	Constructors
	public OrderBook() {
		
	}
	
	public OrderBook(String stockTag) {
		this.stockTag = stockTag;
		buyLimitOrders = new ConcurrentSkipListMap<>();
		sellLimitOrders = new ConcurrentSkipListMap<>();
		
		buyStopLossOrders = new ConcurrentSkipListMap<>();
		sellStopLossOrders = new ConcurrentSkipListMap<>();
		
		matchedQueue = new LinkedBlockingQueue<>();
	}




//	Accessors and mutators
	public String getStockTag() {
		return stockTag;
	}

	public void setStockTag(String stockTag) {
		this.stockTag = stockTag;
	}
	
	public Map<Float, Queue<LimitOrder>> getBuyLimitOrders() {
		return buyLimitOrders;
	}

	public void setBuyLimitOrders(Map<Float, Queue<LimitOrder>> buyLimitOrders) {
		this.buyLimitOrders = buyLimitOrders;
	}

	public Map<Float, Queue<LimitOrder>> getSellLimitOrders() {
		return sellLimitOrders;
	}

	public void setSellLimitOrders(Map<Float, Queue<LimitOrder>> sellLimitOrders) {
		this.sellLimitOrders = sellLimitOrders;
	}
	
	public Map<Float, Queue<StopLossOrder>> getBuyStopLoss() {
		return buyStopLossOrders;
	}

	public void setBuyStopLoss(Map<Float, Queue<StopLossOrder>> buyStopLossOrders) {
		this.buyStopLossOrders = buyStopLossOrders;
	}

	public Map<Float, Queue<StopLossOrder>> getSellStopLoss() {
		return sellStopLossOrders;
	}

	public void setSellStopLoss(Map<Float, Queue<StopLossOrder>> sellStopLossOrders) {
		this.sellStopLossOrders = sellStopLossOrders;
	}

	public BlockingQueue<OrderMatch> getMatchedQueue() {
		return matchedQueue;
	}




//	Methods
	public boolean checkRequest(PostRequest postRequest) {
		List<String> listProperties = new ArrayList<>(Arrays.asList("userId", "stockTag", "type", "condition", "volume", "partialFill"));
		
		switch(postRequest.getOrderCondition()) {
			case STOPLOSS:
				listProperties.add("stopPrice");
			case LIMIT:
				listProperties.add("expirationTime");
				listProperties.add("price");
			case MARKET:
		} // end switch
		
		logOrder.debug("Inside checkRequest method: " + postRequest.toString());
		
		if ( postRequest.checkProperties(listProperties) ) {
			return true;
		}
		
		return false;
		
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
		
		logOrder.info(marketOrder.toString());
		
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
		
		logOrder.info(otherMarketOrder.toString());
		
		return otherMarketOrder;
		
	} // end createOrder(MarketOrder otherMarketOrder)
	
	
	public LimitOrder matchOrder(MarketOrder marketOrder) {
		ConcurrentSkipListMap<Float, Queue<StopLossOrder>> stopLossOrders = (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) this.buyStopLossOrders;
		ConcurrentSkipListMap<Float, Queue<LimitOrder>> offerOrders = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) this.buyLimitOrders;
		
		Entry<Float, Queue<StopLossOrder>> bestStopLossEntry = stopLossOrders.lastEntry();
		Entry<Float, Queue<LimitOrder>> bestOfferEntry = offerOrders.lastEntry();
		
		StopLossOrder bestStopLoss = null;
		LimitOrder bestOffer = null;
		
		StringBuilder collectionType = new StringBuilder("STOPLOSS ");
		
		//
		if ( marketOrder.isBuy() ) {
			stopLossOrders = (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) this.sellStopLossOrders;
			offerOrders = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) this.sellLimitOrders;
			
			bestStopLossEntry = stopLossOrders.firstEntry();
			bestOfferEntry = offerOrders.firstEntry();	
		}
		
		
		if ( bestStopLossEntry == null ) {
			collectionType.append("BUY");
			
			if ( stopLossOrders == this.sellStopLossOrders ) {
				collectionType.append("SELL");
			}
			System.err.println(collectionType + " collection in stock market " + stockTag + " is empty." );
			logOrder.warn(collectionType + " collection in stock market " + stockTag + " is empty." );
			
		}
		else {	
			bestStopLoss = bestStopLossEntry.getValue().peek();
			
			logOrder.debug("bestStopLoss: " + bestStopLoss.toString());
		}
		
		
		if ( bestOfferEntry == null ) {
			collectionType.setLength(0);
			collectionType.append("BUY");
			
			if ( offerOrders == this.sellLimitOrders ) {
				collectionType.setLength(0);
				collectionType.append("SELL");
			}
			System.err.println("LIMIT " + collectionType + " collection in stock market " + stockTag + " is empty." );
			logOrder.warn("LIMIT " + collectionType + " collection in stock market " + stockTag + " is empty." );
			
		}
		else {
			bestOffer = bestOfferEntry.getValue().peek();
			
			logOrder.debug("bestOffer: " + bestOffer.toString());
		}
		
		if ( bestOfferEntry == null && bestStopLossEntry == null ) {
			logOrder.warn("No offers in market (empty market?)");
			
			return null;
		}
		
		LimitOrder bestOption = null;
		
		//
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
		
		return bestOption;
		
	} // end matchOrder(MarketOrder marketOrder)
	
} // end class OrderBook
