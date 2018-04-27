package ie.gmit.sw.fyp.repositories;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import ie.gmit.sw.fyp.me.OrderMatch;




public interface OrderMatchRepository extends CrudRepository<OrderMatch, String> {
	@Transactional
	@Modifying
	@Query(value="INSERT INTO order_match (id, timestamp, sell_order, buy_order, filled_shares) " + 
	              "VALUES (:Id, :timeStamp, :sellOrderId, :buyOrderId, :filledShares)", nativeQuery=true)
	public void save(@Param("Id") String id, @Param("timeStamp") Timestamp timeStamp, @Param("sellOrderId") String sellOrderId,
			@Param("buyOrderId") String buyOrderId, @Param("filledShares") int filledShares);
	
}
