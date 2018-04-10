package ie.gmit.sw.fyp.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import ie.gmit.sw.fyp.me.LimitOrder;
import ie.gmit.sw.fyp.me.Match;
import ie.gmit.sw.fyp.me.PostOrderCondition;
import ie.gmit.sw.fyp.me.MarketOrder;
import ie.gmit.sw.fyp.me.PostRequest;
import ie.gmit.sw.fyp.me.StopLossOrder;




public class OrderBook {
//	Fields
	private Map<Float, Queue<LimitOrder>> buyLimitOrders;
	private Map<Float, Queue<LimitOrder>> sellLimitOrders;
	
	private Map<Float, Queue<StopLossOrder>> buyStopLossOrders;
	private Map<Float, Queue<StopLossOrder>> sellStopLossOrders;
	
	private BlockingQueue<Match> matchedQueue;
	private String stockTag;
	
	
	
	
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
		List<String> listProperties = new ArrayList<>(Arrays.asList("userId", "stockTag", "type", "condition", "volume", "partialFill"));
		
		// Factory pattern
		switch(postRequest.getCondition()) {
			case STOPLOSS:
				listProperties.add("stopPrice");
			case LIMIT:
				listProperties.add("expirationTime");
				listProperties.add("price");
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
	
	
	public boolean matchOrder(MarketOrder marketOrder) {
		ConcurrentSkipListMap<Float, Queue<StopLossOrder>> stopLossOrders = (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) this.buyStopLossOrders;
		ConcurrentSkipListMap<Float, Queue<LimitOrder>> offerOrders = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) this.buyLimitOrders;
		
		Entry<Float, Queue<StopLossOrder>> bestStopLossEntry = stopLossOrders.lastEntry();
		Entry<Float, Queue<LimitOrder>> bestOfferEntry = offerOrders.lastEntry();
		
		StopLossOrder bestStopLoss = null;
		LimitOrder bestOffer = null;
		Match match;
		
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
			
//			return false;
		}
		else {	
			bestStopLoss = bestStopLossEntry.getValue().peek();
		}
		
		
		if ( bestOfferEntry == null ) {
			collectionType.setLength(0);
			collectionType.append("BUY");
			
			if ( offerOrders == this.sellLimitOrders ) {
				collectionType.setLength(0);
				collectionType.append("SELL");
			}
			System.err.println("LIMIT " + collectionType + " collection in stock market " + stockTag + " is empty." );
			
//			return false;
		}
		else {
			bestOffer = bestOfferEntry.getValue().peek();
		}
		
		if ( bestOfferEntry == null && bestStopLossEntry == null ) {
			return false;
		}
		
		LimitOrder bestOption = null;
		
			//
			if ( marketOrder.matches(bestOffer) ) {
				bestOption = bestOffer;
			}
			else if ( bestStopLoss != null &&  (marketOrder instanceof LimitOrder) ) {
				LimitOrder limitOrder = (LimitOrder)marketOrder;
				if ( bestStopLoss.matches(limitOrder) ) {
					bestOption = bestStopLoss;
				}
			} // if - else if
			
			
			if (bestOption != null) {
				marketOrder.setStatus(OrderStatus.MATCHED);
				bestOption.setStatus(OrderStatus.MATCHED);
				
				match = new Match(marketOrder, bestOption);
				
				// A way to say that 'postOrder' and 'bestOption' don't have the same volume of shares
				if ( marketOrder.getVolume() != match.getFilledShares() && bestOption.getVolume() != match.getFilledShares() ) {
					MarketOrder spawnPostOrder = null;
					
					if ( marketOrder.getVolume() > bestOption.getVolume() ) {
						marketOrder.setStatus(OrderStatus.PARTIALLYMATCHED);
						spawnPostOrder = this.createOrder(marketOrder);
					}
					else if ( marketOrder.getVolume() < bestOption.getVolume() ) {
						bestOption.setStatus(OrderStatus.PARTIALLYMATCHED);
						spawnPostOrder = this.createOrder(bestOption);
					}
					//
					spawnPostOrder.setVolume(match.getRemainingShares());
					spawnPostOrder.attachTo(this);
					match.setVolumes();
					
				} // end if ( postOrder.getVolume() != match.getFilledShares() && bestOption.getVolume() != match.getFilledShares() )
	
				
				//
				// TODO Change this for an Observable?? (Notification engine as a subscriber?)
				matchedQueue.offer(match);
				
				if ( bestOption.getCondition() == PostOrderCondition.LIMIT) {
					//
					bestOfferEntry.getValue().poll();
					if ( bestOfferEntry.getValue().isEmpty() ) {
						offerOrders.remove(bestOfferEntry.getKey());
					}
				}
				else if (bestOption.getCondition() == PostOrderCondition.STOPLOSS) {
					Collection<StopLossOrder>stopLossQueue = stopLossOrders.get( ((StopLossOrder)bestOption).getStopPrice() );
					Collection<LimitOrder>limitQueue = offerOrders.get(bestOption.getPrice());
					
					stopLossQueue.remove(bestOption);
					limitQueue.remove(bestOption);
					
					if ( stopLossQueue.isEmpty() ) {
						stopLossOrders.remove( ((StopLossOrder)bestOption).getStopPrice());
					}
					
					if (limitQueue.isEmpty() ) {
						offerOrders.remove(bestOption.getPrice());
					}
					
				} // end if - else
				
				return true;
				
			} // end if (bestOption != null)

		return false;
		
	} // end matchOrder(PostOrder postOrder)
	
} // end class OrderBook
