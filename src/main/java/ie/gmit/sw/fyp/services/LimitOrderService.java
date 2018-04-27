package ie.gmit.sw.fyp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.me.LimitOrder;
import ie.gmit.sw.fyp.repositories.LimitOrderRepository;




@Service
public class LimitOrderService {
//	Fields
	@Autowired
	LimitOrderRepository limitOrderRepository;
	
	
	
	
//	Methods
	public void save(LimitOrder limitOrder) {
		limitOrderRepository.save(
				limitOrder.getId(), limitOrder.getTimestamp() , limitOrder.getUserId() , limitOrder.getStockTag(),
				limitOrder.getOrderCondition().name(), limitOrder.getType().name(), limitOrder.getPrice(), limitOrder.getVolume(),
				limitOrder.isPartialFill(), limitOrder.getExpirationTime(), limitOrder.getStatus().name()
				);
		
	} // end save(LimitOrder limitOrder)

} // end class OrderMatchService
