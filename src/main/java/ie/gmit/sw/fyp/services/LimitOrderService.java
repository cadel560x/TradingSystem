package ie.gmit.sw.fyp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.model.OrderStatus;
import ie.gmit.sw.fyp.repositories.LimitOrderRepository;




@Service
public class LimitOrderService {
//	Fields
	@Autowired
	private LimitOrderRepository limitOrderRepository;

	
	
		
//	Accessors and mutators
	
	
	
	
//	Methods
	public LimitOrder save(LimitOrder limitOrder) {
		return limitOrderRepository.save(limitOrder);
		
	} // end save(LimitOrder limitOrder)
	
	
	public void updateByIdStatusExpired(String id) {
		limitOrderRepository.updateByIdStatus(id, OrderStatus.EXPIRED.name());
		
	} // end updateByIdStatusExpired(String id)
	
	
	public Iterable<LimitOrder> findByStockTagAndStatusAccepted(String stockTag) {
		return limitOrderRepository.findByStockTagAndStatusAndOrderConditionOrderByTimestampAsc(stockTag, OrderStatus.ACCEPTED, PostOrderCondition.LIMIT);
		
	} // end findByStockTagAndStatusAccepted(String stockTag)
	
	
	public Iterable<String> findExpired() {
		Iterable<String> Ids = limitOrderRepository.findByExpirationTimeBeforeAndOrderConditionAndStatus();
		Iterable<String> stringIds = Ids;

		return stringIds;
		
	} // end findExpired()
	
	
	public void deleteAll() {
		limitOrderRepository.deleteAll();
		
	} // end void deleteAll()

} // end class OrderMatchService
