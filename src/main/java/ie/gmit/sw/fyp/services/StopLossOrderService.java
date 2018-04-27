package ie.gmit.sw.fyp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.me.StopLossOrder;
import ie.gmit.sw.fyp.order.OrderStatus;
import ie.gmit.sw.fyp.repositories.StopLossOrderRepository;




@Service
public class StopLossOrderService {
//	Fields
	@Autowired
	StopLossOrderRepository stopLossOrderRepository;
	
	
	
	
//	Methods
	public void save(StopLossOrder stopLossOrder) {
		stopLossOrderRepository.save(
				stopLossOrder.getId(), stopLossOrder.getTimestamp() , stopLossOrder.getUserId() , stopLossOrder.getStockTag(),
				stopLossOrder.getOrderCondition().name(), stopLossOrder.getType().name(), stopLossOrder.getPrice(), stopLossOrder.getStopPrice(),
				stopLossOrder.getVolume(), stopLossOrder.isPartialFill(), stopLossOrder.getExpirationTime(), stopLossOrder.getStatus().name()
				);
		
	} // end save(StopLossOrder stopLossOrder)
	
	
	public void updateByIdStatus(String id, OrderStatus newStatus) {
		stopLossOrderRepository.updateByIdStatus(id, newStatus.name());
		
	} // end updateByIdStatus(String id, PostOrderType newStatus)

} // end class OrderMatchService
