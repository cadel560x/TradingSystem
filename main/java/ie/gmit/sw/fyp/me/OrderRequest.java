package ie.gmit.sw.fyp.me;

//import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

//public class OrderRequest implements Serializable {
public class OrderRequest {
//	Fields

//	private static final long serialVersionUID = 1L;
	
//	protected String userId;
//	protected String orderType;
//	protected String orderClass;
//	protected String price;
//	protected String volume;
//	protected String partialFill;
//	protected String expireTime;
	
	protected String userId;
	protected String stockTag;
	protected PostOrderType orderType;
	protected OrderClass orderClass;
	protected float price;
	protected int volume;
	protected boolean partialFill;
	protected Timestamp expireTime;
	
	
	
	
//	Constructors
	public OrderRequest() {
		
	}
//
//	public OrderRequest(String userId, String orderType, String orderClass, String price, String volume,
//			String partialFill, String expireTime) {
//		this.userId = userId;
//		this.orderType = orderType;
//		this.orderClass = orderClass;
//		this.price = price;
//		this.volume = volume;
//		this.partialFill = partialFill;
//		this.expireTime = expireTime;
//	}
	
	public OrderRequest(String userId, String stockTag, PostOrderType orderType, OrderClass orderClass, float price, int volume,
			boolean partialFill, Timestamp expireTime) {
		this.userId = userId;
		this.stockTag = stockTag;
		this.orderType = orderType;
		this.orderClass = orderClass;
		this.price = price;
		this.volume = volume;
		this.partialFill = partialFill;
		this.expireTime = expireTime;
	}	




//	Accessors and mutators
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		if ( ! checkUserId(userId) ) {
			throw new IllegalArgumentException("Invalid user Id");
		}
		this.userId = userId;
	}

	public String getStockTag() {
		return stockTag;
	}

	public void setStockTag(String stockTag) {
		if ( ! checkStockTag(stockTag) ) {
			throw new IllegalArgumentException("Invalid stock tag");
		}
		this.stockTag = stockTag;
	}

	public PostOrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(PostOrderType orderType) {
		this.orderType = orderType;
	}

	public OrderClass getOrderClass() {
		return orderClass;
	}

	public void setOrderClass(OrderClass orderClass) {
		this.orderClass = orderClass;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		if ( price <= 0 ) {
			throw new NumberFormatException("Invalid price value");
		}
		this.price = price;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		if ( volume <= 0 ) {
			throw new NumberFormatException("Invalid volume value");
		}
		this.volume = volume;
	}

	public boolean isPartialFill() {
		return partialFill;
	}

	public void setPartialFill(boolean partialFill) {
		this.partialFill = partialFill;
	}

	public Timestamp getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Timestamp expireTime) {
		if ( expireTime.before(new Date()) ) {
			throw new IllegalStateException("Expiration time older than current time");
		}
		this.expireTime = expireTime;
	}

//	public String getOrderType() {
//		return orderType;
//	}
//
//	public void setOrderType(String orderType) {
//		this.orderType = orderType;
//	}
//
//	public String getOrderClass() {
//		return orderClass;
//	}
//
//	public void setOrderClass(String orderClass) {
//		this.orderClass = orderClass;
//	}
//
//	public String getPrice() {
//		return price;
//	}
//
//	public void setPrice(String price) {
//		this.price = price;
//	}
//	
//	public String getVolume() {
//		return volume;
//	}
//
//	public void setVolume(String volume) {
//		this.volume = volume;
//	}
//
//	public String getPartialFill() {
//		return partialFill;
//	}
//
//	public void setPartialFill(String partialFill) {
//		this.partialFill = partialFill;
//	}
//
//	public String getExpireTime() {
//		return expireTime;
//	}
//
//	public void setExpireTime(String expireTime) {
//		this.expireTime = expireTime;
//	}	
	
	
	
	
//	Methods
	private boolean checkStockTag(String stockTag) {
//		TODO Implement a real stockTag checker
		if ( stockTag.equals("AAPL") ) {
			return true;
		}
		else {
			return false;
		}
		
	} // end checkStockTag(String stockTag)
	
	
	private boolean checkUserId(String userId) {
		// TODO Implement a real userId checker
		if ( userId.equals("dfgjkaga9") ) {
			return true;
		}
		else {
			return false;
		}
		
	} // end checkUserId(String userId)
	
} // end class OrderRequest
