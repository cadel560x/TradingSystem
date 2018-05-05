package ie.gmit.sw.fyp.matchengine;

import java.time.Instant;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.persistence.Entity;

import ie.gmit.sw.fyp.model.OrderBook;
import ie.gmit.sw.fyp.model.OrderStatus;




@Entity
//@Component
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
	
	
	
	
//	Accessors and mutators

	
	
	
//	Delegated Methods
	public float getPrice() {
		try {
			return (float) properties.get("price");
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			System.out.println("Invalid price value, defaulting to 0.0001");
			return 0.0001f;
		}
	}

	public void setPrice(float price) {
		if ( price <= 0 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		
		properties.put("price", Float.parseFloat(String.format("%.4f", price)));
	}
	
	public Instant getExpirationTime() {
		return (Instant) properties.get("expirationTime");
	}

	public void setExpirationTime(Instant expirationTime) {
		if ( expirationTime.isBefore(Instant.now()) ) {
			throw new IllegalArgumentException("Expiration time older than current time");
		}
		properties.put("expirationTime", expirationTime);
	}
	
	
	
	
//	Methods
	@Override
	public boolean matches(LimitOrder other) {
		// A match is done between two orders of opposite type
		if ( ! super.matches(other) ) {
				return false;
		}
		
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
		Map<Float, Queue<LimitOrder>> priceMap = orderBook.getLimitOrderMap(this);
		
		Queue<LimitOrder> ordersQueue = priceMap.get(this.getPrice());
		if ( ordersQueue == null ) {
			ordersQueue = new ConcurrentLinkedQueue<>();
			priceMap.put(this.getPrice(), ordersQueue);
		}
		ordersQueue.offer(this);
		
		this.setStatus(OrderStatus.ACCEPTED);
		
	} // end attachTo(OrderBook orderBook)
	
	
	public boolean hasExpired() {
		if (this.getExpirationTime().isBefore(Instant.now())) {
			return true;
		}
		
		return false;
	} // end hasExpired
	
	
	@Override
	public int hashCode() {
		return (int) (this.getPrice() * 10000);
		
	} // end hashCode()

	
	@Override
	public String toString() {
		return "LimitOrder [properties=" + properties + "]";
		
	} // end toString()

} // end class PostOrder
