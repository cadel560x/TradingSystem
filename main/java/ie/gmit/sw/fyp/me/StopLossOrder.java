package ie.gmit.sw.fyp.me;

//import java.sql.Timestamp;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.UUID;

//import ie.gmit.sw.fyp.order.Order;
//import ie.gmit.sw.fyp.order.OrderStatus;
//import ie.gmit.sw.fyp.order.Request;

public class StopLossOrder extends LimitOrder {
//	Fields
//	private Map<String, Object> properties;
	
	
	
	
//	Constructors
	public StopLossOrder() {
		super();
//		this.setId( UUID.randomUUID().toString() );
//		this.setTimestamp( new Timestamp(System.currentTimeMillis()) );
//		this.setStatus(OrderStatus.CREATED );
	}
	
//	public StopLossOrder(StopLossRequest stopLossRequest) {
	public StopLossOrder(PostRequest stopLossRequest) {
		super(stopLossRequest);
//		setId(UUID.randomUUID().toString());
//		setTimestamp(new Timestamp(System.currentTimeMillis()));
//		setStatus(OrderStatus.CREATED);
		
//		properties = new HashMap<>(postRequest.requestProperties);
		
//		properties = postRequest.getProperties();
//		this.setId( UUID.randomUUID().toString() );
//		this.setTimestamp( new Timestamp(System.currentTimeMillis()) );
//		this.setStatus(OrderStatus.CREATED );
//		this.requestProperties = postRequest.requestProperties;
	}
	
	public StopLossOrder(LimitOrder limitOrder) {
		super(limitOrder);
//		this.properties = new HashMap<>(postOrder.getProperties());

	}
	
	public StopLossOrder(StopLossOrder stopLossOrder) {
		super(stopLossOrder);
//		this.properties = new HashMap<>(postOrder.getProperties());

	}
	
	
	
	
//	Accessors and mutators
//	public String getId() {
//		return (String) properties.get("Id");
//	}
//
//	public void setId(String Id) {
//		properties.put("Id", Id);
//	}
//
//	public Timestamp getTimestamp() {
//		return (Timestamp) properties.get("timestamp");
//	}
//
//	public void setTimestamp(Timestamp timestamp) {
//		properties.put("timestamp", timestamp);
//	}
//
//	public OrderStatus getStatus() {
//		return (OrderStatus) properties.get("status");
//	}
//
//	public void setStatus(OrderStatus status) {
//		properties.put("status", status);
//	}

	
	
	
//	Absctract methods implementation
//	@Override
//	public PostRequest getRequest() {
//		// TODO Auto-generated method stub
//		return (PostRequest) super.clone();
//	}
//
////	@Override
//	public void setRequest(Request request) {
//		// TODO Is it worth it to implement this here???? Should be implemented in the superclass?? Is it used???
//		this.request = request;
//	}
	
	
	
	
//	Methods
//	public boolean isBuy() {
//		return ( this.properties.get("type") == PostOrderType.BUY );
//		
//	} // end isBuy()
//	
//	
//	public boolean isSell() {
//		return ! this.isBuy();
//		
//	} // end isSell()
	
	
//	public boolean matches(MatchOrder other) {
//		// A match is done between two orders of opposite type
//		// Flipflop of PostOrderType to find out if these objects are counter orders.
////		if ( this.isBuy() == other.isBuy() ) {
////			return false;
////		}
//		
////		Getting the methods from 'request'
////		PostRequest thisOrder = (PostRequest) this.request;
//		
//		
//		//
////		if ( ! (boolean)this.properties.get("partialFill") ) {
//		if ( ! this.isPartialFill() ) {
//			// if volumes match ...
////			if ( (int)this.properties.get("volume") != (int)other.properties.get("volume") ) {
//			if ( this.getVolume() != other.getVolume() ) {
//				return false;
//			}
//			
//		} // end if ( (boolean)this.properties.get("partialFill") )
//		
//		if ( ( this.isBuy() ) && ( this.getPrice() > other.getPrice() ) ) {
//			return false;
//		}
//		else if ( ( this.isSell() ) && ( this.getPrice() > other.getPrice() ) ) {
//			return false;		
//		} // end if ( this.isBuy() ) && ( thisPrice > otherPrice ) - else if ( this.isSell() ) && ( thisPrice < otherPrice )
//		// If we are here is because the prices are equal or better depending on 'this'
//		
//		// Anything else is a match
//		return true;
//		
//	} // end match(PostOrder other) 
	
	
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		
//		MatchOrder other = (MatchOrder) obj;
//		if (properties == null) {
//			if (other.properties != null)
//				return false;
//		} 
//		else {
//			if (! this.getId().equals(other.getId()) ) {
//				return false;
//			}
//
//			if ( ! this.getTimestamp().equals(other.getTimestamp()) ) {
//				return false;
//			}
//		}
//		return true;
//		
//	} // end equals(Object obj)
	
	
//	@Override
//	public int hashCode() {
////		final int prime = 31;
////		int result = 1;
////		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
////		return result;
//		return (int) this.getPrice() * 10000;
//	} // end hashCode()

} // end class PostOrder
