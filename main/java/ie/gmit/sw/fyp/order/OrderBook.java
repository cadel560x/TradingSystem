package ie.gmit.sw.fyp.order;

import java.util.Map;
import java.util.Map.Entry;
//import java.util.NoSuchElementException;
//import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import ie.gmit.sw.fyp.me.Match;
import ie.gmit.sw.fyp.me.PostOrder;
//import ie.gmit.sw.fyp.me.PostRequest;
//import ie.gmit.sw.fyp.notification.Notification;




public class OrderBook {
//	Fields
	private String stockTag;
	private Map<Float, PostOrder> buyQueue;
	private Map<Float, PostOrder> sellQueue;
	private BlockingQueue<Match> matchedQueue;
	
	
	
	
//	Constructor
	public OrderBook(String stockTag) {
		this.stockTag = stockTag;
		buyQueue = new ConcurrentSkipListMap<>();
		sellQueue = new ConcurrentSkipListMap<>();
		matchedQueue = new LinkedBlockingQueue<>();
	}




//	Accessors and mutators
	public String getStockTag() {
		return stockTag;
	}

	public void setStockTag(String stockTag) {
		this.stockTag = stockTag;
	}
	
	public Map<Float, PostOrder> getBuyQueue() {
		return buyQueue;
	}

	public void setBuyQueue(Map<Float, PostOrder> buyQueue) {
		this.buyQueue = buyQueue;
	}

	public Map<Float, PostOrder> getSellQueue() {
		return sellQueue;
	}

	public void setSellQueue(Map<Float, PostOrder> sellQueue) {
		this.sellQueue = sellQueue;
	}
	
	public BlockingQueue<Match> getMatchedQueue() {
		return matchedQueue;
	}




//	Methods
	public void place(PostOrder postOrder) {
		ConcurrentSkipListMap<Float, PostOrder> queue;
		
		if ( postOrder.isBuy() ) {
			queue = (ConcurrentSkipListMap<Float, PostOrder>) this.buyQueue;
		}
		else {
			queue = (ConcurrentSkipListMap<Float, PostOrder>) this.sellQueue;
		}
		
		postOrder.setStatus(OrderStatus.ACCEPTED);
		queue.put(postOrder.getPrice(), postOrder);
		
		
	} // end place(PostOrder postOrder)
	
	
	public boolean matchOrder(PostOrder postOrder) {
		ConcurrentSkipListMap<Float, PostOrder> offerQueue;
//		ConcurrentSkipListMap<Float, PostOrder> queue;
		Entry<Float, PostOrder> bestOfferEntry;
		PostOrder bestOffer = null;
		Match match;
		
		//
		if ( postOrder.isBuy() ) {
			offerQueue = (ConcurrentSkipListMap<Float, PostOrder>) this.sellQueue;
//			queue = (ConcurrentSkipListMap<Float, PostOrder>) this.buyQueue;
			bestOfferEntry = offerQueue.firstEntry();
			
		}
		else {
			offerQueue = (ConcurrentSkipListMap<Float, PostOrder>) this.buyQueue;
//			queue = (ConcurrentSkipListMap<Float, PostOrder>) this.sellQueue;
			bestOfferEntry = offerQueue.lastEntry();
		} // if - else ( postOrder.isBuy() )
		
		if ( bestOfferEntry == null ) {
			StringBuilder queueType = new StringBuilder("BUY");
			
			if ( offerQueue == this.sellQueue ) {
				queueType.setLength(0);
				queueType.append("SELL");
			}
			System.err.println("Offering " + queueType + " queue in stock market " + stockTag + " is empty." );
			
			return false;
		}
		bestOffer = bestOfferEntry.getValue();
		
//		try {
//			bestOffer = bestOfferEntry.getValue();
//		} catch (Exception e) {
//			StringBuilder queueType = new StringBuilder("BUY");
//			
//			if ( offerQueue == this.sellQueue ) {
//				queueType.setLength(0);
//				queueType.append("SELL");
//			}
//			
//			System.err.println(e.toString() + ": Offering " + queueType + " queue in stock market " + stockTag + " is empty." );
//		}
		
		//
		if ( bestOffer.matches(postOrder) ) {
			postOrder.setStatus(OrderStatus.MATCHED);
			bestOffer.setStatus(OrderStatus.MATCHED);
			
			match = new Match(postOrder, bestOffer);
			
			//
			// TODO Change this for an Observable?? (Notification engine as a subscriber?)
			matchedQueue.offer(match);
			offerQueue.remove(bestOfferEntry.getKey());
			
			return true;
		} // end if ( bestOffer.matches(postOrder) )

		return false;
		
	} // end matchOrder(PostOrder postOrder)
	
} // end class OrderBook
