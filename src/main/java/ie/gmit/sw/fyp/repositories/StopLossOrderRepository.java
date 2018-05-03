package ie.gmit.sw.fyp.repositories;

import java.sql.Timestamp;

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
	@Query(value="INSERT INTO stop_loss_order (id, timestamp, user_id, stock_tag, order_condition, type, price, stop_price, volume, partial_fill, expiration_time, status) " + 
	              "VALUES (:Id, :timeStamp, :userId, :stockTag, :orderCondition, :type, :price, :stopPrice, :volume, :partialFill, :expirationTime, :status)", nativeQuery=true)
	void save(@Param("Id") String id, @Param("timeStamp") Timestamp timeStamp, @Param("userId") String userId, @Param("stockTag") String stockTag,
			@Param("orderCondition") String orderCondition, @Param("type") String type, @Param("price") float price, @Param("stopPrice") float stopPrice,
			@Param("volume") int volume,	@Param("partialFill") boolean partialFill, @Param("expirationTime") Timestamp expirationTime,
			@Param("status") String status);
	
	
	@Transactional
	@Modifying
	@Query(value="UPDATE stop_loss_order SET status = :status WHERE id = :Id", nativeQuery=true)
	void updateByIdStatus(@Param("Id") String id, @Param("status") String newStatus);
	
//	SELECT * FROM stop_loss_order WHERE stock_tag = :stockTag AND status = :status AND order_condition = :orderCondition
	public Iterable<StopLossOrder> findByStockTagAndStatusAndOrderConditionOrderByTimestampAsc(String stockTag, OrderStatus status, PostOrderCondition orderCondition);
	
	
//	SELECT id FROM stop_loss_order WHERE expiration_time < :timestamp AND status = :status AND order_condition = :orderCondition
	public Iterable<IdOnly> findByExpirationTimeBeforeAndOrderConditionAndStatus(Timestamp timestamp, PostOrderCondition orderCondition, OrderStatus status);
	
} // end interface StopLossOrderRepository
