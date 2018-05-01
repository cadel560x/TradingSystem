package ie.gmit.sw.fyp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ie.gmit.sw.fyp.services.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(method=RequestMethod.GET, value="/user")
	public void testUserGet() {
		boolean ret = userService.checkUserId("dfgjkaga9");
		System.out.println("userService ret: " + ret);
	}
}
