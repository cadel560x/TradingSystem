package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;




public class PostRequest extends PostEntity {
//	Fields
	
	
	
	
//	Constructors
	public PostRequest() {
		properties = new HashMap<>();
	}

	
	
	
//	Accesors and mutators
	


	
//	Delegated methods
	public Timestamp getExpirationTime() {
		return (Timestamp) properties.get("expirationTime");
	}

	public void setExpirationTime(Timestamp expirationTime) {
		if ( expirationTime.before(new Date()) ) {
			throw new IllegalStateException("Expiration time older than current time");
		}
		properties.put("expirationTime", expirationTime);
	}
	
	public float getStopPrice() {
		return (float) properties.get("stopPrice");
	}
	
	public void setStopPrice(float stopPrice) {
		if ( stopPrice <= 0 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		
		properties.put("stopPrice", stopPrice);
	}	
	
	
	
	
//	Methods
	public boolean checkProperties(Iterable<String> propertiesList) {	
		for ( String postProperty: propertiesList ) {
			if ( ! properties.containsKey(postProperty) ) {
				return false;
			}
		
		} // end for
	
	return true;
	
	} // end checkProperties

} // end class PostRequest
