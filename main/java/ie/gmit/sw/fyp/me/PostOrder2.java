package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import ie.gmit.sw.fyp.order.Order;
import ie.gmit.sw.fyp.order.OrderStatus;
import ie.gmit.sw.fyp.order.StockService;
import ie.gmit.sw.fyp.order.UserService;




public abstract class PostOrder2 implements Order {
//	Data members
	@Autowired
	private UserService userService;
	
	@Autowired
	private StockService stockService;
	
//	Fields
	protected Map<String, Object> properties;
	
	
	
	
//	Constructors
	public PostOrder2() {
		initOrder();
	}

	public PostOrder2(PostRequest postRequest) {
		properties = postRequest.getProperties();
		
		initOrder();
	}
	
	public PostOrder2(PostOrder2 postOrder) {
		this.properties = new HashMap<>(postOrder.getProperties());

	}
	
	
	
	
//	Accessors and mutators
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	
	
	
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
	
	public String getUserId() {
		return (String) properties.get("userId");
	}

	public void setUserId(String userId) {
		if ( ! userService.checkUserId(userId) ) {
			throw new IllegalArgumentException("Invalid user Id");
		}
		
		properties.put("userId", userId);
	}

	public String getStockTag() {
		return (String) properties.get("stockTag");
	}

	public void setStockTag(String stockTag) {
		if ( ! stockService.checkStockTag(stockTag) ) {
			throw new IllegalArgumentException("Invalid stock tag");
		}
		
		properties.put("stockTag", stockTag);
	}
	
	public PostOrderType getType() {
		return (PostOrderType) properties.get("type");
	}
	
	public void setType(PostOrderType type) {
		properties.put("type", type);
	}
	
	public PostOrderCondition getCondition() {
		return (PostOrderCondition) properties.get("condition");
	}
	
	public void setCondition(PostOrderCondition condition) {
		properties.put("condition", condition);
	}
	
	public float getPrice() {
		return (float) properties.get("price");
	}

	public void setPrice(float price) {
		if ( price <= 0 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		
		properties.put("price", Float.parseFloat(String.format("%.4f", price)));
	}

	public int getVolume() {
		return (int) properties.get("volume");
	}

	public void setVolume(int volume) {
		if ( volume <= 0 ) {
			throw new IllegalArgumentException("Invalid volume value");
		}
		properties.put("volume", volume);
	}

	public boolean isPartialFill() {
		return (boolean) properties.get("partialFill");
	}

	public void setPartialFill(boolean partialFill) {
		properties.put("partialFill", partialFill);
	}
	
	
	
//	Absctract methods
	public abstract void attachTo(Map<String, PostOrder2> orderMap);
	
	
	
	
//	Methods
	public void initOrder() {
		this.setId( UUID.randomUUID().toString() );
		this.setTimestamp( new Timestamp(System.currentTimeMillis()) );
		this.setStatus(OrderStatus.CREATED );
		
	} // end initOrder
	
	
	public boolean isBuy() {
		return ( properties.get("type") == PostOrderType.BUY );
		
	} // end isBuy()
	
	
	public boolean isSell() {
		return ! this.isBuy();
		
	} // end isSell()
	

	public boolean matches(PostOrder2 other) {
		// A match is done between two orders of opposite type
		if ( ! this.isPartialFill() ) {
			// if volumes match ...
//			if ( (int)this.properties.get("volume") != (int)other.properties.get("volume") ) {
			if ( this.getVolume() != other.getVolume() ) {
				return false;
			}
			
		} // end if ( (boolean)this.properties.get("partialFill") )
		
		if ( ( this.isBuy() ) && ( this.getPrice() > other.getPrice() ) ) {
			return false;
		}
		else if ( ( this.isSell() ) && ( this.getPrice() < other.getPrice() ) ) {
			return false;		
		} // end if ( this.isBuy() ) && ( thisPrice > otherPrice ) - else if ( this.isSell() ) && ( thisPrice < otherPrice )
		// If we are here is because the prices are equal or better depending on 'this'
		
		// Anything else is a match
		return true;
		
	} // end match(PostOrder other) 
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		PostOrder2 other = (PostOrder2) obj;
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
