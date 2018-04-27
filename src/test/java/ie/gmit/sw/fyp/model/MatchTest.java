package ie.gmit.sw.fyp.model;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.MarketOrder;
import ie.gmit.sw.fyp.matchengine.OrderMatch;
import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.matchengine.PostOrderType;
import ie.gmit.sw.fyp.matchengine.PostRequest;
import ie.gmit.sw.fyp.matchengine.StopLossOrder;
import ie.gmit.sw.fyp.model.OrderStatus;
import ie.gmit.sw.fyp.services.StockService;
import ie.gmit.sw.fyp.services.UserService;




public class MatchTest {
	private Calendar date = new GregorianCalendar();
	@SuppressWarnings("unused")
	private StockService stockService = new StockService();
	@SuppressWarnings("unused")
	private UserService userService = new UserService();
	private Timestamp timeStamp;
	
	private MarketOrder marketOrder;
	private LimitOrder limitOrder;
	private StopLossOrder stopLossOrder;
	private PostRequest postRequest;
	private OrderMatch match;
	
	
	
	
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
		stopLossOrder.setStatus(OrderStatus.MATCHED);
		
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		postRequest.setExpirationTime(timeStamp);
		limitOrder = new LimitOrder(postRequest);
		limitOrder.setStatus(OrderStatus.MATCHED);
		
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		marketOrder = new MarketOrder(postRequest);
		marketOrder.setStatus(OrderStatus.MATCHED);
		
		match = new OrderMatch(limitOrder, marketOrder);
		
	}

	@Test
	public void testMatch() {
		OrderMatch emptyMatch = new OrderMatch();
		
		assertThat("Match empty constructor", emptyMatch.getId(), not(isEmptyString()));
		
		Timestamp timestamp = emptyMatch.getTimestamp();
		assertThat("Match empty constructor", timestamp.before(new Date()), is(true));
		
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testMatchMarketOrderMarketOrder_wrongOrder() {
//		Invalid parameter order
		@SuppressWarnings("unused")
		OrderMatch wrongOrder = new OrderMatch(marketOrder, limitOrder);
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testMatchMarketOrderMarketOrder_wrongBuyBuy() {
//		Invalid parameter order
		@SuppressWarnings("unused")
		OrderMatch wrongOrder = new OrderMatch(marketOrder, marketOrder);
		
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testMatchMarketOrderMarketOrder_wrongSellSell() {
//		Invalid parameter order
		@SuppressWarnings("unused")
		OrderMatch wrongOrder = new OrderMatch(limitOrder, limitOrder);
		
	}
	
	
	@Test
	public void testMatchMarketOrderMarketOrder() {
		OrderMatch newMatch = new OrderMatch(stopLossOrder, marketOrder);
		
		// The property in the 'Match' class is 'Id' with capital I, but 'hasProperty' seems to work with properties whose names start with lower case letters
		assertThat("Match constructor(MarketOrder, MarketOrder)", newMatch, hasProperty("id"));
		assertThat("Match constructor(MarketOrder, MarketOrder)", newMatch.getId(), notNullValue());
		
		assertThat("Match constructor(MarketOrder, MarketOrder)", newMatch, hasProperty("timestamp"));
		assertThat("Match constructor(MarketOrder, MarketOrder)", newMatch.getTimestamp().before(new Date()), is(true));
		assertThat("Match constructor(MarketOrder, MarketOrder)", newMatch.getTimestamp().after(new Date()), is(false));
		
		assertThat("Match constructor(MarketOrder, MarketOrder)", newMatch, hasProperty("sellOrder"));
		assertThat("Match constructor(MarketOrder, MarketOrder)", newMatch, hasProperty("buyOrder"));
		
		assertThat("Match constructor(MarketOrder, MarketOrder)", newMatch.getFilledShares(), is(10));
		
	}

	
	@Test
	public void testGetId() {
		assertThat("Match getId", match.getId(), not(isEmptyString()));
		
	}

	
	@Test
	public void testGetSellOrder() {
		assertThat("Match getSellOrder", match.getSellOrder(), is(limitOrder));
		
	}

	
	@Test
	public void testSetSellOrder() {
		match.setSellOrder(stopLossOrder);
		assertThat("Match setSellOrder", match.getSellOrder(), is(stopLossOrder));
		
	}

	
	@Test
	public void testSetBuyOrder() {
		marketOrder.setType(PostOrderType.SELL);
		stopLossOrder.setType(PostOrderType.BUY);
		
		match.setBuyOrder(stopLossOrder);
		assertThat("Match setSellOrder", match.getBuyOrder(), is(stopLossOrder));
		
	}

	
	@Test
	public void testGetBuyOrder() {
		assertThat("Match getBuyOrder", match.getBuyOrder(), is(marketOrder));
		
	}

	
	@Test
	public void testGetTimestamp() {
		timeStamp = match.getTimestamp();
		
		assertThat("Match getTimestamp", timeStamp.before(new Date()), is(true));
		
	}
	

	@Test
	public void testGetFilledShares() {
		assertThat("Match getFilledShares", match.getFilledShares(), is(10));
		
	}

	
	@Test
	public void testSetFilledShares() {
		match.getSellOrder().setVolume(10);
		match.getBuyOrder().setVolume(5);
		
		match.setFilledShares();
		assertThat("Match getFilledShares", match.getFilledShares(), is(5));
		
		match.getSellOrder().setVolume(1);
		match.getBuyOrder().setVolume(3);
		
		match.setFilledShares();
		assertThat("Match getFilledShares", match.getFilledShares(), is(1));
		
		match.getSellOrder().setVolume(5);
		match.getBuyOrder().setVolume(4);
		
		match.setFilledShares();
		assertThat("Match getFilledShares", match.getFilledShares(), is(4));
		
	}

	
	@Test
	public void testSetVolumes() {
		match.getSellOrder().setVolume(10);
		match.getBuyOrder().setVolume(5);
		
		match.setFilledShares();
		match.setVolumes();
		assertThat("Match setVolumes (of sell order)", match.getSellOrder().getVolume(), is(5));
		assertThat("Match setVolumes (of buy order)", match.getBuyOrder().getVolume(), is(5));
		
		match.getSellOrder().setVolume(1);
		match.getBuyOrder().setVolume(3);
		
		match.setFilledShares();
		match.setVolumes();
		assertThat("Match setVolumes (of sell order)", match.getSellOrder().getVolume(), is(1));
		assertThat("Match setVolumes (of buy order)", match.getBuyOrder().getVolume(), is(2));
		
		match.getSellOrder().setVolume(5);
		match.getBuyOrder().setVolume(4);
		
		match.setFilledShares();
		match.setVolumes();
		assertThat("Match setVolumes (of sell order)", match.getSellOrder().getVolume(), is(1));
		assertThat("Match setVolumes (of buy order)", match.getBuyOrder().getVolume(), is(4));
		
		match.getSellOrder().setVolume(1);
		match.getBuyOrder().setVolume(2);
		
		match.setFilledShares();
		match.setVolumes();
		assertThat("Match setVolumes (of sell order)", match.getSellOrder().getVolume(), is(1));
		assertThat("Match setVolumes (of buy order)", match.getBuyOrder().getVolume(), is(1));
		
		match.getSellOrder().setVolume(200);
		match.getBuyOrder().setVolume(175);
		
		match.setFilledShares();
		match.setVolumes();
		assertThat("Match setVolumes (of sell order)", match.getSellOrder().getVolume(), is(25));
		assertThat("Match setVolumes (of buy order)", match.getBuyOrder().getVolume(), is(175));
		
	}
	
	
	@Test
	public void testGetRemainingShares() {
		assertThat("Match getRemainingShares", match.getRemainingShares(), is(0));
		
		match.getSellOrder().setVolume(5);
		match.getBuyOrder().setVolume(4);
		assertThat("Match getRemainingShares", match.getRemainingShares(), is(1));
		
		match.getSellOrder().setVolume(1);
		match.getBuyOrder().setVolume(3);
		assertThat("Match getRemainingShares", match.getRemainingShares(), is(2));
		
		match.getSellOrder().setVolume(200);
		match.getBuyOrder().setVolume(175);
		assertThat("Match getRemainingShares", match.getRemainingShares(), is(25));
		
	}

}
