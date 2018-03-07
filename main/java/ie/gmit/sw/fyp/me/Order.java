package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
import java.util.UUID;

public class Order extends OrderRequest {
//	Fields
	private String orderId;
	private Timestamp timestamp;

	
	
		
//	Constructors
	public Order() {
		orderId = UUID.randomUUID().toString();
		timestamp = new Timestamp(System.currentTimeMillis());
	}
	
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
	
	public Order(String userId, String stockTag, PostOrderType orderType, OrderClass orderClass, float price, int volume,
			boolean partialFill, Timestamp expireTime) {
		super(userId, stockTag, orderType, orderClass, price, volume, partialFill, expireTime);
		orderId = UUID.randomUUID().toString();
		timestamp = new Timestamp(System.currentTimeMillis());	
	}
	
	public Order(OrderRequest orderRequest) {
		super(orderRequest.userId, orderRequest.stockTag, orderRequest.orderType, orderRequest.orderClass, orderRequest.price, orderRequest.volume, orderRequest.partialFill, orderRequest.expireTime);
		orderId = UUID.randomUUID().toString();
		timestamp = new Timestamp(System.currentTimeMillis());	
	}
	
	
	

//	Accessors and mutators
	public String getOrderId() {
		return orderId;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
	
} // end class Order
