package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;




public class PostRequest extends PostEntity {
//	Fields
	
	
	
	
//	Constructors
	public PostRequest() {
		properties = new HashMap<>();
	}

	
	
	
//	Accesors and mutators
	


	
//	Delegated methods
	public float getPrice() {
		return (float) properties.get("price");
	}

	public void setPrice(float price) {
		if ( price <= 0 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		
		properties.put("price", Float.parseFloat(String.format("%.4f", price)));
	}
	
	public Timestamp getExpirationTime() {
		return (Timestamp) properties.get("expirationTime");
	}

	public void setExpirationTime(Timestamp expirationTime) {
		if ( expirationTime.before(new Date()) ) {
			throw new IllegalArgumentException("Expiration time older than current time");
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
	public boolean checkProperties(Collection<String> propertiesList) {
		Set<String> keys = properties.keySet();
		
		if ( propertiesList.size() != keys.size() ) {
			return false;
		}
		
		for ( String postProperty: keys ) {
			if ( ! propertiesList.contains(postProperty) ) {
				return false;
			}
		
		} // end for
	
	return true;
	
	} // end checkProperties


	@Override
	public String toString() {
		return "PostRequest [properties=" + properties + "]";
		
	} // end toString()

} // end class PostRequest
