package ie.gmit.sw.fyp.repositories;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ie.gmit.sw.fyp.me.StopLossOrder;




public interface StopLossOrderRepository extends CrudRepository<StopLossOrder, String> {
	@Transactional
	@Modifying
	@Query(value="INSERT INTO stop_loss_order (id, timestamp, user_id, stock_tag, order_condition, type, price, stop_price, volume, partial_fill, expiration_time, status) " + 
	              "VALUES (:Id, :timeStamp, :userId, :stockTag, :orderCondition, :type, :price, :stopPrice, :volume, :partialFill, :expirationTime, :status)", nativeQuery=true)
	void save(@Param("Id") String id, @Param("timeStamp") Timestamp timeStamp, @Param("userId") String userId, @Param("stockTag") String stockTag,
			@Param("orderCondition") String orderCondition, @Param("type") String type, @Param("price") float price, @Param("stopPrice") float stopPrice,
			@Param("volume") int volume,	@Param("partialFill") boolean partialFill, @Param("expirationTime") Timestamp expirationTime,
			@Param("status") String status);
	
}