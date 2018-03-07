package ie.gmit.sw.fyp.me;

import java.io.Serializable;
import java.sql.Timestamp;

public class OrderRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String userId;
	protected String orderType;
	protected String orderClass;
	protected String price;
	protected String volume;
	protected String partialFill;
	protected String expireTime;
	
	
	
	
	public OrderRequest() {
		
	}




	public OrderRequest(String userId, String orderType, String orderClass, String price, String volume,
			String partialFill, String expireTime) {
		this.userId = userId;
		this.orderType = orderType;
		this.orderClass = orderClass;
		this.price = price;
		this.volume = volume;
		this.partialFill = partialFill;
		this.expireTime = expireTime;
	}




	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderClass() {
		return orderClass;
	}

	public void setOrderClass(String orderClass) {
		this.orderClass = orderClass;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getPartialFill() {
		return partialFill;
	}

	public void setPartialFill(String partialFill) {
		this.partialFill = partialFill;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}	
	
}
