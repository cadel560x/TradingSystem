package ie.gmit.sw.fyp.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

//import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import ie.gmit.sw.fyp.TradingSystemApplication;
import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.matchengine.PostOrderType;
import ie.gmit.sw.fyp.matchengine.PostRequest;
import ie.gmit.sw.fyp.model.OrderBook;
import ie.gmit.sw.fyp.model.OrderStatus;
import ie.gmit.sw.fyp.notification.Notification;
import ie.gmit.sw.fyp.services.LimitOrderService;
import ie.gmit.sw.fyp.services.MarketOrderService;
import ie.gmit.sw.fyp.services.OrderBookService;
import ie.gmit.sw.fyp.services.OrderMatchService;
import ie.gmit.sw.fyp.services.StopLossOrderService;




@FixMethodOrder(MethodSorters.DEFAULT)
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderBookServiceTest {
	@Autowired
	private OrderBookService orderBookService;
	
	private String stockTag;
	private Calendar date;
	private Notification notification;
	private PostRequest postRequest;
	Instant timeStamp;
	
	

	
	@Before
	public void setUp() throws Exception {
		stockTag = "AAPL";
		date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
		timeStamp = Instant.now().plus(1, ChronoUnit.DAYS);
		
		
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag(stockTag);
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		
	}
	
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		String[] args = {};
		ApplicationContext applicationContext =  SpringApplication.run(TradingSystemApplication.class, args);
		
		MarketOrderService marketOrderService = (MarketOrderService) applicationContext.getBean("marketOrderService");
		LimitOrderService limitOrderService = (LimitOrderService) applicationContext.getBean("limitOrderService");
		StopLossOrderService stopLossOrderService = (StopLossOrderService) applicationContext.getBean("stopLossOrderService");
		OrderMatchService orderMatchService = (OrderMatchService) applicationContext.getBean("orderMatchService");
		
		// Clean DB from test orders
		// This order of deletion is important due to the foreign key constraints in 'order_match' table
		orderMatchService.deleteAll();
		marketOrderService.deleteAll();
		limitOrderService.deleteAll();
		stopLossOrderService.deleteAll();
		
	}
	
	
	@Test
	public void testCheckStockTag_ValidStockTag() {
		String stockId = "AAPL";
		assertThat(orderBookService.checkStockTag(stockId), is(true));
		
	}
	
	
	@Test
	public void testCheckStockTag_InvalidStockTag() {		
		String stockId = "MSFT";
		assertThat(orderBookService.checkStockTag(stockId), is(false));
		
	}
	
	
	@Test
	public void testAddPostOrder_LimitSell() {
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(timeStamp);
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("addPostOrder_LimitSell", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_LimitBuy() {
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.4f);
		postRequest.setExpirationTime(timeStamp);
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("addPostOrder_LimitBuy", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_StopLossSell() {
		postRequest.setOrderCondition(PostOrderCondition.STOPLOSS);
		postRequest.setPrice(2.5f);
		postRequest.setStopPrice(2.3f);
		postRequest.setExpirationTime(timeStamp);
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("addPostOrder_StopLossSell", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_StopLossBuy() {
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.STOPLOSS);
		postRequest.setPrice(2.4f);
		postRequest.setStopPrice(2.6f);
		postRequest.setExpirationTime(timeStamp);
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("addPostOrder_StopLossBuy", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_MarketBuy() {
		postRequest.setType(PostOrderType.BUY);
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("addPostOrder_MarketBuy", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_MarketSell() {
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("addPostOrder_MarketSell", notification.getMessage(), containsString("Request accepted: OrderId "));
		
	}
	
	
	@Test
	public void testAddPostOrder_ExpiredOrder() {
//		Calendar expiredDate = new GregorianCalendar();
//		expiredDate.add(Calendar.SECOND, 2);
		Instant expiredDate = Instant.now().plus(2, ChronoUnit.SECONDS);
		
		PostRequest anotherPostRequest = new PostRequest();
		anotherPostRequest.setUserId("dfgjkaga9");
		anotherPostRequest.setStockTag(stockTag);
		anotherPostRequest.setType(PostOrderType.BUY);
		anotherPostRequest.setOrderCondition(PostOrderCondition.LIMIT);
		anotherPostRequest.setPrice(2.5f);
		anotherPostRequest.setVolume(10);
		anotherPostRequest.setPartialFill(true);
		anotherPostRequest.setExpirationTime(expiredDate);
		LimitOrder expiredLimitOrder = new LimitOrder(anotherPostRequest);
		
		OrderBook orderBook = orderBookService.getOrderBooks().get(stockTag);
		expiredLimitOrder.attachTo(orderBook);
		
		System.out.println("Waiting three seconds for 'expiredLimitOrder' getting expired");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		notification = orderBookService.addPostOrder(stockTag, postRequest);
		System.err.println(notification.getMessage());
		assertThat("addPostOrder_ExpiredOrder", notification.getMessage(), containsString("Request accepted: OrderId "));
		assertThat("addPostOrder_ExpiredOrder", expiredLimitOrder.getStatus(), is(OrderStatus.EXPIRED));
		
	} // end testAddPostOrder_ExpiredOrder

} // end OrderBookServiceTest
