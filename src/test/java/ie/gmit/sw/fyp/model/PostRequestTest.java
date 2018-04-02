package ie.gmit.sw.fyp.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ie.gmit.sw.fyp.me.PostOrderCondition;
import ie.gmit.sw.fyp.me.PostOrderType;
import ie.gmit.sw.fyp.me.PostRequest;
import ie.gmit.sw.fyp.order.StockService;
import ie.gmit.sw.fyp.order.UserService;




public class PostRequestTest {
	private Calendar date;
	private PostRequest postRequest;
	@SuppressWarnings("unused")
	private StockService stockService = new StockService();
	@SuppressWarnings("unused")
	private UserService userService = new UserService();

	
	
	
	@Before
	public void setUp() throws Exception {
		postRequest = new PostRequest();
		
	}

	
	@Test
	public void testPostRequest() {
		PostRequest postRequest = new PostRequest();
		
		assertNotNull("PostRequest empty constructor", postRequest);
		
	}
	

	@Test
	public void testGetExpirationTime() {
		date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp timestamp = new Timestamp(date.getTimeInMillis());
		
		postRequest.setExpirationTime(timestamp);
		
		assertEquals("PostRequest getExpirationTime", timestamp, postRequest.getExpirationTime());
		
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testSetExpirationTime() {
//		Invalid expiration time
		date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, -1);
		
		postRequest.setExpirationTime(new Timestamp(date.getTimeInMillis()));
		
	}
	
	
	@Test
	public void testStopPrice_RoundingAt_5() {
		postRequest.setStopPrice(2.123456789f);
		
		assertEquals("PostRequest getStopPrice (rounding at 5/100000)", 2.1234, postRequest.getStopPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testStopPrice_NotRounding() {
		postRequest.setStopPrice(2.123444444f);
		
		assertEquals("PostRequest getStopPrice (not rounding)", 2.1234, postRequest.getStopPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testStopPrice_Rounding_Above() {
		postRequest.setStopPrice(2.123567891f);
		
		assertEquals("PostRequest getStopPrice (rounding above 5/100000)", 2.1236, postRequest.getStopPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testGetStopPrice() {
		postRequest.setStopPrice(2.3f);
		
		assertEquals("PostRequest getStopPrice", postRequest.getStopPrice(), 2.3f, 0002f);
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetStopPrice() {
//		Invalid price
		postRequest.setStopPrice(-1.4f);
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetStopPriceAt_0() {
//		Invalid price
		postRequest.setStopPrice(0);
		
	}
	
	
	@Test
	public void testCheckProperties_MARKET() {
		List<String> listProperties = new ArrayList<>(Arrays.asList("userId", "stockTag", "type", "condition", "volume", "partialFill"));
		
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		
		assertFalse("PostRequest checkProperties MARKET (invalid)", postRequest.checkProperties(listProperties));
		
		postRequest.setPartialFill(true);
		
		assertTrue("PostRequest checkProperties MARKET (valid)", postRequest.checkProperties(listProperties));
		
	}
	
	
	@Test
	public void testCheckProperties_LIMIT() {
		List<String> listProperties = new ArrayList<>(Arrays.asList("userId", "stockTag", "type", "condition", "volume", "partialFill", "price", "expirationTime"));
		
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setCondition(PostOrderCondition.LIMIT);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		postRequest.setPrice(2.5f);
		
		assertFalse("PostRequest checkProperties LIMIT (invalid)", postRequest.checkProperties(listProperties));
		
		date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
		postRequest.setExpirationTime(new Timestamp(date.getTimeInMillis()));
		
		assertTrue("PostRequest checkProperties LIMIT (valid)", postRequest.checkProperties(listProperties));
		
	}
	
	
	@Test
	public void testCheckProperties_STOPLOSS() {
		List<String> listProperties = new ArrayList<>(Arrays.asList("userId", "stockTag", "type", "condition", "volume", "partialFill", "price", "expirationTime", "stopPrice"));
		date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
		
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setCondition(PostOrderCondition.STOPLOSS);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(new Timestamp(date.getTimeInMillis()));
		
		assertFalse("PostRequest checkProperties STOPLOSS (invalid)", postRequest.checkProperties(listProperties));
		
		postRequest.setStopPrice(2.3f);
		
		assertTrue("PostRequest checkProperties STOPLOSS (valid)", postRequest.checkProperties(listProperties));
		
	}

	
	@Test
	public void testGetProperties() {
		assertNotNull("PostRequest getProperties", postRequest.getProperties());
		
	}

	
	@Test
	public void testSetProperties() {
		Map<String, Object> testMap = new HashMap<>();
		
		postRequest.setProperties(testMap);
		
		assertEquals("PostRequest setProperties", testMap, postRequest.getProperties());
		
	}

	
	@Test
	public void testGetUserId() {
		postRequest.setUserId("dfgjkaga9");
		
		assertThat("PostRequest getUserId (valid userId)", postRequest.getUserId(), containsString("dfgjkaga9"));
		
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testSetUserId() {
//		Invalid userId
		postRequest.setUserId("asdfadsf");
		
	}


	@Test
	public void testGetStockTag() {
		postRequest.setStockTag("AAPL");
		
		assertThat("PostRequest getStockTag (valid tag)", postRequest.getStockTag(), containsString("AAPL"));
		
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetStockTag() {
//		Invalid tag
		postRequest.setStockTag("MSFT");
		
	}

	
	@Test
	public void testGetType() {
		postRequest.setType(PostOrderType.BUY);
		
		assertEquals("PostRequest getType (using BUY)", PostOrderType.BUY, postRequest.getType());
		
	}

	
	@Test
	public void testSetType() {
		postRequest.setType(PostOrderType.SELL);
		
		assertEquals("PostRequest setType (using SELL)", PostOrderType.SELL, postRequest.getType());
		
	}
	

	@Test
	public void testCondition_MARKET() {
		postRequest.setCondition(PostOrderCondition.MARKET);
		
		assertEquals("PostRequest condition MARKET", PostOrderCondition.MARKET, postRequest.getCondition());
		
	}
	
	@Test
	public void testCondition_LIMIT() {
		postRequest.setCondition(PostOrderCondition.LIMIT);
		
		assertEquals("PostRequest condition LIMIT", PostOrderCondition.LIMIT, postRequest.getCondition());
		
	}
	
	
	@Test
	public void testCondition_STOPLOSS() {
		postRequest.setCondition(PostOrderCondition.STOPLOSS);
		
		assertEquals("PostRequest condition STOPLOSS", PostOrderCondition.STOPLOSS, postRequest.getCondition() );
		
	}
	

	@Test
	public void testPrice_RoundingAt_5() {
		postRequest.setPrice(2.123456789f);
		
		assertEquals("PostRequest getPrice (rounding at 5/100000)", 2.1234, postRequest.getPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testPrice_NotRounding() {
		postRequest.setPrice(2.123444444f);
		
		assertEquals("PostRequest getPrice (not rounding)", 2.1234, postRequest.getPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testPrice_Rounding_Above() {
		postRequest.setPrice(2.123567891f);
		
		assertEquals("PostRequest getPrice (rounding above 5/100000)", 2.1236, postRequest.getPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testGetPrice() {
		postRequest.setPrice(2.5f);
		
		assertEquals("PostRequest getPrice", 2.5, postRequest.getPrice(), 0.0002);
		
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testSetPrice() {
//		Invalid price
		postRequest.setPrice(-1.4f);
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetPriceAt_0() {
//		Invalid price
		postRequest.setPrice(0);
		
	}
	

	@Test
	public void testGetVolume() {
		postRequest.setVolume(10);
		
		assertEquals("PostRequest getVolume", 10, postRequest.getVolume());
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetVolume() {
//		Invalid volume
		postRequest.setVolume(-10);
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetVolumeAt_0() {
//		Invalid volume
		postRequest.setVolume(0);
		
	}
	
	
	@Test
	public void testIsPartialFill() {
		postRequest.setPartialFill(true);
		
		assertEquals("PostRequest isPartialFill (using true)", true, postRequest.isPartialFill());
		
	}
	
	
	@Test
	public void testSetPartialFill() {
		postRequest.setPartialFill(false);
		
		assertEquals("PostRequest setPartialFill (using false)", false, postRequest.isPartialFill());
		
	}
	
	
	@Test
	public void testIsBuy() {
		postRequest.setType(PostOrderType.BUY);
		
		assertEquals("PostRequest isBuy", true, postRequest.isBuy());
		assertEquals("PostRequest isBuy (using isSell == false)", false, postRequest.isSell());
		
	}
	
	
	@Test
	public void testIsSell() {
		postRequest.setType(PostOrderType.SELL);
		
		assertEquals("PostRequest isSell", true, postRequest.isSell());
		assertEquals("PostRequest isSell (using isBuy == false)", false, postRequest.isBuy());
		
	}

}
