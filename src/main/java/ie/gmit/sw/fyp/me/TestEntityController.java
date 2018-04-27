package ie.gmit.sw.fyp.me;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestEntityController {
	@Autowired
	private TestEntityService testEntityService;
	
	
	@RequestMapping(method=RequestMethod.GET, value="/test")
	public void testEntityGet( @RequestParam Map<String, String> queryParameters) {
		TestEntity testRequest = new TestEntity();
		
//		System.out.println(queryParameters);
		testRequest.setId(queryParameters.get("Id"));
//		System.out.println("num1: " + Float.parseFloat(queryParameters.get("num1")));
		testRequest.setNum1(Float.parseFloat(queryParameters.get("num1")));
//		System.out.println("num2: " + Integer.parseInt(queryParameters.get("num2")));
		testRequest.setNum2(Integer.parseInt(queryParameters.get("num2")));
		
		testEntityService.save(testRequest);
	}
	
}
