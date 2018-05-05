package ie.gmit.sw.fyp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.MarketOrder;
import ie.gmit.sw.fyp.matchengine.StopLossOrder;
import ie.gmit.sw.fyp.repositories.MarketOrderRepository;




@Service
public class MarketOrderService {
//	Fields
	@Autowired
	private MarketOrderRepository marketOrderRepository;
	
	@Autowired
	private LimitOrderService limitOrderService;
	
	@Autowired
	private StopLossOrderService stopLossOrderService;
	
	
	
	
//	Methods
	public MarketOrder save(MarketOrder marketOrder) {
		MarketOrder result = null;
		
		if ( marketOrder instanceof StopLossOrder ) {
			result = stopLossOrderService.save((StopLossOrder) marketOrder);
		}
		else if ( marketOrder instanceof LimitOrder ) {
			result = limitOrderService.save((LimitOrder) marketOrder);
		}
		else {
			result = marketOrderRepository.save(marketOrder);
		} // end if - else if - else
		
		return result;
		
	} // end save(MarketOrder marketOrder)
	
	
	public void deleteAll() {
		marketOrderRepository.deleteAll();
		
	} // end void deleteAll()

} // end class OrderMatchService
