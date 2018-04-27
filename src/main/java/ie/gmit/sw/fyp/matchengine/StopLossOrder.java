package ie.gmit.sw.fyp.matchengine;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.persistence.Entity;

import ie.gmit.sw.fyp.model.OrderBook;
import ie.gmit.sw.fyp.model.OrderStatus;




@Entity
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
		super(stopLossOrder);

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
	public boolean matches(LimitOrder other) {
		// A match is done between two orders of opposite type
		if ( ! this.isPartialFill() ) {
			// if volumes match ...
//			if ( (int)this.properties.get("volume") != (int)other.properties.get("volume") ) {
			if ( this.getVolume() != other.getVolume() ) {
				return false;
			}
			
		} // end if ( (boolean)this.properties.get("partialFill") )
		
		if ( ( this.getStopPrice() > other.getPrice() ) && ( this.isBuy() ) ) {
			return false;
		}
		else if ( ( this.getStopPrice() < other.getPrice() ) && ( this.isSell() ) ) {
			return false;		
		} // end if ( this.isBuy() ) && ( thisPrice > otherPrice ) - else if ( this.isSell() ) && ( thisPrice < otherPrice )
		// If we are here is because the prices are equal or better depending on 'this'
		
		// Anything else is a match
		return true;
		
	} // end match(PostOrder other) 
	
	
	public void attachTo(OrderBook orderBook) {
		super.attachTo(orderBook);
		
		ConcurrentSkipListMap<Float, Queue<StopLossOrder>> orderMap =  (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) orderBook.getSellStopLoss();
		
		if ( this.isBuy() ) {
			orderMap = (ConcurrentSkipListMap<Float, Queue<StopLossOrder>>) orderBook.getBuyStopLoss();
		}
		
		this.setStatus(OrderStatus.ACCEPTED);
		
		Queue<StopLossOrder> nodeOrders = orderMap.get(this.getStopPrice());
		if ( nodeOrders == null ) {
			nodeOrders = new ConcurrentLinkedQueue<>();
		}
		nodeOrders.offer(this);
		
		orderMap.put(this.getStopPrice(), nodeOrders);
		
	} // end attachTo(OrderBook orderBook)

	
	@Override
	public String toString() {
		return "StopLossOrder [properties=" + properties + "]";
		
	} // end toString()

} // end class PostOrder
