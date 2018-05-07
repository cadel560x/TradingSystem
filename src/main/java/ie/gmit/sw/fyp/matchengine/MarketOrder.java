package ie.gmit.sw.fyp.matchengine;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import ie.gmit.sw.fyp.model.Order;
import ie.gmit.sw.fyp.model.OrderBook;
import ie.gmit.sw.fyp.model.OrderStatus;




@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class MarketOrder extends PostEntity implements Order, PostOrder {
//	Fields
	@Id
	private String Id;
	
	@NotNull
	@PastOrPresent
	private Instant timestamp;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	
	
//	Constructors
	public MarketOrder() {
		initOrder();
		
	}

	public MarketOrder(PostRequest postRequest) {
		this.setUserId(postRequest.getUserId());
		this.setStockTag(postRequest.getStockTag());
		this.setType(postRequest.getType());
		this.setOrderCondition(postRequest.getOrderCondition());
		this.setVolume(postRequest.getVolume());
		this.setPartialFill(postRequest.isPartialFill());
		
		initOrder();
	}
	
	public MarketOrder(MarketOrder otherMarketOrder) {
		this.Id = otherMarketOrder.getId();
		this.timestamp = otherMarketOrder.getTimestamp();
		this.status = otherMarketOrder.getStatus();
		
		this.setUserId(otherMarketOrder.getUserId());
		this.setStockTag(otherMarketOrder.getStockTag());
		this.setType(otherMarketOrder.getType());
		this.setOrderCondition(otherMarketOrder.getOrderCondition());
		this.setVolume(otherMarketOrder.getVolume());
		this.setPartialFill(otherMarketOrder.isPartialFill());

	}
	
	
	
	
//	Accessors and mutators
	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}
	
	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
	
	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	
	
	
//	Implemented Abstract Methods
	public void attachTo(OrderBook orderBook) {
		// Do nothing because MarketOrder doesn't attach to any OrderBook
		
	}
	
	
	
	
//	Methods
	public void initOrder() {
		if ( this.Id == null || this.Id.equals("") ) {
			this.setId( UUID.randomUUID().toString() );
			this.setTimestamp( Instant.now() );
			this.setStatus(OrderStatus.CREATED );
		}
		
	} // end initOrder
	

	public boolean matches(LimitOrder other) {
		// In 'MarketOrder' what matters is the volume of shares. The price is already accepted
		if ( ! this.isPartialFill() && ! other.isPartialFill() ) {
			// Both must have the same share volume
			if ( this.getVolume() != other.getVolume() ) {
				return false;
			}
			
		}
		else if ( ! this.isPartialFill() && other.isPartialFill() ) {
			// 'other' must have the same or more shares than 'this'
			if ( this.getVolume() > other.getVolume() ) {
				return false;
			}
		}
		else if ( this.isPartialFill() && ! other.isPartialFill() ) {
			// 'this' must have the same or more shares than 'other'
			if ( this.getVolume() < other.getVolume() ) {
				return false;
			}
		} // end if - else if - else if
		
//		properties.put("price", other.getPrice());
		
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
			
		return true;
		
	} // end equals(Object obj)

	
	@Override
	public String toString() {
		return "MarketOrder [Id=" + Id + ", timestamp=" + timestamp + ", status=" + status + ", getUserId()="
				+ getUserId() + ", getStockTag()=" + getStockTag() + ", getType()=" + getType()
				+ ", getOrderCondition()=" + getOrderCondition() + ", getVolume()=" + getVolume() + ", isPartialFill()="
				+ isPartialFill() + "]";
	}

} // end class PostOrder
