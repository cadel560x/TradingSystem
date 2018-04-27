package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import ie.gmit.sw.fyp.model.Order;
import ie.gmit.sw.fyp.model.OrderBook;
import ie.gmit.sw.fyp.model.OrderStatus;




@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class MarketOrder extends PostEntity implements Order, PostOrder {
//	Fields
	
	
	
	
//	Constructors
	public MarketOrder() {
		this.properties = new HashMap<>();
		
		initOrder();
	}

	public MarketOrder(PostRequest postRequest) {
		this.properties = postRequest.getProperties();
		
		initOrder();
	}
	
	public MarketOrder(MarketOrder MarketOrder) {
		this.properties = new HashMap<>(MarketOrder.getProperties());

	}
	
	
	
	
//	Accessors and mutators
	
	
	
	
//	Delegated Methods
	@Id
	public String getId() {
		return (String) properties.get("Id");
//		return "MarketOrder Id";
	}

	
	public void setId(String Id) {
		properties.put("Id", Id);
	}

	@Column(name="timestamp")
	public Timestamp getTimestamp() {
		return (Timestamp) properties.get("timestamp");
	}

	public void setTimestamp(Timestamp timestamp) {
		if ( timestamp.after(new Date()) ) {
			throw new IllegalArgumentException("Timestamp newer than current time");
		}
		properties.put("timestamp", timestamp);
	}

	@Enumerated(EnumType.STRING)
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
	

	public boolean matches(LimitOrder other) {
		// A match is done between two orders of opposite type
		if ( ! this.isPartialFill() ) {
			// if volumes match ...
			if ( this.getVolume() != other.getVolume() ) {
				return false;
			}
			
		} // end if ( (boolean)this.properties.get("partialFill") )
		
		properties.put("price", other.getPrice());
		
		// Anything else is a match
		return true;
		
	} // end matches(LimitOrder other) 
	
	
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
	public String toString() {
		return "MarketOrder [properties=" + properties + "]";
		
	} // end toString()

} // end class PostOrder
