package ie.gmit.sw.fyp.me;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;




@Service
public class OrderBookService {
//	Fields
	private Map<Float, Order> buyQueue;
	private Map<Float, Order> sellQueue;
	private BlockingQueue<Pair> matchedQueue;
	
	
	
	
//	Constructor
	public OrderBookService() {
		buyQueue = new TreeMap<>();
		sellQueue = new TreeMap<>();
		matchedQueue = new LinkedBlockingQueue<>();
	}




//	Accessors and mutators
	public Map<Float, Order> getBuyQueue() {
		return buyQueue;
	}

	public void setBuyQueue(Map<Float, Order> buyQueue) {
		this.buyQueue = buyQueue;
	}

	public Map<Float, Order> getSellQueue() {
		return sellQueue;
	}

	public void setSellQueue(Map<Float, Order> sellQueue) {
		this.sellQueue = sellQueue;
	}
	
	public BlockingQueue<Pair> getMatchedQueue() {
		return matchedQueue;
	}




//	Methods
	public Notification addPostOrder(PostRequest postRequest) {
		Notification notification = new Notification("NACK");
		PostOrder postOrder;
		
		
		if ( postRequest.checkProperties() ) {
			postOrder = new PostOrder(postRequest);
		}
		else {
			return notification;
		}
		
		if ( matchOrder(postOrder) ) {			
			
			notification.setMessage("Order Result=Matched\nOrder UUID: " + postOrder.orderProperties.get("Id"));
			
			return notification;
		}
		else { 
			if ( postOrder.orderProperties.get("type") == PostOrderType.BUY  ) {
				buyQueue.put((float)postOrder.orderProperties.get("price"), postOrder);
			}
			else {
				sellQueue.put((float)postOrder.orderProperties.get("price"), postOrder);
			}
			
			postOrder.orderProperties.put("status", OrderStatus.ACCEPTED);
		} // if ( matchOrder(postOrder) )
		
		notification.setMessage("ACK");
		return notification;
		
	} // end addPostOrder
	
	
	private boolean matchOrder(PostOrder postOrder) {
		TreeMap<Float, Order> queue;
		Pair pair;
		
		try {
			if ( postOrder.orderProperties.get("type") == PostOrderType.BUY ) {
				queue = ((TreeMap<Float, Order>)sellQueue);
				Float bestSell = queue.firstKey();
				
				if ( bestSell <= (float)postOrder.orderProperties.get("price") ) {
					pair = new Pair(postOrder, queue.get(bestSell));
					
					matchedQueue.offer(pair);
					pair.getBuyOrder().orderProperties.put("status", OrderStatus.MATCHED);
					pair.getSellOrder().orderProperties.put("status", OrderStatus.MATCHED);
					
					queue.remove(bestSell);
					return true;
				}
				else {
					return false;
				}
			}
			else {
				queue = ((TreeMap<Float, Order>)buyQueue);
				Float bestBuy = queue.lastKey();
				
				if ( bestBuy >= (float)postOrder.orderProperties.get("price") ) {
					pair = new Pair(queue.get(bestBuy), postOrder);
					
					matchedQueue.offer(pair);
					pair.getSellOrder().orderProperties.put("status", OrderStatus.MATCHED);
					pair.getBuyOrder().orderProperties.put("status", OrderStatus.MATCHED);
					
					queue.remove(bestBuy);
					return true;
				}
				else {
					return false;
				}
			}
		} catch ( NoSuchElementException e ) {
			System.err.println(e.toString() + ": Counter list empty");
			return false;
		}
		
	}
	
} // end class OrderBookService
