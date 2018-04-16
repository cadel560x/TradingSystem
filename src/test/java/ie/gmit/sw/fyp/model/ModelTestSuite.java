package ie.gmit.sw.fyp.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;




@RunWith(Suite.class)
@SuiteClasses({ LimitOrderTest.class, MarketOrderTest.class, MatchTest.class, NotificationTest.class,
		OrderBookTest.class, PostRequestTest.class, StopLossOrderTest.class })
public class ModelTestSuite {

}
