package ie.gmit.sw.fyp.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

//import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.junit4.SpringRunner;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.MarketOrder;
import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.matchengine.PostOrderType;
import ie.gmit.sw.fyp.matchengine.PostRequest;
import ie.gmit.sw.fyp.model.OrderBook;
import ie.gmit.sw.fyp.model.OrderStatus;




@RunWith(SpringRunner.class)
@SpringBootTest
@DependsOn({"userService", "orderBookService"})
public class MarketOrderTest {
	private Calendar date = new GregorianCalendar();
	private Instant timeStamp;
	private PostRequest postRequest;
	private MarketOrder marketOrder;
	
	
	
	
	@Before
	public void setUp() throws Exception {
		date.add(Calendar.DAY_OF_MONTH, 1);
//		timeStamp = new Timestamp(date.getTimeInMillis());
		timeStamp = Instant.now().plus(1, ChronoUnit.DAYS	);
		
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		
		marketOrder = new MarketOrder(postRequest);
		
	}

	
	@Test
	public void testMarketOrder() {
		MarketOrder emptyMarketOrder = new MarketOrder();
		
		assertThat("MarketOrder empty constructor", emptyMarketOrder, hasProperty("properties"));
		assertThat("MarketOrder empty constructor", emptyMarketOrder.getId(), not(isEmptyString()));
		
		Instant timestamp = emptyMarketOrder.getTimestamp();
		assertThat("MarketOrder empty constructor", timestamp.isBefore(Instant.now()), is(true));
		
		assertThat("MarketOrder empty constructor", emptyMarketOrder.getStatus(), is(OrderStatus.CREATED));
		
	}

	
	@Test
	public void testMarketOrderPostRequest() {
		assertThat("MarketOrder PostRequest constructor", marketOrder, hasProperty("properties"));
		assertThat("MarketOrder PostRequest constructor", marketOrder.getId(), not(isEmptyString()));
		
		Instant timestamp = marketOrder.getTimestamp();
		assertThat("MarketOrder PostRequest constructor", timestamp.isBefore(Instant.now()), is(true));
		
		assertThat("MarketOrder PostRequest constructor", marketOrder.getStatus(), is(OrderStatus.CREATED));
		
	}

	
	@Test
	public void testMarketOrderMarketOrder() {
		PostRequest otherPostRequest = new PostRequest();
		otherPostRequest.setUserId("uiahfu938");
		otherPostRequest.setStockTag("GOOG");
		otherPostRequest.setType(PostOrderType.BUY);
		otherPostRequest.setOrderCondition(PostOrderCondition.MARKET);
		otherPostRequest.setVolume(10);
		otherPostRequest.setPartialFill(true);
		
		MarketOrder otherMarketOrder = new MarketOrder(otherPostRequest);
		
		assertThat("MarketOrder PostRequest constructor", otherMarketOrder, hasProperty("properties"));
		assertThat("MarketOrder PostRequest constructor", otherMarketOrder.getId(), not(isEmptyString()));
		
		Instant timestamp = otherMarketOrder.getTimestamp();
		assertThat("MarketOrder PostRequest constructor", timestamp.isBefore(Instant.now()), is(true));
		
		assertThat("MarketOrder PostRequest constructor", otherMarketOrder.getStatus(), is(OrderStatus.CREATED));
		
	}

	
	@Test
	public void testGetId() {
		assertThat("MarketOrder getId", marketOrder.getId(), not(isEmptyString()));
		
	}
	
	
	@Test
	public void testSetId() {
		UUID uuid = UUID.randomUUID();
		marketOrder.setId(uuid.toString());
		
		assertThat("MarketOrder setId", marketOrder.getId(), containsString(uuid.toString()));
		
	}

	
	@Test
	public void testGetTimestamp() {
		Instant timestamp = (Instant) marketOrder.getTimestamp();
		
		assertThat("MarketOrder getTimeStamp", timestamp.isBefore(Instant.now()), is(true));
		assertThat("MarketOrder getTimeStamp", timestamp.isAfter(Instant.now()), is(false));
		
	}

	
	@Test
	public void testSetTimestamp() {
		date.add(Calendar.DAY_OF_MONTH, -2);
//		timeStamp.setTime(date.getTimeInMillis());
		timeStamp.minus(Duration.ofDays(2));
		
		marketOrder.setTimestamp(timeStamp);
		
		assertThat("MarketOrder setTimeStamp", marketOrder.getTimestamp().isBefore(Instant.now()), is(true));
		assertThat("MarketOrder setTimeStamp", marketOrder.getTimestamp().isAfter(Instant.now()), is(false));
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetTimestamp_InvalidDate() {
//		Invalid date
		marketOrder.setTimestamp(timeStamp);
		
	}

	
	@Test
	public void testStatus() {
		marketOrder.setStatus(OrderStatus.ACCEPTED);
		assertThat("MarketOrder status (ACCEPTED)", marketOrder.getStatus(), is(OrderStatus.ACCEPTED));
		
		marketOrder.setStatus(OrderStatus.CREATED);
		assertThat("MarketOrder status (CREATED)", marketOrder.getStatus(), is(OrderStatus.CREATED));
		
		marketOrder.setStatus(OrderStatus.EXPIRED);
		assertThat("MarketOrder status (EXPIRED)", marketOrder.getStatus(), is(OrderStatus.EXPIRED));
		
		marketOrder.setStatus(OrderStatus.MATCHED);
		assertThat("MarketOrder status (MATCHED)", marketOrder.getStatus(), is(OrderStatus.MATCHED));
		
		marketOrder.setStatus(OrderStatus.PARTIALLYMATCHED);
		assertThat("MarketOrder status (PARTIALLYMATCHED)", marketOrder.getStatus(), is(OrderStatus.PARTIALLYMATCHED));
		
		marketOrder.setStatus(OrderStatus.REJECTED);
		assertThat("MarketOrder status (REJECTED)", marketOrder.getStatus(), is(OrderStatus.REJECTED));
		
		marketOrder.setStatus(OrderStatus.UNKNOWN);
		assertThat("MarketOrder status (UNKNOWN)", marketOrder.getStatus(), is(OrderStatus.UNKNOWN));
		
	}


	@Test
	public void testAttachTo() {
		OrderBook orderBook = new OrderBook("APPL");
		
		marketOrder.attachTo(orderBook);
		
		assertThat("MarketOrder attachTo (buyLimitOrders)", orderBook.getBuyLimitOrders(), equalTo(Collections.EMPTY_MAP));
		assertThat("MarketOrder attachTo (buyBuyStopLoss)", orderBook.getBuyStopLoss(), equalTo(Collections.EMPTY_MAP));
		assertThat("MarketOrder attachTo (sellLimitOrders)", orderBook.getSellLimitOrders(), equalTo(Collections.EMPTY_MAP));
		assertThat("MarketOrder attachTo (SellStopLoss)", orderBook.getSellStopLoss(), equalTo(Collections.EMPTY_MAP));
		
	}

	
	@Test
	public void testMatches() {
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setVolume(5);
		postRequest.setPartialFill(true);
		postRequest.setExpirationTime(timeStamp);
		
		LimitOrder limitOrder = new LimitOrder(postRequest);
		
		assertThat("MarketOrder matches (partial fill - 5 shares out of 10 shares)", marketOrder.matches(limitOrder), is(true));
		
//		marketOrder.getProperties().remove("price");
		marketOrder.setPartialFill(false);
		assertThat("MarketOrder matches (No partial fill - 5 shares out of 10 shares)", marketOrder.matches(limitOrder), is(false));
		
		limitOrder.setVolume(10);
		assertThat("MarketOrder matches (No partial fill - 10 shares out of 10 shares)", marketOrder.matches(limitOrder), is(true));
		
	}

	
	@Test
	public void testEqualsObject() {
		MarketOrder nullMarketOrder = null;
		assertThat("MarketOrder equals (null)", marketOrder.equals(nullMarketOrder), is(false));
		
		MarketOrder sameMarketOrder = marketOrder;
		assertThat("MarketOrder equals (same orders by pointer)", marketOrder.equals(sameMarketOrder), is(true));
		
		PostRequest otherPostRequest = new PostRequest();
		otherPostRequest.setUserId("uiahfu938");
		otherPostRequest.setStockTag("GOOG");
		otherPostRequest.setType(PostOrderType.BUY);
		otherPostRequest.setOrderCondition(PostOrderCondition.MARKET);
		otherPostRequest.setVolume(10);
		otherPostRequest.setPartialFill(true);
		
		MarketOrder otherMarketOrder = new MarketOrder(otherPostRequest);
		assertThat("MarketOrder equals (different orders)", marketOrder.equals(otherMarketOrder), is(false));
		
		otherMarketOrder.setId(marketOrder.getId());
		assertThat("MarketOrder equals (different orders)", marketOrder.equals(otherMarketOrder), is(false));
		
		otherMarketOrder.setUserId(marketOrder.getUserId());
		assertThat("MarketOrder equals (different orders)", marketOrder.equals(otherMarketOrder), is(false));
		
		otherMarketOrder.setType(marketOrder.getType());
		assertThat("MarketOrder equals (different orders)", marketOrder.equals(otherMarketOrder), is(false));
		
		otherMarketOrder.setStockTag(marketOrder.getStockTag());
		assertThat("MarketOrder equals (same orders by properties)", marketOrder.equals(otherMarketOrder), is(true));
		
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(timeStamp);
		LimitOrder limitOrder = new LimitOrder(postRequest);
		assertThat("MarketOrder equals (different class)", marketOrder.equals(limitOrder), is(false));
		
//		marketOrder.setProperties(null);
//		assertThat("MarketOrder equals (null properties map)", marketOrder.equals(otherMarketOrder), is(false));
		
	}

}
