package ie.gmit.sw.fyp.me;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

import ie.gmit.sw.fyp.order.OrderBook;
import ie.gmit.sw.fyp.order.OrderStatus;




public class StopLossOrder extends LimitOrder implements PostOrder {
//	Fields
	
	
	
	
//	Constructors
	public StopLossOrder() {

	}
	
	public StopLossOrder(PostRequest stopLossRequest) {
		super(stopLossRequest);
	}
	
	public StopLossOrder(LimitOrder limitOrder) {
		super(limitOrder);

	}
	
	public StopLossOrder(StopLossOrder stopLossOrder) {

	}
	
	
	
	
//	Accessors and mutators


	
		
//	Delegated methods

	
	

//	Delegated Methods	
	public float getStopPrice() {
		return (float) properties.get("stopPrice");
	}
	
	public void setStopPrice(float stopPrice) {
		if ( stopPrice <= 0 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		
		properties.put("stopPrice", stopPrice);
	}
	
	
	
	
//	Methods
	public void attachTo(OrderBook orderBook) {
		super.attachTo(orderBook);
		
		ConcurrentSkipListMap<Float, Queue<StopLossOrder>> orderMap;
		
		if ( this.isBuy() ) {
			orderMap = (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) orderBook.getSellStopLoss();
			
		}
		else {
			orderMap = (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) orderBook.getBuyStopLoss();
		}
		
		this.setStatus(OrderStatus.ACCEPTED);
		
		Queue<StopLossOrder> nodeOrders = orderMap.get(this.getPrice());
		if ( orderMap.get(this.getPrice()) == null ) {
			nodeOrders = new ConcurrentLinkedQueue<>();
		}
		nodeOrders.offer(this);
		
		orderMap.put(this.getStopPrice(), nodeOrders);
		
	} // end attachTo(OrderBook orderBook)

} // end class PostOrder
