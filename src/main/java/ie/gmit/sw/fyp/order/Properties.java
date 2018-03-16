package ie.gmit.sw.fyp.order;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ie.gmit.sw.fyp.me.PostOrderCondition;
import ie.gmit.sw.fyp.me.PostOrderType;

public class Properties {
//	Fields
	protected Map<String, Object> properties = new HashMap<>();
//	protected float price;
//	protected int volume;
//	protected boolean partialFill;
//	protected Timestamp expirationTime;
	
	
	

//	Constructors
	public Properties() {
		
	}
	
	
	
	
//	Accessors and mutators
	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	public String getId() {
		return (String) properties.get("Id");
	}

	public void setId(String Id) {
		properties.put("Id", Id);
//		this.userId = userId;
	}
	
	public Timestamp getTimestamp() {
		return (Timestamp) properties.get("timestamp");
	}

	public void setTimestamp(Timestamp timestamp) {
		properties.put("timestamp", timestamp);
//		this.expirationTime = expirationTime;
	}
	
	public String getUserId() {
		return (String) properties.get("userId");
	}

	public void setUserId(String userId) {
		if ( ! checkUserId(userId) ) {
			throw new IllegalArgumentException("Invalid user Id");
		}
		
		properties.put("userId", userId);
//		this.userId = userId;
	}

	public String getStockTag() {
		return (String) properties.get("stockTag");
	}

	public void setStockTag(String stockTag) {
		if ( ! checkStockTag(stockTag) ) {
			throw new IllegalArgumentException("Invalid stock tag");
		}
		
		properties.put("stockTag", stockTag);
//		this.stockTag = stockTag;
	}
	
	public OrderStatus getStatus() {
		return (OrderStatus) properties.get("status");
	}
	
	public void setStatus(OrderStatus status) {
		properties.put("status", status);
//		this.orderType = orderType;
	}
	
	public PostOrderType getType() {
		return (PostOrderType) properties.get("type");
	}
	
	public void setType(PostOrderType type) {
		properties.put("type", type);
//		this.orderType = orderType;
	}
	
	public PostOrderCondition getCondition() {
		return (PostOrderCondition) properties.get("condition");
	}
	
	public void setCondition(PostOrderCondition condition) {
		properties.put("condition", condition);
//		this.orderClass = orderClass;
	}
	
	public float getPrice() {
		return (float) properties.get("price");
	}

	public void setPrice(float price) {
		if ( price <= 0 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		properties.put("price", price);
//		this.price = price;
	}

	public int getVolume() {
		return (int) properties.get("volume");
	}

	public void setVolume(int volume) {
		if ( volume <= 0 ) {
			throw new IllegalArgumentException("Invalid volume value");
		}
		properties.put("volume", volume);
//		this.volume = volume;
	}

	public boolean isPartialFill() {
		return (boolean) properties.get("partialFill");
	}

	public void setPartialFill(boolean partialFill) {
//		this.partialFill = partialFill;
		properties.put("partialFill", partialFill);
	}

	public Timestamp getExpirationTime() {
		return (Timestamp) properties.get("expirationTime");
	}

	public void setExpirationTime(Timestamp expirationTime) {
		if ( expirationTime.before(new Date()) ) {
			throw new IllegalStateException("Expiration time older than current time");
		}
		properties.put("expirationTime", expirationTime);
//		this.expirationTime = expirationTime;
	}
	
	
	
	
//	Methods
	public Object getProperty(String property) {
		return properties.get(property);
		
	} // end getProperty(String property)
	
	
//	public boolean checkProperties() {
//		for ( String key: properties.keySet() ) {
//			Object value = getProperty(key);
//			
//			if ( key.equals("price") || key.equals("volume") ) {
//				if ( (Float)value == 0 ) {
//					System.err.println("Invalid integer or float value");
//					return false;
//				}
//			}
//			else {
//				if ( value == null ) {
//					System.err.println("Invalid value");
//					return false;
//				}
//			} // if ( key.equals("price") || key.equals("volume") ) - else
//			
//		} // end for
//		
//		return true;
//		
//	} // end checkProperties
	
	
	private boolean checkStockTag(String stockTag) {
//		TODO Implement a real stockTag checker. Using another service maybe?
		if ( stockTag.equals("AAPL") ) {
			return true;
		}
		else {
			return false;
		}
		
	} // end checkStockTag(String stockTag)
	
	
	private boolean checkUserId(String userId) {
		// TODO Implement a real userId checker. Using another service maybe?
		if ( userId.equals("dfgjkaga9") ) {
			return true;
		}
		else {
			return false;
		}
		
	} // end checkUserId(String userId)
	
} // end abstract class PropertiesSpec 
