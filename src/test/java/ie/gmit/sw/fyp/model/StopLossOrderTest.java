package ie.gmit.sw.fyp.model;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.matchengine.PostOrderType;
import ie.gmit.sw.fyp.matchengine.PostRequest;
import ie.gmit.sw.fyp.matchengine.StopLossOrder;
import ie.gmit.sw.fyp.model.OrderBook;




@RunWith(SpringRunner.class)
@SpringBootTest
public class StopLossOrderTest {
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
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		postRequest.setExpirationTime(timeStamp);
		postRequest.setStopPrice(2.3f);
		
		stopLossOrder = new StopLossOrder(postRequest);
		
	}
	

	@Test
	public void testAttachTo() {
		OrderBook orderBook = new OrderBook("APPL");
		
		stopLossOrder.attachTo(orderBook);
		
		assertThat("StopLossOrder attachTo (buyLimitOrders)", orderBook.getBuyLimitOrders(), equalTo(Collections.EMPTY_MAP));
		assertThat("StopLossOrder attachTo (buyBuyStopLoss)", orderBook.getBuyStopLoss(), equalTo(Collections.EMPTY_MAP));
		assertThat("StopLossOrder attachTo (SellStopLoss)", orderBook.getSellStopLoss(), not(equalTo(Collections.EMPTY_MAP)));
		assertThat("StopLossOrder attachTo (sellLimitOrders)", orderBook.getSellLimitOrders(), not(equalTo(Collections.EMPTY_MAP)));
		
		orderBook.getSellLimitOrders().clear();
		orderBook.getSellStopLoss().clear();
		stopLossOrder.setType(PostOrderType.BUY);
		stopLossOrder.attachTo(orderBook);
		
		assertThat("StopLossOrder attachTo (SellStopLoss)", orderBook.getSellStopLoss(), equalTo(Collections.EMPTY_MAP));
		assertThat("StopLossOrder attachTo (sellLimitOrders)", orderBook.getSellLimitOrders(), equalTo(Collections.EMPTY_MAP));
		assertThat("StopLossOrder attachTo (buyBuyStopLoss)", orderBook.getBuyStopLoss(), not(equalTo(Collections.EMPTY_MAP)));
		assertThat("StopLossOrder attachTo (buyLimitOrders)", orderBook.getBuyLimitOrders(), not(equalTo(Collections.EMPTY_MAP)));
	}

	
	@Test
	public void testMatches() {
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.3f);
		postRequest.setVolume(5);
		postRequest.setPartialFill(true);
		postRequest.setExpirationTime(timeStamp);
		
		LimitOrder limitOrder = new LimitOrder(postRequest);
		
		assertThat("StopLossOrder matches (same price)", stopLossOrder.matches(limitOrder), is(true));
		
		limitOrder.setPrice(2.2999f);
		assertThat("StopLossOrder matches (buy limitOrder with lower price)", stopLossOrder.matches(limitOrder), is(true));
		limitOrder.setPrice(2.3001f);
		assertThat("StopLossOrder matches (buy limitOrder with higher price)", stopLossOrder.matches(limitOrder), is(false));
		
		stopLossOrder.setType(PostOrderType.BUY);
		limitOrder.setType(PostOrderType.SELL);
		
		limitOrder.setPrice(2.3f);
		assertThat("StopLossOrder matches (same price)", stopLossOrder.matches(limitOrder), is(true));
		
		limitOrder.setPrice(2.2999f);
		assertThat("StopLossOrder matches (sell limitOrder with lower price)", stopLossOrder.matches(limitOrder), is(false));
		limitOrder.setPrice(2.3001f);
		assertThat("StopLossOrder matches (sell limitOrder with higher price)", stopLossOrder.matches(limitOrder), is(true));
		
		
		stopLossOrder.setType(PostOrderType.SELL);
		
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.STOPLOSS);
		postRequest.setPrice(2.3f);
		postRequest.setVolume(5);
		postRequest.setPartialFill(true);
		postRequest.setExpirationTime(timeStamp);
		postRequest.setStopPrice(2.1f);
		StopLossOrder anotherStopLossOrder = new StopLossOrder(postRequest);
		
		assertThat("StopLossOrder matches (same price)", stopLossOrder.matches(anotherStopLossOrder), is(true));
		
		anotherStopLossOrder.setPrice(2.2999f);
		assertThat("StopLossOrder matches (buy limitOrder with lower price)", stopLossOrder.matches(anotherStopLossOrder), is(true));
		anotherStopLossOrder.setPrice(2.3001f);
		assertThat("StopLossOrder matches (buy limitOrder with higher price)", stopLossOrder.matches(anotherStopLossOrder), is(false));
		
		stopLossOrder.setType(PostOrderType.BUY);
		anotherStopLossOrder.setType(PostOrderType.SELL);
		
		anotherStopLossOrder.setPrice(2.3f);
		assertThat("LimitOrder matches (same price)", stopLossOrder.matches(anotherStopLossOrder), is(true));
		
		anotherStopLossOrder.setPrice(2.2999f);
		assertThat("StopLossOrder matches (sell limitOrder with lower price)", stopLossOrder.matches(anotherStopLossOrder), is(false));
		anotherStopLossOrder.setPrice(2.3001f);
		assertThat("StopLossOrder matches (sell limitOrder with higher price)", stopLossOrder.matches(anotherStopLossOrder), is(true));
	
	}

	
	@Test
	public void testStopPrice_RoundingAt_5() {
		stopLossOrder.setStopPrice(2.123456789f);
		
		assertEquals("StopLossOrder getPrice (rounding at 5/100000)", 2.1234, stopLossOrder.getStopPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testStopLossOrderStopLossOrder() {
		StopLossOrder newStopLossOrder = new StopLossOrder(stopLossOrder);
		
		assertThat("StopLossOrder constructor (StopLossOrder)", newStopLossOrder.getProperties(), notNullValue() );
		assertThat("StopLossOrder constructor (StopLossOrder)", newStopLossOrder.getProperties(), is(stopLossOrder.getProperties()));
		
	}
	
	
	@Test
	public void testStopPrice_NotRounding() {
		stopLossOrder.setStopPrice(2.123444444f);
		
		assertEquals("StopLossOrder getPrice (not rounding)", 2.1234, stopLossOrder.getStopPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testStopPrice_Rounding_Above() {
		stopLossOrder.setStopPrice(2.123567891f);
		
		assertEquals("StopLossOrder getPrice (rounding above 5/100000)", 2.1236, stopLossOrder.getStopPrice(), 0.0002);
		
	}
	
	
	@Test
	public void testGetStopPrice() {
		assertEquals("StopLossOrder getPrice", 2.3, stopLossOrder.getStopPrice(), 0.0002);
		
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
