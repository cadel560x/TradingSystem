package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
//import java.util.List;
import java.util.Date;
import java.util.Map;

import ie.gmit.sw.fyp.order.Request;




//public abstract class PostRequest extends Request {
public class PostRequest implements Request {
//	Fields
//	protected PostProperties postProperties;
//	private Map<String, Object> postProperties;
	protected Map<String, Object> properties;
//	private List<String> propertiesList;
	
	
	
	
//	Constructors
	public PostRequest() {
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
	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	@Override
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
//	public List<String> getPropertiesList() {
//		return propertiesList;
//	}
//
//	public void setPropertiesList(List<String> propertiesList) {
//		this.propertiesList = propertiesList;
//	}

	


	//	Delegated methods
	@Override
	public String getUserId() {
		return (String) properties.get("userId");
	}

	@Override
	public void setUserId(String userId) {
		if ( ! checkUserId(userId) ) {
			throw new IllegalArgumentException("Invalid user Id");
		}
		
		properties.put("userId", userId);
	}

	@Override
	public String getStockTag() {
		return (String) properties.get("stockTag");
	}

	@Override
	public void setStockTag(String stockTag) {
		if ( ! checkStockTag(stockTag) ) {
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
//		DecimalFormat df = new DecimalFormat("###.####");
//		df.format(price);
		
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
	
	public float getStopPrice() {
		return (float) properties.get("stopPrice");
	}
	
	public void setStopPrice(float stopPrice) {
		if ( stopPrice <= 0 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		
		properties.put("stopPrice", stopPrice);
	}
	
	
	
	
//	Methods
	@Override
	public boolean checkProperties(Iterable<String> propertiesList) {
//		List<String> postProperties = new ArrayList<>(Arrays.asList("userId", "stockTag", "type", "condition", "price", "volume", "partialFill"));
//		String [] postProperties = {"userId", "stockTag", "type", "condition", "price", "volume", "partialFill"};

//		switch(this.getCondition()) {
//			case STOPLOSS:
//				postProperties.add("stopPrice");
//			case LIMIT:
//				postProperties.add("expirationTime");
//				break;
//			case MARKET:
//				break;
//		} // end switch
		
		for ( String postProperty: propertiesList ) {
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
	
	
	public boolean isBuy() {
		return ( properties.get("type") == PostOrderType.BUY );
		
	} // end isBuy()
	
	public boolean isSell() {
		return ! this.isBuy();
		
	} // end isSell()
	
	
	protected boolean checkStockTag(String stockTag) {
//		TODO Implement a real stockTag checker. Using another service maybe?
		if ( stockTag.equals("AAPL") ) {
			return true;
		}
		else {
			return false;
		}
		
	} // end checkStockTag(String stockTag)
	
	
	protected boolean checkUserId(String userId) {
		// TODO Implement a real userId checker. Using another service maybe?
		if ( userId.equals("dfgjkaga9") ) {
			return true;
		}
		else {
			return false;
		}
		
	} // end checkUserId(String userId)

} // end class PostOrderRequest
