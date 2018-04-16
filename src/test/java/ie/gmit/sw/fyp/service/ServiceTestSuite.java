package ie.gmit.sw.fyp.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ie.gmit.sw.fyp.model.LimitOrderTest;
import ie.gmit.sw.fyp.model.MarketOrderTest;
import ie.gmit.sw.fyp.model.MatchTest;
import ie.gmit.sw.fyp.model.PostRequestTest;
import ie.gmit.sw.fyp.model.StopLossOrderTest;




@RunWith(Suite.class)
@SuiteClasses({UserServiceTest.class, StockServiceTest.class})
public class ServiceTestSuite {

}
