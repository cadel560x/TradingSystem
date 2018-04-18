package ie.gmit.sw.fyp.model;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import ie.gmit.sw.fyp.me.LimitOrder;
import ie.gmit.sw.fyp.me.PostOrderCondition;
import ie.gmit.sw.fyp.me.PostOrderType;
import ie.gmit.sw.fyp.me.PostRequest;
import ie.gmit.sw.fyp.order.OrderBook;
import ie.gmit.sw.fyp.order.StockService;
import ie.gmit.sw.fyp.order.UserService;




public class LimitOrderTest {
	@SuppressWarnings("unused")
	private StockService stockService = new StockService();
	@SuppressWarnings("unused")
	private UserService userService = new UserService();
	
	private Calendar date = new GregorianCalendar();
	private Timestamp timeStamp;
	private PostRequest postRequest;
	private LimitOrder limitOrder;

	
	
	
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
		
		limitOrder = new LimitOrder(postRequest);
		
	}

	
	@Test
	public void testHashCode() {
		assertEquals("LimitOrder hashCode", 25000, limitOrder.hashCode());
		
	}

	
	@Test
	public void testAttachTo() {
		OrderBook orderBook = new OrderBook("APPL");
		
		limitOrder.attachTo(orderBook);
		
		assertThat("LimitOrder attachTo (SellStopLoss)", orderBook.getSellStopLoss(), equalTo(Collections.EMPTY_MAP));
		assertThat("LimitOrder attachTo (buyBuyStopLoss)", orderBook.getBuyStopLoss(), equalTo(Collections.EMPTY_MAP));
		assertThat("LimitOrder attachTo (buyLimitOrders)", orderBook.getBuyLimitOrders(), equalTo(Collections.EMPTY_MAP));
		assertThat("LimitOrder attachTo (sellLimitOrders)", orderBook.getSellLimitOrders(), not(equalTo(Collections.EMPTY_MAP)));
		
		orderBook.getSellLimitOrders().clear();
		limitOrder.setType(PostOrderType.BUY);
		limitOrder.attachTo(orderBook);
		
		assertThat("LimitOrder attachTo (SellStopLoss)", orderBook.getSellStopLoss(), equalTo(Collections.EMPTY_MAP));
		assertThat("LimitOrder attachTo (buyBuyStopLoss)", orderBook.getBuyStopLoss(), equalTo(Collections.EMPTY_MAP));
		assertThat("LimitOrder attachTo (sellLimitOrders)", orderBook.getSellLimitOrders(), equalTo(Collections.EMPTY_MAP));
		assertThat("LimitOrder attachTo (buyLimitOrders)", orderBook.getBuyLimitOrders(), not(equalTo(Collections.EMPTY_MAP)));
		
	}

	
	@Test
	public void testMatches() {
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setVolume(5);
		postRequest.setPartialFill(true);
		postRequest.setExpirationTime(timeStamp);
		
		LimitOrder anotherLimitOrder = new LimitOrder(postRequest);
		
		assertThat("LimitOrder matches (same price)", limitOrder.matches(anotherLimitOrder), is(true));
		
		anotherLimitOrder.setPrice(2.5001f);
		assertThat("LimitOrder matches (buy limitOrder with higher price)", limitOrder.matches(anotherLimitOrder), is(true));
		anotherLimitOrder.setPrice(2.4999f);
		assertThat("LimitOrder matches (buy limitOrder with lower price)", limitOrder.matches(anotherLimitOrder), is(false));
		
		limitOrder.setType(PostOrderType.BUY);
		anotherLimitOrder.setType(PostOrderType.SELL);
		
		anotherLimitOrder.setPrice(2.5f);
		assertThat("LimitOrder matches (same price)", limitOrder.matches(anotherLimitOrder), is(true));
		
		anotherLimitOrder.setPrice(2.5001f);
		assertThat("LimitOrder matches (sell limitOrder with higher price)", limitOrder.matches(anotherLimitOrder), is(false));
		anotherLimitOrder.setPrice(2.4999f);
		assertThat("LimitOrder matches (sell limitOrder with lower price)", limitOrder.matches(anotherLimitOrder), is(true));
		
	}


	@Test
	public void testLimitOrderLimitOrderOrder() {
		LimitOrder newLimitOrder = new LimitOrder(limitOrder);
		
		assertThat("LimitOrder constructor (LimitOrder)", newLimitOrder.getProperties(), notNullValue() );
		assertThat("LimitOrder constructor (LimitOrder)", newLimitOrder.getProperties(), is(limitOrder.getProperties()));
		
	}

	
	@Test
	public void testPrice_RoundingAt_5() {
		limitOrder.setPrice(2.123456789f);
		
		assertEquals("LimitOrder getPrice (rounding at 5/100000)", 2.1234, limitOrder.getPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testPrice_NotRounding() {
		limitOrder.setPrice(2.123444444f);
		
		assertEquals("LimitOrder getPrice (not rounding)", 2.1234, limitOrder.getPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testPrice_Rounding_Above() {
		limitOrder.setPrice(2.123567891f);
		
		assertEquals("LimitOrder getPrice (rounding above 5/100000)", 2.1236, limitOrder.getPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testGetPrice() {
		limitOrder.setPrice(2.5f);
		
		assertEquals("LimitOrder getPrice", 2.5, limitOrder.getPrice(), 0.0002);
		
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testSetPrice() {
//		Invalid price
		limitOrder.setPrice(-1.4f);
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetPriceAt_0() {
//		Invalid price
		limitOrder.setPrice(0);
		
	}

	
	@Test
	public void testGetExpirationTime() {
		Timestamp timestamp = limitOrder.getExpirationTime();
		
		assertThat("MarketOrder getExpirationTime", timestamp.before(new Date()), is(false));
		assertThat("MarketOrder getExpirationTime", timestamp.after(new Date()), is(true));
		
	}

	
	@Test
	public void testSetExpirationTime() {
		date.add(Calendar.DAY_OF_MONTH, 1);
		timeStamp.setTime(date.getTimeInMillis());
		
		limitOrder.setExpirationTime(timeStamp);
		
		assertThat("MarketOrder setExpirationTime", limitOrder.getExpirationTime().before(new Date()), is(false));
		assertThat("MarketOrder setExpirationTime", limitOrder.getExpirationTime().after(new Date()), is(true));
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetExpirationTime_InvalidDate() {
//		Invalid date
		date.add(Calendar.DAY_OF_MONTH, -2);
		timeStamp.setTime(date.getTimeInMillis());
		
		limitOrder.setExpirationTime(timeStamp);
		
	}

}
