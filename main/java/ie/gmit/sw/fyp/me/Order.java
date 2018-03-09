package ie.gmit.sw.fyp.me;

//import java.util.HashMap;
import java.util.Map;

//import java.sql.Timestamp;
//import java.util.UUID;

public abstract class Order {
//	Fields
//	protected Properties orderProperties = new Properties();
	protected Map<String, Object> orderProperties;
//	protected Properties requestProperties;

	
	
		
//	Constructors
//	public Order() {
//		orderId = UUID.randomUUID().toString();
//		timestamp = new Timestamp(System.currentTimeMillis());
//	}
	
//	public Order(String userId, String orderType, String orderClass,
//			String price, String volume, String partialFill, String expireTime) {
//		super(userId, orderType, orderClass, price, volume, partialFill, expireTime);
//		orderId = UUID.randomUUID().toString();
//		timestamp = new Timestamp(System.currentTimeMillis());	
//	}
//	
//	public Order(OrderRequest orderRequest) {
//		super(orderRequest.userId, orderRequest.orderType, orderRequest.orderClass, orderRequest.price, orderRequest.volume, orderRequest.partialFill, orderRequest.expireTime);
//		orderId = UUID.randomUUID().toString();
//		timestamp = new Timestamp(System.currentTimeMillis());	
//	}
	
//	public Order(String userId, String stockTag, OrderType orderType, OrderClass orderClass, float price, int volume,
//			boolean partialFill, Timestamp expireTime) {
//		super(userId, stockTag, orderType, orderClass, price, volume, partialFill, expireTime);
//		orderId = UUID.randomUUID().toString();
//		timestamp = new Timestamp(System.currentTimeMillis());	
//	}
//	
//	public Order(OrderRequest orderRequest) {
//		super(orderRequest.userId, orderRequest.stockTag, orderRequest.orderType, orderRequest.orderClass, orderRequest.price, orderRequest.volume, orderRequest.partialFill, orderRequest.expireTime);
//		orderId = UUID.randomUUID().toString();
//		timestamp = new Timestamp(System.currentTimeMillis());	
//	}
	
	
	

//	Accessors and mutators
//	public String getOrderId() {
//		return orderId;
//	}
//
//	public Timestamp getTimestamp() {
//		return timestamp;
//	}
	
	
	
	
//	Abstract methods
//	public abstract boolean checkRequest();
	
} // end class Order
