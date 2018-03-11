package ie.gmit.sw.fyp.me;

//import java.text.DecimalFormat;
//import java.util.List;
//import java.util.Map;
import java.util.HashMap;
//import ie.gmit.sw.fyp.order.Request;




public class MatchRequest extends PostRequest {
//	Fields
//	protected PostProperties postProperties;
//	private Map<String, Object> postProperties;
//	private OrderProperties properties;
	
	
	
	
//	Constructors
	public MatchRequest() {
		properties = new HashMap<>();
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
//	public Map<String, Object> getProperties() {
//		return properties;
//	}
//
//	public void setProperties(Map<String, Object> properties) {
//		this.properties = properties;
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
//	public String getUserId() {
//		return (String) properties.get("userId");
//	}
//
//	public void setUserId(String userId) {
//		if ( ! checkUserId(userId) ) {
//			throw new IllegalArgumentException("Invalid user Id");
//		}
//		
//		properties.put("userId", userId);
////		this.userId = userId;
//	}
//
//	public String getStockTag() {
//		return (String) properties.get("stockTag");
//	}
//
//	public void setStockTag(String stockTag) {
//		if ( ! checkStockTag(stockTag) ) {
//			throw new IllegalArgumentException("Invalid stock tag");
//		}
//		
//		properties.put("stockTag", stockTag);
////		this.stockTag = stockTag;
//	}
//	
//	public PostOrderType getType() {
//		return (PostOrderType) properties.get("type");
//	}
//	
//	public void setType(PostOrderType type) {
//		properties.put("type", type);
////		this.orderType = orderType;
//	}
//	
//	public boolean isBuy() {
//		return ( properties.get("type") == PostOrderType.BUY );
//		
//	} // end isBuy()
//	
//	
//	public boolean isSell() {
//		return ! this.isBuy();
//		
//	} // end isSell()
//	
//	public PostOrderCondition getCondition() {
//		return (PostOrderCondition) properties.get("condition");
//	}
//	
//	public void setCondition(PostOrderCondition condition) {
//		properties.put("condition", condition);
////		this.orderClass = orderClass;
//	}
//	
//	public float getPrice() {
//		return (float) properties.get("price");
//	}
//
//	public void setPrice(float price) {
//		if ( price <= 0 ) {
//			throw new IllegalArgumentException("Invalid price value");
//		}
////		DecimalFormat df = new DecimalFormat("###.####");
////		df.format(price);
//		
//		properties.put("price", Float.parseFloat(String.format("%.4f", price)));
////		this.price = price;
//	}
//
//	public int getVolume() {
//		return (int) properties.get("volume");
//	}
//
//	public void setVolume(int volume) {
//		if ( volume <= 0 ) {
//			throw new IllegalArgumentException("Invalid volume value");
//		}
//		properties.put("volume", volume);
////		this.volume = volume;
//	}
//
//	public boolean isPartialFill() {
//		return (boolean) properties.get("partialFill");
//	}
//
//	public void setPartialFill(boolean partialFill) {
////		this.partialFill = partialFill;
//		properties.put("partialFill", partialFill);
//	}

	
	
	
//	Methods
	@Override
	public boolean checkProperties() {
		String[] postProperties = {"userId", "stockTag", "type", "condition", "price", "volume", "partialFill"};
		
		for ( String postProperty: postProperties ) {
			if ( ! properties.containsKey(postProperty) ) {
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
	
	
//	private boolean checkStockTag(String stockTag) {
////		TODO Implement a real stockTag checker. Using another service maybe?
//		if ( stockTag.equals("AAPL") ) {
//			return true;
//		}
//		else {
//			return false;
//		}
//		
//	} // end checkStockTag(String stockTag)
//	
//	
//	private boolean checkUserId(String userId) {
//		// TODO Implement a real userId checker. Using another service maybe?
//		if ( userId.equals("dfgjkaga9") ) {
//			return true;
//		}
//		else {
//			return false;
//		}
//		
//	} // end checkUserId(String userId)

} // end class PostOrderRequest
