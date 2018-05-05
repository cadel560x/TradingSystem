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
		try {
			return (float) properties.get("stopPrice");
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			System.out.println("Invalid stop price value, defaulting to 0.0001");
			return 0.0001f;
		}
	}
	
	public void setStopPrice(float stopPrice) {
		if ( stopPrice <= 0 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		
		properties.put("stopPrice", stopPrice);
	}
	
	
	
	
//	Methods
	public boolean matches(LimitOrder other) {
		// Check match by nominal prices
		if ( super.matches(other) ) {
			return true;
		}
		// No luck?
		// Check match by stop loss prices
		else {
			// Against the 'other' nominal price
			if ( ( this.getStopPrice() > other.getPrice() ) && ( this.isBuy() ) ) {
				// Against 'other' stop loss price, if 'other' is a stop loss too
				if ( other instanceof StopLossOrder) {
					if ( ((StopLossOrder) other).getStopPrice() < this.getPrice() && other.isSell() ) {
						return false;
					}
				}
				else {
					return false;
				}
			}
			// Against the 'other' nominal price
			else if ( ( this.getStopPrice() < other.getPrice() ) && ( this.isSell() ) ) {
				// Against 'other' stop loss price, if 'other' is a stop loss too
				if ( other instanceof StopLossOrder) {
					if ( ( ((StopLossOrder) other).getStopPrice() > this.getPrice() ) && ( other.isBuy() ) ) {
						return false;
					}
				}
				else {
					return false;
				}
			} // end if - else if
			
		} // end if ( super.matches(other) ) - else
		
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
