package ie.gmit.sw.fyp.model;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import ie.gmit.sw.fyp.me.PostOrderCondition;
import ie.gmit.sw.fyp.me.PostOrderType;
import ie.gmit.sw.fyp.me.PostRequest;
import ie.gmit.sw.fyp.me.StopLossOrder;
import ie.gmit.sw.fyp.order.StockService;
import ie.gmit.sw.fyp.order.UserService;




public class StopLossOrderTest {
	@SuppressWarnings("unused")
	private StockService stockService = new StockService();
	@SuppressWarnings("unused")
	private UserService userService = new UserService();
	
	private Calendar date = new GregorianCalendar();
	private Timestamp timeStamp;
	private PostRequest postRequest;
	private StopLossOrder stopLossOrder;

	
	
	
	@Before
	public void setUp() throws Exception {
		date.add(Calendar.DAY_OF_MONTH, 1);
		timeStamp = new Timestamp(date.getTimeInMillis());
		
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		postRequest.setExpirationTime(timeStamp);
		postRequest.setStopPrice(2.3f);
		
		stopLossOrder = new StopLossOrder(postRequest);
		
	}
	

	@Test
	public void testAttachTo() {
		fail("Not yet implemented");
	}

	@Test
	public void testMatches() {
		fail("Not yet implemented");
	}

	@Test
	public void testStopPrice_RoundingAt_5() {
		stopLossOrder.setStopPrice(2.123456789f);
		
		assertEquals("StopLossOrder getPrice (rounding at 5/100000)", 2.1234, stopLossOrder.getPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testStopPrice_NotRounding() {
		stopLossOrder.setStopPrice(2.123444444f);
		
		assertEquals("StopLossOrder getPrice (not rounding)", 2.1234, stopLossOrder.getPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testStopPrice_Rounding_Above() {
		stopLossOrder.setStopPrice(2.123567891f);
		
		assertEquals("StopLossOrder getPrice (rounding above 5/100000)", 2.1236, stopLossOrder.getPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testGetStopPrice() {
		assertEquals("StopLossOrder getPrice", 2.3, stopLossOrder.getPrice(), 0.0002);
		
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testSetStopPrice() {
//		Invalid price
		stopLossOrder.setStopPrice(-1.4f);
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetStopPriceAt_0() {
//		Invalid price
		stopLossOrder.setStopPrice(0);
		
	}

}
