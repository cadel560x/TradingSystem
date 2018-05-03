package ie.gmit.sw.fyp.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ie.gmit.sw.fyp.services.UserService;




@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	@Autowired
	UserService userService;
	
	
	
	
	@Before
	public void setUpBeforeClass() {
	}
	
	
	@Test
	public void testCheckUserId_ValidUserId() {
		String userId = "dfgjkaga9";
		assertThat("checkUserId_ValidUserId", userService.checkUserId(userId ), is(true));
	}
	
	
	@Test
	public void testCheckUserId_InvalidUserId() {
		String userId = "asdfadsf";
		assertThat("checkUserId_InvalidUserId", userService.checkUserId(userId ), is(false));
	}

} // end class UserServiceTest
