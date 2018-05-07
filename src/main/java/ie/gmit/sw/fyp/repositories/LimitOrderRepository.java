package ie.gmit.sw.fyp.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.model.OrderStatus;




public interface LimitOrderRepository extends CrudRepository<LimitOrder, String> {
	@Transactional
	@Modifying
	@Query(value="UPDATE limit_order SET status = :status WHERE id = :Id", nativeQuery=true)
	void updateByIdStatus(@Param("Id") String id, @Param("status") String newStatus);
	
	
//	SELECT * FROM limit_order WHERE stock_tag = :stockTag AND status = :status AND order_condition = :orderCondition	
	public Iterable<LimitOrder> findByStockTagAndStatusAndOrderConditionOrderByTimestampAsc(String stockTag, OrderStatus status, PostOrderCondition orderCondition);
	
	
//	SELECT id FROM limit_order WHERE expiration_time < :timestamp AND status = :status AND order_condition = :orderCondition
//	public Iterable<IdOnly> findByExpirationTimeBeforeAndOrderConditionAndStatus(Instant timestamp, PostOrderCondition orderCondition, OrderStatus status);
	@Query(value="SELECT id FROM limit_order WHERE expiration_time < NOW() AND status = 'ACCEPTED' AND order_condition = 'LIMIT'", nativeQuery=true)
	public Iterable<String> findByExpirationTimeBeforeAndOrderConditionAndStatus();
	
} // end interface LimitOrderRepository
