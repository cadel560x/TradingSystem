package ie.gmit.sw.fyp.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ie.gmit.sw.fyp.model.PostRequestTest;




@RunWith(Suite.class)
@SuiteClasses({UserServiceTest.class, StockServiceTest.class, PostRequestTest.class})
public class ServiceTestSuite {

}
