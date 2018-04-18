package ie.gmit.sw.fyp.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import ie.gmit.sw.fyp.notification.Notification;




public class NotificationTest {
	private Notification notification;

	
	
	
	@Before
	public void setUp() throws Exception {
		notification = new Notification();
		
	}
	

	@Test
	public void testNotification() {
		assertThat(notification.getMessage(),  isEmptyOrNullString());
		
	}
	

	@Test
	public void testNotificationString() {
		Notification notification = new Notification("Test message");
		
		assertThat("New empty notification", notification.getMessage(), containsString("Test message"));

	}
	

	@Test
	public void testGetMessage() {
		assertThat("Getting notification message", notification.getMessage(),  isEmptyOrNullString());
		
	}
	

	@Test
	public void testSetMessage() {
		notification.setMessage("Test message");
		
		assertThat("Setting notification message", notification.getMessage(), containsString("Test message"));
		
	}
	

	@Test
	public void testUpdateMessage() {
		notification.setMessage("Test message");
		assertThat("Setting notification message", notification.getMessage(), containsString("Test message"));
		
		notification.updateMessage("Test message updated");
		assertThat("Setting notification message", notification.getMessage(), containsString("Test message updated"));
		
	}

}
