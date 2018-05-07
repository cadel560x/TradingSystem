package ie.gmit.sw.fyp.matchengine;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.*;




@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@DependsOn({"userService", "orderBookService"})
public class PostRequest extends PostEntity {
//	Fields
	private Map<String, Object> properties;
	
	
	
	
//	Constructors
	public PostRequest() {
		this.properties = new HashMap<>();
	}
	
	public PostRequest(PostRequest postRequest) {
		this.properties = new HashMap<>(postRequest.getProperties());
	}

	
	
	
//	Accesors and mutators
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	


	
//	Delegated methods
	public float getPrice() {
		return (float) properties.get("price");
	}

	public void setPrice(float price) {
		if ( price < 0.0001 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		
		properties.put("price", Float.parseFloat(String.format("%.4f", price)));
	}
	
	public Instant getExpirationTime() {
		return (Instant) properties.get("expirationTime");
	}

	public void setExpirationTime(Instant expirationTime) {
		if ( expirationTime.isBefore(Instant.now()) ) {
			throw new IllegalArgumentException("Expiration time older than current time");
		}
		properties.put("expirationTime", expirationTime);
	}
	
	public float getStopPrice() {
		return (float) properties.get("stopPrice");
	}
	
	public void setStopPrice(float stopPrice) {
		if ( stopPrice < 0.0001 ) {
			throw new IllegalArgumentException("Invalid price value");
		}
		
		properties.put("stopPrice", stopPrice);
	}	
	
	
	
	
//	Methods
	public boolean checkProperties(Collection<String> propertiesList) throws IllegalArgumentException {
		Set<String> keys = properties.keySet();
		
		if ( propertiesList.size() != keys.size() ) {
			throw new IllegalArgumentException("Invalid size of parameter set");
		}
		
		for ( String postProperty: keys ) {
			if ( ! propertiesList.contains(postProperty) ) {
				throw new IllegalArgumentException("The '"+ postProperty + "' parameter must not be null or empty");
			}
		
		} // end for
	
	return true;
	
	} // end checkProperties


	@Override
	public String toString() {
		return "PostRequest [properties=" + properties + "]";
		
	} // end toString()

} // end class PostRequest
