package ie.gmit.sw.fyp.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ie.gmit.sw.fyp.order.UserService;




@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Test
	public void testCheckUserId_ValidUserId() {
		String userId = "dfgjkaga9";
		
//		assertEquals("UserId '" + userId + "' must exist", true, UserService.checkUserId(userId ));
		assertThat(UserService.checkUserId(userId ), is(true));
	}
	
	
	@Test
	public void testCheckUserId_InvalidUserId() {
		String userId = "asdfadsf";
		
//		assertEquals("UserId '" + userId + "' must not exist", false, UserService.checkUserId(userId ));
		assertThat(UserService.checkUserId(userId ), is(false));
	}
	

	@Test
	public void testGetInstance() {
		UserService userService1 = UserService.getInstance();
		UserService userService2 = UserService.getInstance();
		
		assertEquals("'userService1' is equals to 'userService2'", true, userService1.equals(userService2));
	}

} // end class UserServiceTest