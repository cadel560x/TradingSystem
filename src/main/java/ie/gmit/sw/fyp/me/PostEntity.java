package ie.gmit.sw.fyp.me;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

//import org.springframework.beans.factory.annotation.Autowired;

import ie.gmit.sw.fyp.order.StockService;
import ie.gmit.sw.fyp.order.Transaction;
import ie.gmit.sw.fyp.order.UserService;




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
//	@Column
//	@Transient
	public String getUserId() {
		return (String) properties.get("userId");
	}

	@Override
//	@Transient
	public void setUserId(String userId) {
		if ( ! UserService.checkUserId(userId) ) {
			throw new IllegalArgumentException("Invalid user Id");
		}
		
		properties.put("userId", userId);
	}

	@Override
//	@Column
//	@Transient
	public String getStockTag() {
		return (String) properties.get("stockTag");
	}

	@Override
//	@Transient
	public void setStockTag(String stockTag) {
		if ( ! StockService.checkStockTag(stockTag) ) {
			throw new IllegalArgumentException("Invalid stock tag");
		}
		
		properties.put("stockTag", stockTag);
	}
	
	
	
	
//	Delegated Methods
//	@Transient
	@Enumerated(EnumType.STRING)
	public PostOrderType getType() {
		return (PostOrderType) properties.get("type");
	}
	
//	@Transient
	public void setType(PostOrderType type) {
		properties.put("type", type);
	}
	
//	@Transient
	@Enumerated(EnumType.STRING)
	public PostOrderCondition getOrderCondition() {
		return (PostOrderCondition) properties.get("condition");
	}
	
//	@Transient
	public void setOrderCondition(PostOrderCondition condition) {
		properties.put("condition", condition);
	}
	
//	@Transient
	public int getVolume() {
		return (int) properties.get("volume");
	}

//	@Transient
	public void setVolume(int volume) {
		if ( volume <= 0 ) {
			throw new IllegalArgumentException("Invalid volume value");
		}
		properties.put("volume", volume);
	}

//	@Transient
	public boolean isPartialFill() {
		return (boolean) properties.get("partialFill");

	}

//	@Transient
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
