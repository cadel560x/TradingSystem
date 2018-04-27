package ie.gmit.sw.fyp.repositories;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ie.gmit.sw.fyp.me.MarketOrder;

public interface MarketOrderRepository extends CrudRepository<MarketOrder, String> {
	@Transactional
	@Modifying
	@Query(value="INSERT INTO market_order (id, timestamp, user_id, stock_tag, order_condition, type, volume, partial_fill, status) " + 
	              "VALUES (:Id, :timeStamp, :userId, :stockTag, :orderCondition, :type, :volume, :partialFill, :status)", nativeQuery=true)
	void save(@Param("Id") String id, @Param("timeStamp") Timestamp timeStamp, @Param("userId") String userId, @Param("stockTag") String stockTag,
			@Param("orderCondition") String orderCondition, @Param("type") String type, @Param("volume") int volume,
			@Param("partialFill") boolean partialFill, @Param("status") String status);
	
	
	@Transactional
	@Modifying
	@Query(value="UPDATE market_order SET status = :status WHERE id = :Id", nativeQuery=true)
	void updateByIdStatus(@Param("Id") String id, @Param("status") String newStatus);
	
} // end interface MarketOrderRepository
