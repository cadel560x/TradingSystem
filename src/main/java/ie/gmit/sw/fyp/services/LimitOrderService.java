package ie.gmit.sw.fyp.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.model.OrderStatus;
import ie.gmit.sw.fyp.repositories.LimitOrderRepository;
import ie.gmit.sw.fyp.repositories.IdOnly;




@Service
public class LimitOrderService {
//	Fields
	@Autowired
	private LimitOrderRepository limitOrderRepository;

	
	
		
//	Accessors and mutators
	
	
	
	
//	Methods
	public void save(LimitOrder limitOrder) {
		limitOrderRepository.save(
				limitOrder.getId(), limitOrder.getTimestamp() , limitOrder.getUserId() , limitOrder.getStockTag(),
				limitOrder.getOrderCondition().name(), limitOrder.getType().name(), limitOrder.getPrice(), limitOrder.getVolume(),
				limitOrder.isPartialFill(), limitOrder.getExpirationTime(), limitOrder.getStatus().name()
				);
		
	} // end save(LimitOrder limitOrder)
	
	
	public void updateByIdStatus(String id, OrderStatus newStatus) {
		limitOrderRepository.updateByIdStatus(id, newStatus.name());
		
	} // end updateByIdStatus(String id, PostOrderType newStatus)
	
	
	public Iterable<LimitOrder> findByStockTagAndStatus(String stockTag, OrderStatus status) {
		return limitOrderRepository.findByStockTagAndStatusAndOrderConditionOrderByTimestampAsc(stockTag, status, PostOrderCondition.LIMIT);
		
	} // end findByStockTagAndStatus(String stockTag, OrderStatus status)
	
	
	public Iterable<String> findIdsByExpirationTimeBefore(Timestamp timestamp) {
		Iterable<IdOnly> Ids = limitOrderRepository.findByExpirationTimeBeforeAndOrderConditionAndStatus(timestamp, PostOrderCondition.LIMIT, OrderStatus.ACCEPTED);
		List<String> stringIds = new ArrayList<>();
		
		for (IdOnly idOnly: Ids) {
			stringIds.add(idOnly.getId());
		}
		
		return stringIds;
		
	} // end findByExpirationTimeBefore(Timestamp timestamp)
	
	
	

} // end class OrderMatchService
