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
	
	@Autowired
	private StopLossOrderService stopLossOrderService;
	
	
	
	
//	Methods
	public void save(MarketOrder marketOrder) {
		if ( marketOrder instanceof StopLossOrder ) {
			stopLossOrderService.save((StopLossOrder) marketOrder);
		}
		else if ( marketOrder instanceof LimitOrder ) {
			limitOrderService.save((LimitOrder) marketOrder);
		}
		else {
			marketOrderRepository.save(
					marketOrder.getId(), marketOrder.getTimestamp() , marketOrder.getUserId() , marketOrder.getStockTag(),
					marketOrder.getOrderCondition().name(), marketOrder.getType().name(), marketOrder.getVolume(),
					marketOrder.isPartialFill(), marketOrder.getStatus().name()
					);
		} // end if - else if - else
		
	} // end save(MarketOrder marketOrder)

} // end class OrderMatchService
