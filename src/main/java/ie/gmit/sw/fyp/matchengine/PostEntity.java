package ie.gmit.sw.fyp.matchengine;

//import java.util.Map;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import ie.gmit.sw.fyp.model.Transaction;
import ie.gmit.sw.fyp.services.OrderBookService;
import ie.gmit.sw.fyp.services.UserService;




@MappedSuperclass
public abstract class PostEntity implements Transaction {
//	Fields
	@NotNull
	private String userId;
	
	@NotNull
	private String stockTag;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private PostOrderType type;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private PostOrderCondition orderCondition;
	
	@Min(1)
	@NotNull
	private int volume;
	
	@NotNull
	private Boolean partialFill;
	
	
	
	
//	Accesors and mutators
	
	
	
	
//	Implemented Abstract Methods
	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public void setUserId(String userId) {
		if ( ! UserService.checkUserIdStatic(userId) ) {
			throw new IllegalArgumentException("Invalid user Id");
		}
		
		this.userId = userId;
	}

	@Override
	public String getStockTag() {
		return stockTag;
	}

	@Override
	public void setStockTag(String stockTag) {
		if ( ! OrderBookService.checkStockTagStatic(stockTag) ) {
			throw new IllegalArgumentException("Invalid stock tag");
		}
		
		this.stockTag = stockTag;
	}
	
	
	
	
//	Accesors and mutators
	@Enumerated(EnumType.STRING)
	public PostOrderType getType() {
		return type;
	}
	
	public void setType(PostOrderType type) {
		this.type = type;
	}
	
	public PostOrderCondition getOrderCondition() {
		return orderCondition;
	}
	
	public void setOrderCondition(PostOrderCondition orderCondition) {
		this.orderCondition = orderCondition;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}

	public boolean isPartialFill() {
		return partialFill;
	}
	
	public boolean getPartialFill() {
		return partialFill;
	}

	public void setPartialFill(Boolean partialFill) {
//		properties.put("partialFill", partialFill);
		this.partialFill = partialFill;
	}
	
	
	
	
//	Methods
	@Transient
	public boolean isBuy() {
//		return ( properties.get("type") == PostOrderType.BUY );
		return ( type == PostOrderType.BUY );
		
	} // end isBuy()
	
	@Transient
	public boolean isSell() {
		return ! this.isBuy();
		
	} // end isSell()
	
} // end abstract class PostEntity
