package ie.gmit.sw.fyp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ie.gmit.sw.fyp.services.DummyService;

@RestController
public class DummyController {
	@Autowired
	private DummyService dummyService;
	
	@RequestMapping(method=RequestMethod.GET, value="/dummy")
	public void testGet() {
		dummyService.setUserId("dfgjkaga9");
	}
	
}
