package ie.gmit.sw.fyp.model;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ie.gmit.sw.fyp.matchengine.LimitOrder;
import ie.gmit.sw.fyp.matchengine.MarketOrder;
import ie.gmit.sw.fyp.matchengine.PostOrderCondition;
import ie.gmit.sw.fyp.matchengine.PostOrderType;
import ie.gmit.sw.fyp.matchengine.PostRequest;
import ie.gmit.sw.fyp.matchengine.StopLossOrder;
import ie.gmit.sw.fyp.model.OrderBook;




@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderBookTest {
	private OrderBook orderBook;
	
	@Rule
    public ErrorCollector errorCollector = new ErrorCollector();

	
	
	
	@Before
	public void setUp() throws Exception {
		orderBook = new OrderBook("AAPL");
		
	}

	
	@Test
	public void testOrderBook() {
		OrderBook newOrderBook = new OrderBook("GOOG");
		
		assertThat("OrderBook constructor(String)", newOrderBook, hasProperty("stockTag", equalTo("GOOG")));
		assertThat("OrderBook constructor(String)", newOrderBook, hasProperty("buyLimitOrders"));
		assertThat("OrderBook constructor(String)", newOrderBook, hasProperty("sellLimitOrders"));
//		assertThat("OrderBook constructor(String)", newOrderBook, hasProperty("buyStopLossOrders")); // don't know why it doesn't work
		assertThat("OrderBook constructor(String)", newOrderBook.getBuyStopLoss(), notNullValue());
//		assertThat("OrderBook constructor(String)", newOrderBook, hasProperty("sellStopLossOrders")); // don't know why it doesn't work
		assertThat("OrderBook constructor(String)", newOrderBook.getSellStopLoss(), notNullValue());
		assertThat("OrderBook constructor(String)", newOrderBook, hasProperty("matchedQueue"));
		
	}

	
	@Test
	public void testGetStockTag() {
		assertThat("OrderBook getStockTag", orderBook, hasProperty("stockTag", equalTo("AAPL")));
		
	}

	
	@Test
	public void testSetStockTag() {
		orderBook.setStockTag("MSFT");
		
		assertThat("OrderBook setStockTag", orderBook.getStockTag(), is("MSFT"));
		
	}

	
	@Test
	public void testGetBuyLimitOrders() {
		assertThat("OrderBook getBuyLimitOrders map", orderBook.getBuyLimitOrders(), equalTo(Collections.EMPTY_MAP));
		assertThat("OrderBook getBuyLimitOrders map", orderBook.getBuyLimitOrders(), instanceOf(ConcurrentSkipListMap.class));
		
		try {
			Field buyLimitOrdersFields = OrderBook.class.getDeclaredField("buyLimitOrders");
			ParameterizedType buyLimitOrdersFieldsType = (ParameterizedType) buyLimitOrdersFields.getGenericType();
	        Class<?> floatClass = (Class<?>) buyLimitOrdersFieldsType.getActualTypeArguments()[0];
	        
			ParameterizedType queueType = (ParameterizedType) buyLimitOrdersFieldsType.getActualTypeArguments()[1];
			Class<?> queueClass = (Class<?>) queueType.getRawType();
			Class<?> limitOrderClass = (Class<?>) queueType.getActualTypeArguments()[0];
			
			assertThat("OrderBook getBuyLimitOrders map key type", floatClass.getSimpleName(), is("Float"));
			assertThat("OrderBook getBuyLimitOrders map value type", queueClass.getSimpleName(), is("Queue"));
			assertThat("OrderBook getBuyLimitOrders queue type", limitOrderClass.getSimpleName(), is("LimitOrder"));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
	}
	

	@Test
	public void testGetSellLimitOrders() {
		assertThat("OrderBook getSellLimitOrders map", orderBook.getSellLimitOrders(), equalTo(Collections.EMPTY_MAP));
		assertThat("OrderBook getSellLimitOrders map", orderBook.getSellLimitOrders(), instanceOf(ConcurrentSkipListMap.class));
		
		try {
			Field sellLimitOrdersFields = OrderBook.class.getDeclaredField("sellLimitOrders");
			ParameterizedType sellLimitOrdersFieldsType = (ParameterizedType) sellLimitOrdersFields.getGenericType();
	        Class<?> floatClass = (Class<?>) sellLimitOrdersFieldsType.getActualTypeArguments()[0];
	        
			ParameterizedType queueType = (ParameterizedType) sellLimitOrdersFieldsType.getActualTypeArguments()[1];
			Class<?> queueClass = (Class<?>) queueType.getRawType();
			Class<?> limitOrderClass = (Class<?>) queueType.getActualTypeArguments()[0]; 
			
			assertThat("OrderBook getSellLimitOrders map key type", floatClass.getSimpleName(), is("Float"));
			assertThat("OrderBook getSellLimitOrders map value type", queueClass.getSimpleName(), is("Queue"));
			assertThat("OrderBook getSellLimitOrders queue type", limitOrderClass.getSimpleName(), is("LimitOrder"));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
			
	}

	
	@Test
	public void testGetBuyStopLoss() {
		assertThat("OrderBook getBuyStopLoss map", orderBook.getBuyStopLoss(), equalTo(Collections.EMPTY_MAP));
		assertThat("OrderBook getBuyStopLoss map", orderBook.getBuyStopLoss(), instanceOf(ConcurrentSkipListMap.class));
		
		try {
			Field buyStopLossOrdersFields = OrderBook.class.getDeclaredField("buyStopLossOrders");
			ParameterizedType buyStopLossOrdersFieldsType = (ParameterizedType) buyStopLossOrdersFields.getGenericType();
	        Class<?> floatClass = (Class<?>) buyStopLossOrdersFieldsType.getActualTypeArguments()[0];
	        
			ParameterizedType queueType = (ParameterizedType) buyStopLossOrdersFieldsType.getActualTypeArguments()[1];
			Class<?> queueClass = (Class<?>) queueType.getRawType();
			Class<?> limitOrderClass = (Class<?>) queueType.getActualTypeArguments()[0]; 
			
			assertThat("OrderBook getBuyStopLoss map key type", floatClass.getSimpleName(), is("Float"));
			assertThat("OrderBook getBuyStopLoss map value type", queueClass.getSimpleName(), is("Queue"));
			assertThat("OrderBook getBuyStopLoss queue type", limitOrderClass.getSimpleName(), is("StopLossOrder"));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
	}

	
	@Test
	public void testGetSellStopLoss() {
		assertThat("OrderBook getSellStopLoss map", orderBook.getBuyStopLoss(), equalTo(Collections.EMPTY_MAP));
		assertThat("OrderBook getSellStopLoss map", orderBook.getBuyStopLoss(), instanceOf(ConcurrentSkipListMap.class));
		
		try {
			Field sellStopLossOrdersFields = OrderBook.class.getDeclaredField("sellStopLossOrders");
			ParameterizedType sellStopLossOrdersFieldsType = (ParameterizedType) sellStopLossOrdersFields.getGenericType();
	        Class<?> floatClass = (Class<?>) sellStopLossOrdersFieldsType.getActualTypeArguments()[0];
	        
			ParameterizedType queueType = (ParameterizedType) sellStopLossOrdersFieldsType.getActualTypeArguments()[1];
			Class<?> queueClass = (Class<?>) queueType.getRawType();
			Class<?> limitOrderClass = (Class<?>) queueType.getActualTypeArguments()[0]; 
			
			assertThat("OrderBook getSellStopLoss map key type", floatClass.getSimpleName(), is("Float"));
			assertThat("OrderBook getSellStopLoss map value type", queueClass.getSimpleName(), is("Queue"));
			assertThat("OrderBook getSellStopLoss queue type", limitOrderClass.getSimpleName(), is("StopLossOrder"));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
	}

	
	@Test
	public void testGetMatchedQueue() {
		assertThat("OrderBook getMatchedQueue", orderBook.getMatchedQueue(), is(empty()));
		assertThat("OrderBook getMatchedQueue", orderBook.getMatchedQueue(), is(instanceOf(BlockingQueue.class)));
		
		try {
			Field matchedQueueFields = OrderBook.class.getDeclaredField("matchedQueue");
			ParameterizedType matchedQueueFieldsType = (ParameterizedType) matchedQueueFields.getGenericType();
	        Class<?> matchClass = (Class<?>) matchedQueueFieldsType.getActualTypeArguments()[0];
			
			assertThat("OrderBook matchedQueue map key type", matchClass.getSimpleName(), is("OrderMatch"));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
	}

	
	@Test
	public void testCheckRequest() {
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
//		Timestamp timeStamp = new Timestamp(date.getTimeInMillis());
		Instant timeStamp = Instant.now().plus(1, ChronoUnit.DAYS);
		
		PostRequest postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		assertThat("OrderBook checkRequest", orderBook.checkRequest(postRequest), is(false));
		
		postRequest.setPartialFill(true);
		assertThat("OrderBook checkRequest", orderBook.checkRequest(postRequest), is(true));
		
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(timeStamp);
		assertThat("OrderBook checkRequest", orderBook.checkRequest(postRequest), is(true));
		
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.STOPLOSS);
		postRequest.setStopPrice(2.3f);
		assertThat("OrderBook checkRequest", orderBook.checkRequest(postRequest), is(true));
		
	}

	
	@Test
	public void testCreateOrderPostRequest() {
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
//		Timestamp timeStamp = new Timestamp(date.getTimeInMillis());
		Instant timeStamp = Instant.now().plus(1, ChronoUnit.DAYS);
		
		PostRequest postRequest = new PostRequest();		
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		MarketOrder marketOrder = orderBook.createOrder(postRequest);
		assertThat("OrderBook createOrder(PostRequest)", marketOrder, isA(MarketOrder.class));
		
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(timeStamp);
		MarketOrder limitOrder = orderBook.createOrder(postRequest);
		assertThat("OrderBook createOrder(PostRequest)", limitOrder instanceof LimitOrder, is(true));
		
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.STOPLOSS);
		postRequest.setStopPrice(2.3f);
		MarketOrder stopLossOrder = orderBook.createOrder(postRequest);
		assertThat("OrderBook createOrder(PostRequest)", stopLossOrder instanceof StopLossOrder, is(true));
		
	}

	
	@Test
	public void testCreateOrderMarketOrder() {
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
//		Timestamp timeStamp = new Timestamp(date.getTimeInMillis());
		Instant timeStamp = Instant.now().plus(1, ChronoUnit.DAYS);
		
		PostRequest postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		MarketOrder marketOrder = orderBook.createOrder(postRequest);
		MarketOrder anotherMarketOrder = orderBook.createOrder(marketOrder);
		assertThat("OrderBook createOrder(MarketOrder)", anotherMarketOrder, isA(MarketOrder.class));
		
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(timeStamp);
		MarketOrder limitOrder = orderBook.createOrder(postRequest);
		MarketOrder anotherLimitOrder = orderBook.createOrder(limitOrder);
		assertThat("OrderBook createOrder(MarketOrder)", anotherLimitOrder instanceof LimitOrder, is(true));
		
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.STOPLOSS);
		postRequest.setStopPrice(2.3f);
		MarketOrder stopLossOrder = orderBook.createOrder(postRequest);
		MarketOrder anotherStopLossOrder = orderBook.createOrder(stopLossOrder);
		assertThat("OrderBook createOrder(PostRequest)", anotherStopLossOrder instanceof StopLossOrder, is(true));
		
	}

	
	@Test
	public void testMatchOrder_MarketOrderEmptyMarket() {
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
//		Timestamp timeStamp = new Timestamp(date.getTimeInMillis());
		
		// Market orders vs empty markets
		PostRequest postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.MARKET);
		postRequest.setPartialFill(true);
		postRequest.setVolume(10);
		MarketOrder marketOrder = new MarketOrder(postRequest);
		assertThat("OrderBook matchOrder", orderBook.getSellLimitOrders(), equalTo(Collections.EMPTY_MAP));
		assertThat("OrderBook matchOrder", orderBook.matchOrder(marketOrder), nullValue());
		
		postRequest.setType(PostOrderType.SELL);
		marketOrder = new MarketOrder(postRequest);
		assertThat("OrderBook matchOrder", orderBook.getBuyLimitOrders(), equalTo(Collections.EMPTY_MAP));
		assertThat("OrderBook matchOrder", orderBook.matchOrder(marketOrder), nullValue());
	}
	
	
	@Test
	public void testMatchOrder_MarketOrderLimitOrder() {	
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
//		Timestamp timeStamp = new Timestamp(date.getTimeInMillis());
		Instant timeStamp = Instant.now().plus(1, ChronoUnit.DAYS);
		
		// Market orders buy vs limit orders sell
		PostRequest postRequest = new PostRequest();
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPartialFill(true);
		postRequest.setVolume(10);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(timeStamp);
		LimitOrder limitOrder = new LimitOrder(postRequest);
		limitOrder.attachTo(orderBook);
		assertThat("OrderBook matchOrder", orderBook.getSellLimitOrders().keySet(), contains(2.5f));
		assertThat("OrderBook matchOrder", orderBook.getSellLimitOrders().get(2.5f), contains(limitOrder));
		assertThat("OrderBook matchOrder", orderBook.getSellLimitOrders().get(2.5f), hasSize(1));
		
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		MarketOrder marketOrder = new MarketOrder(postRequest);
		assertThat("OrderBook matchOrder", orderBook.matchOrder(marketOrder), is(instanceOf(LimitOrder.class)));
		
		// Market orders sell vs limit orders buy
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPartialFill(true);
		postRequest.setVolume(10);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(timeStamp);
		limitOrder = new LimitOrder(postRequest);
		limitOrder.attachTo(orderBook);
		assertThat("OrderBook matchOrder", orderBook.getBuyLimitOrders().keySet(), contains(2.5f));
		assertThat("OrderBook matchOrder", orderBook.getBuyLimitOrders().get(2.5f), contains(limitOrder));
		assertThat("OrderBook matchOrder", orderBook.getBuyLimitOrders().get(2.5f), hasSize(1));
		
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		marketOrder = new MarketOrder(postRequest);
		assertThat("OrderBook matchOrder", orderBook.matchOrder(marketOrder), is(instanceOf(LimitOrder.class)));
		
		// limit orders buy vs limit orders sell
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPartialFill(true);
		postRequest.setVolume(10);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(timeStamp);
		limitOrder = new LimitOrder(postRequest);
		limitOrder.attachTo(orderBook);
		assertThat("OrderBook matchOrder", orderBook.getSellLimitOrders().keySet(), contains(2.5f));
		assertThat("OrderBook matchOrder", orderBook.getSellLimitOrders().get(2.5f), hasItem(limitOrder));
		assertThat("OrderBook matchOrder", orderBook.getSellLimitOrders().get(2.5f), hasSize(2));
		
		// don't match
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPartialFill(true);
		postRequest.setVolume(10);
		postRequest.setPrice(2.4999f);
		postRequest.setExpirationTime(timeStamp);
		LimitOrder anotherlimitOrder = new LimitOrder(postRequest);
		assertThat("OrderBook matchOrder", orderBook.matchOrder(anotherlimitOrder), nullValue());
		anotherlimitOrder.attachTo(orderBook);
		assertThat("OrderBook matchOrder", orderBook.getBuyLimitOrders().get(2.4999f), hasSize(1));
		
		// they match
		postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.BUY);
		postRequest.setOrderCondition(PostOrderCondition.LIMIT);
		postRequest.setPartialFill(true);
		postRequest.setVolume(10);
		postRequest.setPrice(2.5001f);
		postRequest.setExpirationTime(timeStamp);
		anotherlimitOrder = new LimitOrder(postRequest);
		assertThat("OrderBook matchOrder", orderBook.matchOrder(anotherlimitOrder), is(instanceOf(LimitOrder.class)));
		
		// limit orders sell vs limit orders buy
//		postRequest = new PostRequest();
//		postRequest.setUserId("dfgjkaga9");
//		postRequest.setStockTag("AAPL");
//		postRequest.setType(PostOrderType.BUY);
//		postRequest.setCondition(PostOrderCondition.LIMIT);
//		postRequest.setPartialFill(true);
//		postRequest.setVolume(10);
//		postRequest.setPrice(2.6f);
//		postRequest.setExpirationTime(timeStamp);
//		limitOrder = new LimitOrder(postRequest);
//		limitOrder.attachTo(orderBook);
//		assertThat("OrderBook matchOrder", orderBook.getBuyLimitOrders().keySet(), contains(2.6f));
//		assertThat("OrderBook matchOrder", orderBook.getBuyLimitOrders().get(2.6f), contains(limitOrder));
//		assertThat("OrderBook matchOrder", orderBook.getBuyLimitOrders().get(2.6f), hasSize(1));
		
		// don't match
//		postRequest = new PostRequest();
//		postRequest.setUserId("dfgjkaga9");
//		postRequest.setStockTag("AAPL");
//		postRequest.setType(PostOrderType.SELL);
//		postRequest.setCondition(PostOrderCondition.LIMIT);
//		postRequest.setPartialFill(true);
//		postRequest.setVolume(10);
//		postRequest.setPrice(2.5999f);
//		postRequest.setExpirationTime(timeStamp);
//		anotherlimitOrder = new LimitOrder(postRequest);
//		assertThat("OrderBook matchOrder", orderBook.matchOrder(anotherlimitOrder), is(true));
//		anotherlimitOrder.attachTo(orderBook);
//		assertThat("OrderBook matchOrder", orderBook.getSellLimitOrders().get(2.5999f), hasSize(1));
//		
//		// they match
//		postRequest = new PostRequest();
//		postRequest.setUserId("dfgjkaga9");
//		postRequest.setStockTag("AAPL");
//		postRequest.setType(PostOrderType.SELL);
//		postRequest.setCondition(PostOrderCondition.LIMIT);
//		postRequest.setPartialFill(true);
//		postRequest.setVolume(10);
//		postRequest.setPrice(2.6001f);
//		postRequest.setExpirationTime(timeStamp);
//		anotherlimitOrder = new LimitOrder(postRequest);
//		assertThat("OrderBook matchOrder", orderBook.matchOrder(anotherlimitOrder), is(true));
//		assertThat("OrderBook matchOrder", orderBook.getBuyLimitOrders(), equalTo(Collections.EMPTY_MAP));
		
//		postRequest.setStopPrice(2.3f);
		
	}

}