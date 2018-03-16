package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

import ie.gmit.sw.fyp.order.OrderBook;
import ie.gmit.sw.fyp.order.OrderStatus;



public class LimitOrder extends MarketOrder implements PostOrder {
//	Fields
	
	
	
	
//	Constructors
	public LimitOrder() {

	}

	public LimitOrder(PostRequest limitRequest) {
		super(limitRequest);
		
	}
	
	public LimitOrder(LimitOrder limitOrder) {
		super(limitOrder);
	}
	
	public LimitOrder(StopLossOrder stopLossOrder) {
		// TODO Removes stop-loss attribute

	}
	
	
	
	
//	Accessors and mutators

	
	
	
//	Delegated Methods
	public Timestamp getExpirationTime() {
		return (Timestamp) properties.get("expirationTime");
	}

	public void setExpirationTime(Timestamp expirationTime) {
		if ( expirationTime.before(new Date()) ) {
			throw new IllegalStateException("Expiration time older than current time");
		}
		properties.put("expirationTime", expirationTime);
	}
	
	
	
	
//	Methods
	@Override
	public void attachTo(OrderBook orderBook) {
		ConcurrentSkipListMap<Float, Queue<LimitOrder>> orderMap = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) orderBook.getSellLimitOrders();
		
		if ( this.isBuy() ) {
			orderMap = (ConcurrentSkipListMap<Float, Queue<LimitOrder>>) orderBook.getBuyLimitOrders();
		}
		
		this.setStatus(OrderStatus.ACCEPTED);
		
		Queue<LimitOrder> nodeOrders = orderMap.get(this.getPrice());
		if ( nodeOrders == null ) {
			nodeOrders = new ConcurrentLinkedQueue<>();
			orderMap.put(this.getPrice(), nodeOrders);
		}
		nodeOrders.offer(this);
		
	} // end attachTo(OrderBook orderBook)

} // end class PostOrder
