package ie.gmit.sw.fyp.me;

//import java.sql.Timestamp;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Date;

//import ie.gmit.sw.fyp.order.Request;




public class LimitRequest extends PostRequest {
//	Fields
//	protected PostProperties postProperties;
//	private Map<String, Object> postProperties;
//	private OrderProperties properties;
	
	
	
	
//	Constructors
	public LimitRequest() {
//		properties = new HashMap<>();
	}
	
	public LimitRequest(PostRequest postRequest) {
		this.properties = postRequest.properties;
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

	


//	Delegated methods
//	public Timestamp getExpirationTime() {
//		return (Timestamp) properties.get("expirationTime");
//	}
//
//	public void setExpirationTime(Timestamp expirationTime) {
//		if ( expirationTime.before(new Date()) ) {
//			throw new IllegalStateException("Expiration time older than current time");
//		}
//		properties.put("expirationTime", expirationTime);
////		this.expirationTime = expirationTime;
//	}
	
	
	
//	Methods
//	@Override
//	public boolean checkProperties() {
//		String[] postProperties = {"userId", "stockTag", "type", "condition", "price", "volume", "partialFill", "expirationTime"};
//		
//		for ( String postProperty: postProperties ) {
//			if ( ! properties.containsKey(postProperty) ) {
//				return false;
//			}
//			
////			Object value = getProperty(key);
////			
////			if ( key.equals("price") || key.equals("volume") ) {
////				if ( (Float)value == 0 ) {
////					System.err.println("Invalid integer or float value");
////					return false;
////				}
////			}
////			else {
////				if ( value == null ) {
////					System.err.println("Invalid value");
////					return false;
////				}
////			} // if ( key.equals("price") || key.equals("volume") ) - else
//		
//		} // end for
//	
//	return true;
//	
//	} // end checkProperties

} // end class PostOrderRequest