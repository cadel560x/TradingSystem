package ie.gmit.sw.fyp.me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class OrderController {
	@Autowired
	private OrderBookService orderBookService;
	
	
	
	
	@RequestMapping(method=RequestMethod.POST, value="/AAPL")
	public Notification addOrder(@RequestBody OrderRequest orderRequest) {
		return orderBookService.addOrder(orderRequest);
		
	}
	
}
