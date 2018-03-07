package ie.gmit.sw.fyp.me;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;




@Service
public class OrderBookService {
	private Map<Float, Order> buyQueue;
	private Map<Float, Order> sellQueue;
	private BlockingQueue<Pair> matchedQueue;
	
	
	
	
	public OrderBookService() {
		buyQueue = new TreeMap<>();
		sellQueue = new TreeMap<>();
		matchedQueue = new LinkedBlockingQueue<>();
	}




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




	public Notification addOrder(OrderRequest orderRequest) {
		Notification notification = new Notification("NACK");
		Order order;
		
		if (checkOrderRequest(orderRequest)) {
			order = new Order(orderRequest);
		}
		else {
			return notification;
		}
		
		if ( matchOrder(order) ) {			
			
			notification.setMessage("Order Result=Matched\nOrder UUID: " + order.getOrderId().toString());
			
			return notification;
		}
		else { 
//			if ( order.orderType.equalsIgnoreCase("buy") ) {
//				buyQueue.put(Float.parseFloat(order.price), order);
//			}
//			else {
//				sellQueue.put(Float.parseFloat(order.price), order);
//			}
			if ( order.orderType == PostOrderType.BUY  ) {
				buyQueue.put(order.price, order);
			}
			else {
				sellQueue.put(order.price, order);
			}
		}
		
		notification.setMessage("ACK");
		return notification;
		
	}

	private boolean checkOrderRequest(OrderRequest orderRequest) {
		return true;
		
	}
	
	
	private boolean matchOrder(Order order) {
		TreeMap<Float, Order> queue;
		Pair pair;
		
		try {
//			if ( order.orderType.equalsIgnoreCase("buy") ) {
			if ( order.orderType == PostOrderType.BUY ) {
				queue = ((TreeMap<Float, Order>)sellQueue);
				Float bestSell = queue.firstKey();
//				if ( bestSell <= Float.parseFloat(order.price) ) {
				if ( bestSell <= order.price ) {
					pair = new Pair(order, queue.get(bestSell));
					matchedQueue.offer(pair);
					
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
//				if ( bestBuy >= Float.parseFloat(order.price) ) {
				if ( bestBuy >= order.price ) {
					pair = new Pair(queue.get(bestBuy), order);
					matchedQueue.offer(pair);
					
					queue.remove(bestBuy);
					return true;
				}
				else {
					return false;
				}
			}
		} catch ( NumberFormatException | NoSuchElementException e ) {
			System.err.println(e.toString() + ": First order created for stock AAPL");
			return false;
		}
		
	}
	
}
