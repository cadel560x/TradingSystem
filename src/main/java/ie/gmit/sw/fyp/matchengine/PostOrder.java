package ie.gmit.sw.fyp.matchengine;

import ie.gmit.sw.fyp.model.OrderBook;




public interface PostOrder {
	
	public boolean matches(LimitOrder other);
	
	public void attachTo(OrderBook orderBook);
	
} // end interface PersistentOrder
