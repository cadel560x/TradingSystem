package ie.gmit.sw.fyp.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ie.gmit.sw.fyp.matchengine.PostRequest;
import ie.gmit.sw.fyp.notification.Notification;
import ie.gmit.sw.fyp.services.OrderBookService;




@RestController
public class OrderController {
//	Fields
	@Autowired
	private OrderBookService orderBookService;

	
	
	
//	Methods
	@RequestMapping(method=RequestMethod.POST, value="/order-matching-system/{stockTag}")
	public Notification addPostOrder(@PathVariable String stockTag, @Valid @RequestBody PostRequest postRequest) {
		
		return orderBookService.addPostOrder(stockTag, postRequest);
		
	}
	
	
	@ExceptionHandler(IllegalArgumentException.class)
	void handleBadRequests(IllegalArgumentException e, HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.BAD_REQUEST.value());
	}
	
} // end class OrderController
