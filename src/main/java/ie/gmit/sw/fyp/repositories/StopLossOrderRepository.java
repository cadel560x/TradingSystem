package ie.gmit.sw.fyp.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.matchengine.StopLossOrder;
import ie.gmit.sw.fyp.model.OrderStatus;




public interface StopLossOrderRepository extends CrudRepository<StopLossOrder, String> {
	@Transactional
	@Modifying
	@Query(value="UPDATE stop_loss_order SET status = :status WHERE id = :Id", nativeQuery=true)
	void updateByIdStatus(@Param("Id") String id, @Param("status") String newStatus);
	
//	SELECT * FROM stop_loss_order WHERE stock_tag = :stockTag AND status = :status AND order_condition = :orderCondition
	public Iterable<StopLossOrder> findByStockTagAndStatusAndOrderConditionOrderByTimestampAsc(String stockTag, OrderStatus status, PostOrderCondition orderCondition);
	
	
//	SELECT id FROM stop_loss_order WHERE expiration_time < :timestamp AND status = :status AND order_condition = :orderCondition
//	public Iterable<IdOnly> findByExpirationTimeBeforeAndOrderConditionAndStatus(Instant timestamp, PostOrderCondition orderCondition, OrderStatus status);
	@Query(value="SELECT id FROM stop_loss_order WHERE expiration_time < NOW() AND status = 'ACCEPTED' AND order_condition = 'STOPLOSS'", nativeQuery=true)
	public Iterable<String> findByExpirationTimeBeforeAndOrderConditionAndStatus();
	
} // end interface StopLossOrderRepository
