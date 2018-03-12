package ie.gmit.sw.fyp.me;

//import java.sql.Timestamp;
//import java.text.DecimalFormat;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Date;
//import java.util.Map;
//import ie.gmit.sw.fyp.order.Request;




public class StopLossRequest extends LimitRequest {
//	Fields
//	protected PostProperties postProperties;
//	private Map<String, Object> postProperties;
//	private OrderProperties properties;
	
	
	
	
//	Constructors
	public StopLossRequest() {
//		properties = new HashMap<>();
	}

	public StopLossRequest(PostRequest postRequest) {
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
//	public float getStopPrice() {
//		return (float) properties.get("stopPrice");
//	}
//	
//	public void setStopPrice(float stopPrice) {
//		if ( stopPrice <= 0 ) {
//			throw new IllegalArgumentException("Invalid price value");
//		}
//		
//		properties.put("stopPrice", stopPrice);
//	}

	
	
	
//	Methods
//	@Override
//	public boolean checkProperties() {
//		String[] postProperties = {"userId", "stockTag", "type", "condition", "price", "volume", "partialFill", "expirationTime", "stopPrice"};
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
