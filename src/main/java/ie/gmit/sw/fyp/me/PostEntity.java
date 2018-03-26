package ie.gmit.sw.fyp.me;

import java.util.Map;

//import org.springframework.beans.factory.annotation.Autowired;

import ie.gmit.sw.fyp.order.StockService;
import ie.gmit.sw.fyp.order.Transaction;
import ie.gmit.sw.fyp.order.UserService;




public abstract class PostEntity implements Transaction {
//	Data members
//	Singleton pattern
//	private UserService userService = UserService.getInstance();
//	private StockService stockService = StockService.getInstance();
	
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private StockService stockService;
	
//	Fields
	protected Map<String, Object> properties;
	
	
	
	
//	Accesors and mutators
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	
	
	
//	Implemented Abstract Methods
	public String getUserId() {
		return (String) properties.get("userId");
	}

	@Override
	public void setUserId(String userId) {
		if ( ! UserService.checkUserId(userId) ) {
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
		if ( ! StockService.checkStockTag(stockTag) ) {
			throw new IllegalArgumentException("Invalid stock tag");
		}
		
		properties.put("stockTag", stockTag);
	}
	
	
	
	
//	Delegated Methods
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
	
	
	
	
//	Methods
	public boolean isBuy() {
		return ( properties.get("type") == PostOrderType.BUY );
		
	} // end isBuy()
	
	public boolean isSell() {
		return ! this.isBuy();
		
	} // end isSell()
	
} // end abstract class PostEntity
