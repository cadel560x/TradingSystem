package ie.gmit.sw.fyp.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ie.gmit.sw.fyp.services.StockService;




@RunWith(SpringRunner.class)
@SpringBootTest
public class StockServiceTest {

	@Test
	public void testCheckStockTag_ValidStockTag() {
		String stockId = "AAPL";
		
		assertThat(StockService.checkStockTag(stockId), is(true));
		
	}
	
	
	@Test
	public void testCheckStockTag_InvalidStockTag() {
		String stockId = "MSFT";
		
		assertThat(StockService.checkStockTag(stockId), is(false));
		
	}
	

	@Test
	public void testGetInstance() {
		StockService stockService1 = StockService.getInstance();
		StockService stockService2 = StockService.getInstance();
		
		assertThat(stockService1.equals(stockService2), is(true));
		
	}

}
