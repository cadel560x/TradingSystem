package ie.gmit.sw.fyp.order;

import java.util.Map;

//import java.util.HashMap;
//import java.util.Map;

public interface Request {
//	Fields
//	protected Properties requestProperties = new Properties();
//	protected Map<String, Object> requestProperties;
//	protected Map<String, Object> properties;
//	protected String userId;
//	protected String stockTag;




//	Accessors and mutators
	
	
	
	

//	Abstract Methods
//	public boolean checkProperties();
//	{
//		return checkUserId(getUserId()) && checkStockTag(getStockTag());
//		
//	} // end checkRequestProperties()
	
	public Map<String, Object> getProperties();

	public void setProperties(Map<String, Object> properties);
	
	public String getUserId();

	public void setUserId(String userId);

	public String getStockTag();

	public void setStockTag(String stockTag);

	boolean checkProperties(Iterable<String> propertiesList);
	
} // end abstract class Request
