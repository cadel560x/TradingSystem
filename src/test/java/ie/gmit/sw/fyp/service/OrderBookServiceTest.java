package ie.gmit.sw.fyp.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ie.gmit.sw.fyp.me.PostOrderCondition;
import ie.gmit.sw.fyp.me.PostOrderType;
import ie.gmit.sw.fyp.me.PostRequest;
import ie.gmit.sw.fyp.notification.Notification;
import ie.gmit.sw.fyp.services.OrderBookService;



//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderBookServiceTest {
	@Autowired
	private OrderBookService orderBookService;
	
	private String stockTag;
	private Calendar date;
	private Notification notification;
	private PostRequest postRequest;
	
	

	
	@Before
	public void setUp() throws Exception {
		stockTag = "AAPL";
		date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
		
		
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag(stockTag);
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		
		
	}
	
	
	@Test
	public void testAddPostOrder_LimitSell() {
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(new Timestamp(date.getTimeInMillis()));
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("Request was accepted", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_LimitBuy() {
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.4f);
		postRequest.setExpirationTime(new Timestamp(date.getTimeInMillis()));
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("Request was accepted", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_StopLossSell() {
		postRequest.setOrderCondition(PostOrderCondition.STOPLOSS);
		postRequest.setPrice(2.5f);
		postRequest.setStopPrice(2.3f);
		postRequest.setExpirationTime(new Timestamp(date.getTimeInMillis()));
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("Request was accepted", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_StopLossBuy() {
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.STOPLOSS);
		postRequest.setPrice(2.4f);
		postRequest.setStopPrice(2.6f);
		postRequest.setExpirationTime(new Timestamp(date.getTimeInMillis()));
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("Request was accepted", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_MarketBuy() {
		postRequest.setType(PostOrderType.BUY);
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("Request was accepted", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_MarketSell() {
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("Request was accepted", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}

} // end OrderBookServiceTest
