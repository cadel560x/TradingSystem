package ie.gmit.sw.fyp.order;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
//import java.util.NoSuchElementException;
//import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import ie.gmit.sw.fyp.me.LimitOrder;
import ie.gmit.sw.fyp.me.LimitRequest;
import ie.gmit.sw.fyp.me.Match;
import ie.gmit.sw.fyp.me.MarketOrder;
import ie.gmit.sw.fyp.me.MarketRequest;
import ie.gmit.sw.fyp.me.PostOrder;
import ie.gmit.sw.fyp.me.PostRequest;
//import ie.gmit.sw.fyp.notification.Notification;
import ie.gmit.sw.fyp.me.StopLossOrder;
import ie.gmit.sw.fyp.me.StopLossRequest;




public class OrderBook {
//	Fields
	private String stockTag;
	private Map<Float, Queue<PostOrder>> buyOrders;
	private Map<Float, Queue<PostOrder>> sellOrders;
	private BlockingQueue<Match> matchedQueue;
	
	
	
	
//	Constructor
	public OrderBook(String stockTag) {		
		this.stockTag = stockTag;
		buyOrders = new ConcurrentSkipListMap<>();
		sellOrders = new ConcurrentSkipListMap<>();
		matchedQueue = new LinkedBlockingQueue<>();
	}




//	Accessors and mutators
	public String getStockTag() {
		return stockTag;
	}

	public void setStockTag(String stockTag) {
		this.stockTag = stockTag;
	}
	
	public Map<Float, Queue<PostOrder>> getBuyOrders() {
		return buyOrders;
	}

	public void setBuyOrders(Map<Float, Queue<PostOrder>> buyOrders) {
		this.buyOrders = buyOrders;
	}

	public Map<Float, Queue<PostOrder>> getSellOrders() {
		return sellOrders;
	}

	public void setSellOrders(Map<Float, Queue<PostOrder>> sellOrders) {
		this.sellOrders = sellOrders;
	}
	
	public BlockingQueue<Match> getMatchedQueue() {
		return matchedQueue;
	}




//	Methods
	public PostRequest createRequest(PostRequest postRequest) {
		List<String> listProperties = new ArrayList<>(Arrays.asList("userId", "stockTag", "type", "condition", "price", "volume", "partialFill"));
		
		// Factory pattern
		switch(postRequest.getCondition()) {
			case STOPLOSS:
				postRequest = new StopLossRequest(postRequest);
				listProperties.addAll(Arrays.asList("expirationTime", "stopPrice"));
				break;
			case LIMIT:
				postRequest = new LimitRequest(postRequest);
				listProperties.add("expirationTime");
				break;
			case MARKET:
				postRequest = new MarketRequest(postRequest);
				break;
		} // end switch
		postRequest.setPropertiesList(listProperties);
		
		return postRequest;
		
	} // PostRequest
	
	
	public PostOrder createOrder(PostRequest postRequest) {
		PostOrder postOrder = null;
		
		// Factory pattern
		if ( postRequest instanceof MarketRequest ) {
			postOrder = new MarketOrder(postRequest);
		}
		else if ( postRequest instanceof LimitRequest ) {
			postOrder = new LimitOrder((LimitRequest)postRequest);
		}
		else if ( postRequest instanceof StopLossRequest ) {
			postOrder = new StopLossOrder((StopLossRequest)postRequest);
		}
		
		return postOrder;
		
	} // end createOrder(PostRequest postRequest)
	
	
	public PostOrder createOrder(PostOrder otherPostOrder) {
//		PostOrder postOrder = null;
		
		// Factory pattern
		if ( otherPostOrder instanceof MarketOrder ) {
			otherPostOrder = new MarketOrder((MarketOrder)otherPostOrder);
		}
		else if ( otherPostOrder instanceof LimitOrder ) {
			otherPostOrder = new LimitOrder((LimitOrder)otherPostOrder);
		}
		else if ( otherPostOrder instanceof StopLossOrder ) {
			otherPostOrder = new StopLossOrder((StopLossOrder)otherPostOrder);
		}
		
//		if ( otherPostOrder instanceof MatchOrder ) {
//			postOrder = new MatchOrder((MatchOrder)otherPostOrder);
//		}
//		else if ( otherPostOrder instanceof LimitOrder ) {
//			postOrder = new LimitOrder((LimitOrder)otherPostOrder);
//		}
//		else if ( otherPostOrder instanceof StopLossOrder ) {
//			postOrder = new StopLossOrder((StopLossOrder)otherPostOrder);
//		}
		
//		return postOrder;
		return otherPostOrder;
		
	} // end createOrder(PostRequest postRequest)
	
	
	public void place(PostOrder postOrder) {
		ConcurrentSkipListMap<Float, Queue<PostOrder>> queue;
		
		// MatchOrder are not placed
		if ( !(postOrder instanceof MarketOrder) ) {
			if ( postOrder.isBuy() ) {
				queue = (ConcurrentSkipListMap<Float, Queue<PostOrder>>) this.buyOrders;
				
			}
			else {
				queue = (ConcurrentSkipListMap<Float, Queue<PostOrder>>) this.sellOrders;
			}
			
			postOrder.setStatus(OrderStatus.ACCEPTED);
			
			Queue<PostOrder> nodeOrders = queue.get(postOrder.getPrice());
			if ( queue.get(postOrder.getPrice()) == null ) {
				nodeOrders = new ConcurrentLinkedQueue<>();
			}
			nodeOrders.offer(postOrder);
			
			queue.put(postOrder.getPrice(), nodeOrders);
			
			if ( postOrder instanceof StopLossOrder ) {
				// Do StopLossOrder stuff...
			}
			
		} // end if ( !(postOrder instanceof MatchOrder) )
		
	} // end place(PostOrder postOrder)
	
	
	public boolean matchOrder(PostOrder postOrder) {
		ConcurrentSkipListMap<Float, Queue<PostOrder>> offerOrders = (ConcurrentSkipListMap<Float, Queue<PostOrder>>) this.buyOrders;
//		ConcurrentSkipListMap<Float, Queue<PostOrder>> offerOrders;
//		ConcurrentSkipListMap<Float, PostOrder> queue;
		Entry<Float, Queue<PostOrder>> bestOfferEntry = offerOrders.lastEntry();
//		Entry<Float, Queue<PostOrder>> bestOfferEntry;
//		PostOrder bestOffer = bestOfferEntry.getValue().peek();
		PostOrder bestOffer;
		Match match;
		
		//
		if ( postOrder.isBuy() ) {
			offerOrders = (ConcurrentSkipListMap<Float, Queue<PostOrder>>) this.sellOrders;
//			queue = (ConcurrentSkipListMap<Float, PostOrder>) this.buyQueue;
			bestOfferEntry = offerOrders.firstEntry();
			
		}
//		else {
//			offerOrders = (ConcurrentSkipListMap<Float, Queue<PostOrder>>) this.buyOrders;
////			queue = (ConcurrentSkipListMap<Float, PostOrder>) this.sellQueue;
//			bestOfferEntry = offerOrders.lastEntry();
//		} // if - else ( postOrder.isBuy() )
		
		if ( bestOfferEntry == null ) {
			StringBuilder collectionType = new StringBuilder("BUY");
			
			if ( offerOrders == this.sellOrders ) {
				collectionType.setLength(0);
				collectionType.append("SELL");
			}
			System.err.println("Offering " + collectionType + " collection in stock market " + stockTag + " is empty." );
			
			return false;
		}
		bestOffer = bestOfferEntry.getValue().peek();
		
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
			
			// A way to say that 'postOrder' and 'bestOffer' don't have the same volume of shares
			if ( postOrder.getVolume() != match.getFilledShares() && bestOffer.getVolume() != match.getFilledShares() ) {
				PostOrder spawnPostOrder = null;
				
				if ( postOrder.getVolume() > bestOffer.getVolume() ) {
					postOrder.setStatus(OrderStatus.PARTIALLYMATCHED);
					spawnPostOrder = this.createOrder(postOrder);
				}
				else if ( postOrder.getVolume() < bestOffer.getVolume() ) {
					bestOffer.setStatus(OrderStatus.PARTIALLYMATCHED);
					spawnPostOrder = this.createOrder(bestOffer);
				}
				//
				spawnPostOrder.setVolume(match.getFilledShares());
				this.place(spawnPostOrder);
				match.setVolumes();
				
			} // end if ( postOrder.getVolume() != match.getFilledShares() && bestOffer.getVolume() != match.getFilledShares() )

			
			//
			// TODO Change this for an Observable?? (Notification engine as a subscriber?)
			matchedQueue.offer(match);
			
			//
			bestOfferEntry.getValue().poll();
			if ( bestOfferEntry.getValue().isEmpty() ) {
				offerOrders.remove(bestOfferEntry.getKey());
			}
//			else {
//				
//			}
			
			return true;
			
		} // end if ( bestOffer.matches(postOrder) )

		return false;
		
	} // end matchOrder(PostOrder postOrder)
	
} // end class OrderBook
