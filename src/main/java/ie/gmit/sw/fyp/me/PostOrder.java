package ie.gmit.sw.fyp.me;

import ie.gmit.sw.fyp.order.OrderBook;




public interface PostOrder {
	
//	public boolean matches(MarketOrder other);
	
	public void attachTo(OrderBook orderBook);
	
} // end interface PersistentOrder
