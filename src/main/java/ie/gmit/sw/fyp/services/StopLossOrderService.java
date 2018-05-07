package ie.gmit.sw.fyp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.matchengine.StopLossOrder;
import ie.gmit.sw.fyp.model.OrderStatus;
import ie.gmit.sw.fyp.repositories.StopLossOrderRepository;




@Service
public class StopLossOrderService {
//	Fields
	@Autowired
	private StopLossOrderRepository stopLossOrderRepository;
	
	
	
	
//	Methods
	public StopLossOrder save(StopLossOrder stopLossOrder) {
		return stopLossOrderRepository.save(stopLossOrder);
		
	} // end save(StopLossOrder stopLossOrder)
	
	
	public void updateByIdStatusExpired(String id) {
		stopLossOrderRepository.updateByIdStatus(id, OrderStatus.EXPIRED.name());
		
	} // end updateByIdStatusExpired(String id)
	
	
	public Iterable<StopLossOrder> findByStockTagAndStatusAccepted(String stockTag) {
		return stopLossOrderRepository.findByStockTagAndStatusAndOrderConditionOrderByTimestampAsc(stockTag, OrderStatus.ACCEPTED, PostOrderCondition.STOPLOSS);
		
	} // end findByStockTagAndStatusAccepted(String stockTag)
	
	
	public Iterable<String> findExpired() {
//		Iterable<IdOnly> Ids = stopLossOrderRepository.findByExpirationTimeBeforeAndOrderConditionAndStatus(Instant.now(), PostOrderCondition.STOPLOSS, OrderStatus.ACCEPTED);
		Iterable<String> Ids = stopLossOrderRepository.findByExpirationTimeBeforeAndOrderConditionAndStatus();
//		List<String> stringIds = new ArrayList<>();
		Iterable<String> stringIds = Ids;
		
//		for (IdOnly idOnly: Ids) {
//			stringIds.add(idOnly.getId());
//		}
		
		return stringIds;
		
	} // end findExpired()
	
	
	public void deleteAll() {
		stopLossOrderRepository.deleteAll();
		
	} // end void deleteAll()

} // end class OrderMatchService
