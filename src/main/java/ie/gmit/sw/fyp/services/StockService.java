package ie.gmit.sw.fyp.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;




@Service
public class StockService {
//	Fields
	private static Map<String, String> stockTags;
	private static StockService onlyInstance;
	
	
	
	
//	Constructors
	public StockService() {
		stockTags = new HashMap<>();
		
		initService();
	}
	
	
	
	
//	Methods
	public void initService() {
		// Should call a real DAO
		stockTags.put("AAPL", "Apple");
		stockTags.put("GOOGL", "Google");
		stockTags.put("GOOG", "Google");
	}
	
	
	public static boolean checkStockTag(String stockTag) {
		if ( stockTags.containsKey(stockTag) ) {
			return true;
		}
		
		return false;
		
	} // end checkStockTag
	
	
//	Singleton
	public static StockService getInstance() {
		if ( onlyInstance == null ) {
			onlyInstance = new StockService();
		}
		
		return onlyInstance;
		
	} // end getInstance()
	
} // end class StockService
