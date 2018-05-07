package ie.gmit.sw.fyp.matchengine;

import java.time.Instant;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.persistence.Entity;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import ie.gmit.sw.fyp.model.OrderBook;
import ie.gmit.sw.fyp.model.OrderStatus;




@Entity
public class LimitOrder extends MarketOrder implements PostOrder {
//	Fields
	@NotNull
	@DecimalMin("0.0001")
	private float price;
	
	@NotNull
	@FutureOrPresent
	private Instant expirationTime;
	
	
	
	
//	Constructors
	public LimitOrder() {

	}

	public LimitOrder(PostRequest limitRequest) {
		super(limitRequest);
		
		this.price = limitRequest.getPrice();
		this.expirationTime = limitRequest.getExpirationTime();
		
	}
	
	public LimitOrder(LimitOrder otherLimitOrder) {
		super(otherLimitOrder);
		
		this.price = otherLimitOrder.getPrice();
		this.expirationTime = otherLimitOrder.getExpirationTime();
		
	}
	
	
	
	
//	Accessors and mutators
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	public Instant getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Instant expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	
	
	
//	Methods
	@Override
	public boolean matches(LimitOrder other) {
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
		return "LimitOrder [price=" + price + ", expirationTime=" + expirationTime + ", getId()=" + getId()
				+ ", getTimestamp()=" + getTimestamp() + ", getStatus()=" + getStatus() + ", getUserId()=" + getUserId()
				+ ", getStockTag()=" + getStockTag() + ", getType()=" + getType() + ", getOrderCondition()="
				+ getOrderCondition() + ", getVolume()=" + getVolume() + ", isPartialFill()=" + isPartialFill() + "]";
	}

} // end class PostOrder
