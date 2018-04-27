package ie.gmit.sw.fyp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.me.LimitOrder;
import ie.gmit.sw.fyp.me.MarketOrder;
import ie.gmit.sw.fyp.me.StopLossOrder;
import ie.gmit.sw.fyp.repositories.MarketOrderRepository;




@Service
public class MarketOrderService {
//	Fields
	@Autowired
	private MarketOrderRepository marketOrderRepository;
	
	@Autowired
	private LimitOrderService limitOrderService;
	
	
	
	
//	Methods
	public void save(MarketOrder marketOrder) {
		if ( marketOrder instanceof LimitOrder ) {
			limitOrderService.save((LimitOrder) marketOrder);
		}
		else if ( marketOrder instanceof StopLossOrder ) {
			
		}
		else {
			marketOrderRepository.save(marketOrder);
		}
		
	}

} // end class OrderMatchService
