package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

public class PostOrder extends Order {
//	Fields
//	private Properties properties;
	
	
	
	
//	Constructors
	public PostOrder() {
//		super();
	}

	public PostOrder(PostRequest postRequest) {
//		super(PostOrderRequest);
//		setId(UUID.randomUUID().toString());
//		setTimestamp(new Timestamp(System.currentTimeMillis()));
//		setStatus(OrderStatus.CREATED);
		
		orderProperties = new HashMap<>(postRequest.requestProperties);
		orderProperties.put("Id", UUID.randomUUID().toString());
		orderProperties.put("timestamp", new Timestamp(System.currentTimeMillis()));
		orderProperties.put("status", OrderStatus.CREATED );
//		this.requestProperties = postRequest.requestProperties;
	}
	
	
	
	
//	Accessors and mutators
//	public String getId() {
//		return orderProperties.getId();
//	}
//
//	public void setId(String Id) {
//		orderProperties.setId(Id);
//	}
//
//	public Timestamp getTimestamp() {
//		return orderProperties.getTimestamp();
//	}
//
//	public void setTimestamp(Timestamp timestamp) {
//		orderProperties.setTimestamp(timestamp);
//	}
//
//	public OrderStatus getStatus() {
//		return orderProperties.getStatus();
//	}
//
//	public void setStatus(OrderStatus status) {
//		orderProperties.setStatus(status);
//	}

	
	
	
//	Methods
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostOrder other = (PostOrder) obj;
		if (orderProperties == null) {
			if (other.orderProperties != null)
				return false;
		} 
		else {
			String thisId = (String) this.orderProperties.get("Id");
			String otherId = (String) other.orderProperties.get("Id");
			if (!thisId.equals(otherId)) {
				return false;
			}
			Timestamp thisTimestamp = (Timestamp) this.orderProperties.get("timestamp");
			Timestamp otherTimestamp = (Timestamp) other.orderProperties.get("timestamp");
			if (!thisTimestamp.equals(otherTimestamp)) {
				return false;
			}
		}
			
		return true;
	} // end equals(Object obj)
	
	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
//		return result;
//	} // end hashCode()

} // end class PostOrder
