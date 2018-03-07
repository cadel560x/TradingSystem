package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;

public class Match {
	private Order buyOrder;
	private Order sellOrder;
	private Timestamp timestamp;
	
	
	
	
	public Match() {
		
	}
	
	public Match(Order buyOrder, Order sellOrder) {
		timestamp = new Timestamp(System.currentTimeMillis());
		this.buyOrder = buyOrder;
		this.sellOrder = sellOrder;
	}
	
	
	
	
	public Order getBuyOrder() {
		return buyOrder;
	}
	
	public void setBuyOrder(Order buyOrder) {
		this.buyOrder = buyOrder;
	}
	
	public Order getSellOrder() {
		return sellOrder;
	}
	
	public void setSellOrder(Order sellOrder) {
		this.sellOrder = sellOrder;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
}
