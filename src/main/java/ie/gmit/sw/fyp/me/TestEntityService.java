package ie.gmit.sw.fyp.me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestEntityService {
	@Autowired
	private TestEntityInterface testEntityRepository;
	
	
	
	public void save(TestEntity testEntity) {
		testEntityRepository.save(testEntity.getId(), testEntity.getNum1(), testEntity.getNum2());
		
	}
	
} // end class TestEntityService
