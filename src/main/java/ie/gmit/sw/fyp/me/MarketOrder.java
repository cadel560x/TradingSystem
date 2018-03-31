package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import ie.gmit.sw.fyp.order.Order;
import ie.gmit.sw.fyp.order.OrderBook;
import ie.gmit.sw.fyp.order.OrderStatus;




public class MarketOrder extends PostEntity implements Order, PostOrder {
//	Fields
	
	
	
	
//	Constructors
	public MarketOrder() {
		this.properties = new HashMap<>();
		
		initOrder();
	}

	public MarketOrder(PostRequest postRequest) {
		properties = postRequest.getProperties();
		
		initOrder();
	}
	
	public MarketOrder(MarketOrder MarketOrder) {
		this.properties = new HashMap<>(MarketOrder.getProperties());

	}
	
	
	
	
//	Accessors and mutators
	
	
	
	
//	Delegated Methods
	public String getId() {
		return (String) properties.get("Id");
	}

	public void setId(String Id) {
		properties.put("Id", Id);
	}

	public Timestamp getTimestamp() {
		return (Timestamp) properties.get("timestamp");
	}

	public void setTimestamp(Timestamp timestamp) {
		properties.put("timestamp", timestamp);
	}

	public OrderStatus getStatus() {
		return (OrderStatus) properties.get("status");
	}

	public void setStatus(OrderStatus status) {
		properties.put("status", status);
	}
	
	
	
	
//	Implemented Abstract Methods
	public void attachTo(OrderBook orderBook) {
		// Do nothing because MarketOrder doesn't attach to any OrderBook
		
	}
	
	
	
	
	//	Methods
	public void initOrder() {
		this.setId( UUID.randomUUID().toString() );
		this.setTimestamp( new Timestamp(System.currentTimeMillis()) );
		this.setStatus(OrderStatus.CREATED );
		
	} // end initOrder
	

	public boolean matches(MarketOrder other) {
		// A match is done between two orders of opposite type
		if ( ! this.isPartialFill() ) {
			// if volumes match ...
			if ( this.getVolume() != other.getVolume() ) {
				return false;
			}
			
		} // end if ( (boolean)this.properties.get("partialFill") )
		
		this.setPrice(other.getPrice());
		
		// Anything else is a match
		return true;
		
	} // end match(MarketOrder other) 
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		MarketOrder other = (MarketOrder) obj;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} 
		else {
			if (! this.getId().equals(other.getId()) ) {
				return false;
			}
			
			if (! this.getUserId().equals(other.getUserId()) ) {
				return false;
			}

			if ( this.getType() != other.getType() ) {
				return false;
			}
			
			if (! this.getStockTag().equals(other.getStockTag())) {
				return false;
			}
			
		}
		return true;
		
	} // end equals(Object obj)
	
	
	@Override
	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
//		return result;
		return (int) this.getPrice() * 10000;
		
	} // end hashCode()

} // end class PostOrder
