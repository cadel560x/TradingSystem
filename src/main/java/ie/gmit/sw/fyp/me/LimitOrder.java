package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.persistence.Entity;

import ie.gmit.sw.fyp.order.OrderBook;
import ie.gmit.sw.fyp.order.OrderStatus;




@Entity
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
	
//	public LimitOrder(StopLossOrder stopLossOrder) {
//		// TODO Removes stop-loss attribute
//
//	}
	
	
	
	
//	Accessors and mutators

	
	
	
//	Delegated Methods
	public float getPrice() {
		return (float) properties.get("price");
	}

	public void setPrice(float price) {
		if ( price <= 0 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		
		properties.put("price", Float.parseFloat(String.format("%.4f", price)));
	}
	
	public Timestamp getExpirationTime() {
		return (Timestamp) properties.get("expirationTime");
	}

	public void setExpirationTime(Timestamp expirationTime) {
		if ( expirationTime.before(new Date()) ) {
			throw new IllegalArgumentException("Expiration time older than current time");
		}
		properties.put("expirationTime", expirationTime);
	}
	
	
	
	
//	Methods
	@Override
	public boolean matches(LimitOrder other) {
		// A match is done between two orders of opposite type
		if ( ! this.isPartialFill() ) {
			// if volumes match ...
//			if ( (int)this.properties.get("volume") != (int)other.properties.get("volume") ) {
			if ( this.getVolume() != other.getVolume() ) {
				return false;
			}
			
		} // end if ( (boolean)this.properties.get("partialFill") )
		
		if ( ( this.getPrice() < other.getPrice() ) && ( this.isBuy() ) ) {
			return false;
		}
		else if ( ( this.getPrice() > other.getPrice() ) && ( this.isSell() ) ) {
			return false;		
		}
		
		// If we are here is because the prices are equal or better depending on 'this'
		// Anything else is a match
		return true;
		
	} // end match(MarketOrder other) 
	
	
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
	
	
	@Override
	public int hashCode() {
		return (int) (this.getPrice() * 10000);
		
	} // end hashCode()

	
	@Override
	public String toString() {
		return "LimitOrder [properties=" + properties + "]";
		
	} // end toString()

} // end class PostOrder
