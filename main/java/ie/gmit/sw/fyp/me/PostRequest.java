package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
import java.util.Date;
import java.util.HashMap;

//import java.sql.Timestamp;




public class PostRequest extends Request {
//	Fields
//	protected PostProperties postProperties;
//	private Map<String, Object> postProperties;
//	private OrderProperties properties;
	
	
	
	
//	Constructors
	public PostRequest() {
		requestProperties = new HashMap<>();
	}

//	public PostRequest(String userId, String stockTag, OrderType orderType, OrderClass orderClass, float price,
//			int volume, boolean partialFill, Timestamp expirationTime) {
//		super.setUserId(userId);
//		super.setStockTag(stockTag);
//		this.postProperties.setOrderType(orderType);
//		this.postProperties.setOrderClass(orderClass);
//		this.postProperties.setPrice(price);
//		this.postProperties.setVolume(volume);
//		this.postProperties.setPartialFill(partialFill);
//		this.postProperties.setExpirationTime(expirationTime);
//	}

	
	
	
//	Accesors and mutators
//	public Map<String, Object> getPostProperties() {
//		return postProperties;
//	}
//
//	public void setPostProperties(Map<String, Object> postProperties) {
//		this.postProperties = postProperties;
//	}
//	
//	public OrderType getOrderType() {
//		return (OrderType) postProperties.get("orderType");
//	}
//	
//	public void setOrderType(OrderType orderType) {
//		postProperties.put("orderType", orderType);
////		this.orderType = orderType;
//	}
//	
//	public OrderClass getOrderClass() {
//		return (OrderClass) postProperties.get("orderClass");
//	}
//	
//	public void setOrderClass(OrderClass orderClass) {
//		postProperties.put("orderClass", orderClass);
////		this.orderClass = orderClass;
//	}

	


//	Delegated methods
	public String getUserId() {
		return (String) requestProperties.get("userId");
	}

	public void setUserId(String userId) {
		if ( ! checkUserId(userId) ) {
			throw new IllegalArgumentException("Invalid user Id");
		}
		
		requestProperties.put("userId", userId);
//		this.userId = userId;
	}

	public String getStockTag() {
		return (String) requestProperties.get("stockTag");
	}

	public void setStockTag(String stockTag) {
		if ( ! checkStockTag(stockTag) ) {
			throw new IllegalArgumentException("Invalid stock tag");
		}
		
		requestProperties.put("stockTag", stockTag);
//		this.stockTag = stockTag;
	}
	
	public PostOrderType getType() {
		return (PostOrderType) requestProperties.get("type");
	}
	
	public void setType(PostOrderType type) {
		requestProperties.put("type", type);
//		this.orderType = orderType;
	}
	
	public OrderCondition getCondition() {
		return (OrderCondition) requestProperties.get("condition");
	}
	
	public void setCondition(OrderCondition condition) {
		requestProperties.put("condition", condition);
//		this.orderClass = orderClass;
	}
	
	public float getPrice() {
		return (float) requestProperties.get("price");
	}

	public void setPrice(float price) {
		if ( price <= 0 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
//		DecimalFormat df = new DecimalFormat("###.####");
//		df.format(price);
		
		requestProperties.put("price", Float.parseFloat(String.format("%.4f", price)));
//		this.price = price;
	}

	public int getVolume() {
		return (int) requestProperties.get("volume");
	}

	public void setVolume(int volume) {
		if ( volume <= 0 ) {
			throw new IllegalArgumentException("Invalid volume value");
		}
		requestProperties.put("volume", volume);
//		this.volume = volume;
	}

	public boolean isPartialFill() {
		return (boolean) requestProperties.get("partialFill");
	}

	public void setPartialFill(boolean partialFill) {
//		this.partialFill = partialFill;
		requestProperties.put("partialFill", partialFill);
	}

	public Timestamp getExpirationTime() {
		return (Timestamp) requestProperties.get("expirationTime");
	}

	public void setExpirationTime(Timestamp expirationTime) {
		if ( expirationTime.before(new Date()) ) {
			throw new IllegalStateException("Expiration time older than current time");
		}
		requestProperties.put("expirationTime", expirationTime);
//		this.expirationTime = expirationTime;
	}
	
	
	
//	Methods
	@Override
	public boolean checkProperties() {
//		List<String> postProperties = new ArrayList<>();
//		postProperties.add("userId");
//		postProperties.add("stockTag");
//		postProperties.add("type");
//		postProperties.add("condition");
//		postProperties.add("price");
//		postProperties.add("volume");
//		postProperties.add("partialFill");
//		postProperties.add("expirationTime");
		
		String[] postProperties = {"userId", "stockTag", "type", "condition", "price", "volume", "partialFill", "expirationTime"};
		
		for ( String postProperty: postProperties ) {
			if ( ! requestProperties.containsKey(postProperty) ) {
				return false;
			}
			
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
		
		} // end for
	
	return true;
	
	} // end checkProperties
	
	
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

} // end class PostOrderRequest
