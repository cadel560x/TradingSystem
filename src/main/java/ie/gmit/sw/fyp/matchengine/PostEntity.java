package ie.gmit.sw.fyp.matchengine;

import java.util.Map;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
//import javax.validation.constraints.Min;

//import org.springframework.stereotype.Component;

import ie.gmit.sw.fyp.model.Transaction;
import ie.gmit.sw.fyp.services.OrderBookService;
//import ie.gmit.sw.fyp.services.StockService;
import ie.gmit.sw.fyp.services.UserService;




//@Component
@MappedSuperclass
public abstract class PostEntity implements Transaction {
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
		if ( ! UserService.checkUserIdStatic(userId) ) {
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
		if ( ! OrderBookService.checkStockTagStatic(stockTag) ) {
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
		try {
			return (int) properties.get("volume");
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			System.out.println("Invalid volume value, defaulting to 1");
			return 1;
		}
	}
	
//	@Min(value = 1)
	public void setVolume(int volume) {
		if ( volume <= 0 ) {
			throw new IllegalArgumentException("Invalid volume value");
		}
		properties.put("volume", volume);
	}

	public boolean isPartialFill() {
		try {
			return (boolean) properties.get("partialFill");
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			System.out.println("Invalid partialFill value, defaulting to true");
			return true;
		}
		
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
