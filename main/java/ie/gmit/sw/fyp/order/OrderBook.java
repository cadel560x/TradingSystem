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
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import ie.gmit.sw.fyp.me.LimitOrder;
import ie.gmit.sw.fyp.me.Match;
import ie.gmit.sw.fyp.me.MarketOrder;
//import ie.gmit.sw.fyp.me.PostOrder;
import ie.gmit.sw.fyp.me.PostRequest;
//import ie.gmit.sw.fyp.notification.Notification;
import ie.gmit.sw.fyp.me.StopLossOrder;




public class OrderBook {
//	Fields
	private String stockTag;
	private Map<Float, Queue<LimitOrder>> buyLimitOrders;
	private Map<Float, Queue<LimitOrder>> sellLimitOrders;
	
	private Map<Float, Queue<StopLossOrder>> buyStopLossOrders;
	private Map<Float, Queue<StopLossOrder>> sellStopLossOrders;
	
	private BlockingQueue<Match> matchedQueue;
	
	
	
	
//	Constructor
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

	public BlockingQueue<Match> getMatchedQueue() {
		return matchedQueue;
	}




//	Methods
	public PostRequest checkRequest(PostRequest postRequest) throws InstantiationException {
		List<String> listProperties = new ArrayList<>(Arrays.asList("userId", "stockTag", "type", "condition", "price", "volume", "partialFill"));
		
		// Factory pattern
		switch(postRequest.getCondition()) {
			case STOPLOSS:
				listProperties.add("stopPrice");
			case LIMIT:
				listProperties.add("expirationTime");
				break;
			case MARKET:
				break;
		} // end switch
		
		if ( ! postRequest.checkProperties(listProperties) ) {
			throw new InstantiationException("Invalid request properties");
		}
		
		return postRequest;
		
	} // PostRequest
	
	
	public MarketOrder createOrder(PostRequest postRequest) {
		MarketOrder marketOrder = null;
		
		// Factory pattern		
		switch(postRequest.getCondition()) {
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
		
		return marketOrder;
		
	} // end createOrder(PostRequest postRequest)
	
	
	public MarketOrder createOrder(MarketOrder otherMarketOrder) {
//		PostOrder postOrder = null;
		
		// Factory pattern
		if ( otherMarketOrder instanceof MarketOrder ) {
			otherMarketOrder = new MarketOrder(otherMarketOrder);
		}
		else if ( otherMarketOrder instanceof LimitOrder ) {
			otherMarketOrder = new LimitOrder((LimitOrder)otherMarketOrder);
		}
		else if ( otherMarketOrder instanceof StopLossOrder ) {
			otherMarketOrder = new StopLossOrder((StopLossOrder)otherMarketOrder);
		}
		
		return otherMarketOrder;
		
	} // end createOrder(PostOrder otherPostOrder)
	
	
//	public void place(PostOrder postOrder) {
//		ConcurrentSkipListMap<Float, Queue<PostOrder>> queue;
//		
//		// MatchOrder are not placed
//		if ( !(postOrder instanceof MarketOrder) ) {
//			if ( postOrder.isBuy() ) {
//				queue = (ConcurrentSkipListMap<Float, Queue<PostOrder>>) this.buyOrders;
//				
//			}
//			else {
//				queue = (ConcurrentSkipListMap<Float, Queue<PostOrder>>) this.sellOrders;
//			}
//			
//			postOrder.setStatus(OrderStatus.ACCEPTED);
//			
//			Queue<PostOrder> nodeOrders = queue.get(postOrder.getPrice());
//			if ( queue.get(postOrder.getPrice()) == null ) {
//				nodeOrders = new ConcurrentLinkedQueue<>();
//			}
//			nodeOrders.offer(postOrder);
//			
//			queue.put(postOrder.getPrice(), nodeOrders);
//			
//			if ( postOrder instanceof StopLossOrder ) {
//				// Do StopLossOrder stuff...
//				ConcurrentSkipListMap<Float, Queue<PostOrder>> offerOrders = (ConcurrentSkipListMap<Float, Queue<PostOrder>>) this.buyOrders;
//			}
//			
//		} // end if ( !(postOrder instanceof MatchOrder) )
//		
//	} // end place(PostOrder postOrder)
	
	
	public boolean matchOrder(MarketOrder marketOrder) {
		ConcurrentSkipListMap<Float, Queue<LimitOrder>> offerOrders = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) this.buyLimitOrders;
		Entry<Float, Queue<LimitOrder>> bestOfferEntry = offerOrders.lastEntry();
		MarketOrder bestOffer;
		Match match;
		
		//
		if ( marketOrder.isBuy() ) {
			offerOrders = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) this.sellLimitOrders;
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
			
			if ( offerOrders == this.sellLimitOrders ) {
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
		if ( marketOrder.matches(bestOffer) ) {
			marketOrder.setStatus(OrderStatus.MATCHED);
			bestOffer.setStatus(OrderStatus.MATCHED);
			
			match = new Match(marketOrder, bestOffer);
			
			// A way to say that 'postOrder' and 'bestOffer' don't have the same volume of shares
			if ( marketOrder.getVolume() != match.getFilledShares() && bestOffer.getVolume() != match.getFilledShares() ) {
				MarketOrder spawnPostOrder = null;
				
				if ( marketOrder.getVolume() > bestOffer.getVolume() ) {
					marketOrder.setStatus(OrderStatus.PARTIALLYMATCHED);
					spawnPostOrder = this.createOrder(marketOrder);
				}
				else if ( marketOrder.getVolume() < bestOffer.getVolume() ) {
					bestOffer.setStatus(OrderStatus.PARTIALLYMATCHED);
					spawnPostOrder = this.createOrder(bestOffer);
				}
				//
				spawnPostOrder.setVolume(match.getFilledShares());
//				this.place(spawnPostOrder);
				spawnPostOrder.attachTo(this);
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
