package ie.gmit.sw.fyp.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.matchengine.StopLossOrder;
import ie.gmit.sw.fyp.model.OrderStatus;
import ie.gmit.sw.fyp.repositories.IdOnly;
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
	
	
	public Iterable<StopLossOrder> findByStockTagAndStatus(String stockTag, OrderStatus status) {
		return stopLossOrderRepository.findByStockTagAndStatusAndOrderConditionOrderByTimestampAsc(stockTag, status, PostOrderCondition.STOPLOSS);
		
	} // end findByStockTagAndStatus(String stockTag, OrderStatus status)
	
	
	public Iterable<String> findIdsByExpirationTimeBefore(Timestamp timestamp) {
		Iterable<IdOnly> Ids = stopLossOrderRepository.findByExpirationTimeBeforeAndOrderConditionAndStatus(timestamp, PostOrderCondition.STOPLOSS, OrderStatus.ACCEPTED);
		List<String> stringIds = new ArrayList<>();
		
		for (IdOnly idOnly: Ids) {
			stringIds.add(idOnly.getId());
		}
		
		return stringIds;
		
	} // end findByExpirationTimeBefore(Timestamp timestamp)

} // end class OrderMatchService
