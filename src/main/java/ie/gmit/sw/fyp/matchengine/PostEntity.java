package ie.gmit.sw.fyp.matchengine;

import java.util.Map;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import ie.gmit.sw.fyp.model.Transaction;
import ie.gmit.sw.fyp.services.StockService;
import ie.gmit.sw.fyp.services.UserService;




@MappedSuperclass
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
	@Transient
	protected Map<String, Object> properties;
	
	
	
	
//	Accesors and mutators
	@Transient
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	@Transient
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	
	
	
//	Implemented Abstract Methods
	@Override
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
	@Enumerated(EnumType.STRING)
	public PostOrderType getType() {
		return (PostOrderType) properties.get("type");
	}
	
	public void setType(PostOrderType type) {
		properties.put("type", type);
	}
	
	@Enumerated(EnumType.STRING)
	public PostOrderCondition getOrderCondition() {
		return (PostOrderCondition) properties.get("condition");
	}
	
	public void setOrderCondition(PostOrderCondition condition) {
		properties.put("condition", condition);
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
	@Transient
	public boolean isBuy() {
		return ( properties.get("type") == PostOrderType.BUY );
		
	} // end isBuy()
	
	@Transient
	public boolean isSell() {
		return ! this.isBuy();
		
	} // end isSell()
	
} // end abstract class PostEntity
