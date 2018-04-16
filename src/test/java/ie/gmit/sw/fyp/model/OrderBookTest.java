package ie.gmit.sw.fyp.model;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import ie.gmit.sw.fyp.me.LimitOrder;
import ie.gmit.sw.fyp.me.PostOrderCondition;
import ie.gmit.sw.fyp.me.PostOrderType;
import ie.gmit.sw.fyp.me.PostRequest;
import ie.gmit.sw.fyp.me.StopLossOrder;
import ie.gmit.sw.fyp.order.OrderBook;
import ie.gmit.sw.fyp.order.StockService;
import ie.gmit.sw.fyp.order.UserService;




public class OrderBookTest {
	private OrderBook orderBook;
	@SuppressWarnings("unused")
	private StockService stockService = new StockService();
	@SuppressWarnings("unused")
	private UserService userService = new UserService();
	
	@Rule
    public ErrorCollector errorCollector = new ErrorCollector();

	
	
	
	@Before
	public void setUp() throws Exception {
		orderBook = new OrderBook("AAPL");
		
	}

	
	@Test
	public void testOrderBook() {
		OrderBook newOrderBook = new OrderBook("GOOGL");
		
		assertThat("OrderBook constructor(String)", newOrderBook, hasProperty("stockTag", equalTo("GOOGL")));
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
	public void testSetBuyLimitOrders() {
		Map<Float, Queue<LimitOrder>> newBuyLimitOrders = new ConcurrentSkipListMap<>();
		orderBook.setBuyLimitOrders(newBuyLimitOrders);
		
		assertThat("OrderBook setBuyLimitOrders", orderBook.getBuyLimitOrders(), is(newBuyLimitOrders));
		
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
	public void testSetSellLimitOrders() {
		Map<Float, Queue<LimitOrder>> newSellLimitOrders = new ConcurrentSkipListMap<>();
		orderBook.setSellLimitOrders(newSellLimitOrders);
		
		assertThat("OrderBook setSellLimitOrders", orderBook.getSellLimitOrders(), is(newSellLimitOrders));
		
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
	public void testSetBuyStopLoss() {
		Map<Float, Queue<StopLossOrder>> newBuyStopLossOrders = new ConcurrentSkipListMap<>();
		orderBook.setBuyStopLoss(newBuyStopLossOrders);
		
		assertThat("OrderBook setBuyStopLossOrders", orderBook.getBuyStopLoss(), is(newBuyStopLossOrders));
		
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
	public void testSetSellStopLoss() {
		Map<Float, Queue<StopLossOrder>> newSellStopLossOrders = new ConcurrentSkipListMap<>();
		orderBook.setBuyStopLoss(newSellStopLossOrders);
		
		assertThat("OrderBook setSellStopLossOrders", orderBook.getSellStopLoss(), is(newSellStopLossOrders));
		
	}

	
	@Test
	public void testGetMatchedQueue() {
		assertThat("OrderBook getMatchedQueue", orderBook.getMatchedQueue(), is(empty()));
		assertThat("OrderBook getMatchedQueue", orderBook.getMatchedQueue(), is(instanceOf(BlockingQueue.class)));
		
		try {
			Field matchedQueueFields = OrderBook.class.getDeclaredField("matchedQueue");
			ParameterizedType matchedQueueFieldsType = (ParameterizedType) matchedQueueFields.getGenericType();
	        Class<?> matchClass = (Class<?>) matchedQueueFieldsType.getActualTypeArguments()[0];
			
			assertThat("OrderBook matchedQueue map key type", matchClass.getSimpleName(), is("Match"));
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
	}

	
	@Test
	public void testCheckRequest() {
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp timeStamp = new Timestamp(date.getTimeInMillis());
		
		PostRequest postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setCondition(PostOrderCondition.MARKET);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		assertThat("OrderBook checkRequest", orderBook.checkRequest(postRequest), is(true));
		
		postRequest.setType(PostOrderType.BUY);
		postRequest.setCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(timeStamp);
		assertThat("OrderBook checkRequest", orderBook.checkRequest(postRequest), is(true));
		
		postRequest.setType(PostOrderType.BUY);
		postRequest.setCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setExpirationTime(timeStamp);
		postRequest.setStopPrice(2.3f);
		assertThat("OrderBook checkRequest", orderBook.checkRequest(postRequest), is(true));
		
	}

	
	@Test
	public void testCreateOrderPostRequest() {
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp timeStamp = new Timestamp(date.getTimeInMillis());
		
		PostRequest postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		postRequest.setExpirationTime(timeStamp);
		postRequest.setStopPrice(2.3f);
		
		fail("Not yet implemented");
	}

	@Test
	public void testCreateOrderMarketOrder() {
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp timeStamp = new Timestamp(date.getTimeInMillis());
		
		PostRequest postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		postRequest.setExpirationTime(timeStamp);
		postRequest.setStopPrice(2.3f);
		
		fail("Not yet implemented");
	}

	@Test
	public void testMatchOrder() {
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp timeStamp = new Timestamp(date.getTimeInMillis());
		
		PostRequest postRequest = new PostRequest();
		postRequest.setUserId("dfgjkaga9");
		postRequest.setStockTag("AAPL");
		postRequest.setType(PostOrderType.SELL);
		postRequest.setCondition(PostOrderCondition.LIMIT);
		postRequest.setPrice(2.5f);
		postRequest.setVolume(10);
		postRequest.setPartialFill(true);
		postRequest.setExpirationTime(timeStamp);
		postRequest.setStopPrice(2.3f);
		
		fail("Not yet implemented");
		
	}

}