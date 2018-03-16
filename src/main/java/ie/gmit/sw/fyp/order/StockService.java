package ie.gmit.sw.fyp.order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;




@Service
public class StockService {
//	Fields
	private Map<String, String> stockTags;
	
	
	
	
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
	
	
	public boolean checkStockTag(String stockTag) {
		if ( stockTags.containsKey(stockTag) ) {
			return true;
		}
		
		return false;
	} // end checkStockTag
	
} // end class StockService
