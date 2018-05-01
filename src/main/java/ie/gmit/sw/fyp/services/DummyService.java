package ie.gmit.sw.fyp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.gmit.sw.fyp.matchengine.DummyClass;

@Service
public class DummyService {
	@Autowired
	private DummyClass dummy;
	
	public void setUserId(String userId) {
		dummy.setUserId(userId);
		
	}
}
