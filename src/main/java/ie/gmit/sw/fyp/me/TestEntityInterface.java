package ie.gmit.sw.fyp.me;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TestEntityInterface extends CrudRepository<TestEntity, String> {
	@Transactional
	@Modifying
	@Query(value="INSERT INTO test_entity VALUES(:Id, :num1, :num2)", nativeQuery=true)
	void save(@Param("Id") String id, @Param("num1") float num1, @Param("num2") int num2);

}
