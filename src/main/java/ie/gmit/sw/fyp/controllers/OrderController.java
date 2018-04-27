package ie.gmit.sw.fyp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ie.gmit.sw.fyp.me.PostRequest;
import ie.gmit.sw.fyp.notification.Notification;
import ie.gmit.sw.fyp.services.OrderBookService;




@RestController
public class OrderController {
	@Autowired
	private OrderBookService orderBookService;
	
	
	
	
	@RequestMapping(method=RequestMethod.POST, value="/{stockTag}")
	public Notification addPostOrder(@PathVariable String stockTag, @RequestBody PostRequest postRequest) {
		return orderBookService.addPostOrder(stockTag, postRequest);
		
	}
	
} // end class OrderController
