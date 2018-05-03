package ie.gmit.sw.fyp.repositories;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.model.OrderStatus;




public interface LimitOrderRepository extends CrudRepository<LimitOrder, String> {
	@Transactional
	@Modifying
	@Query(value="INSERT INTO limit_order (id, timestamp, user_id, stock_tag, order_condition, type, price, volume, partial_fill, expiration_time, status) " + 
	              "VALUES (:Id, :timeStamp, :userId, :stockTag, :orderCondition, :type, :price, :volume, :partialFill, :expirationTime, :status)", nativeQuery=true)
	void save(@Param("Id") String id, @Param("timeStamp") Timestamp timeStamp, @Param("userId") String userId, @Param("stockTag") String stockTag,
			@Param("orderCondition") String orderCondition, @Param("type") String type, @Param("price") float price, @Param("volume") int volume,
			@Param("partialFill") boolean partialFill, @Param("expirationTime") Timestamp expirationTime, @Param("status") String status);
	
	
	@Transactional
	@Modifying
	@Query(value="UPDATE limit_order SET status = :status WHERE id = :Id", nativeQuery=true)
	void updateByIdStatus(@Param("Id") String id, @Param("status") String newStatus);
	
	
	public Iterable<LimitOrder> findByStockTagAndStatusOrderByTimestampAsc(String stockTag, OrderStatus status);
	
	
//	public Iterable<LimitOrder> findByStockTag(@Param("stockTag") String stockTag);
	
} // end interface LimitOrderRepository
