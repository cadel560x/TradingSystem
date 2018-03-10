package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;




public class Match {
//	Fields
	private PostOrder buyOrder;
	private PostOrder sellOrder;
	private Timestamp timestamp;
	
	
	
//	Constructors
	public Match() {
		
	}
	
	public Match(PostOrder postOrder1, PostOrder postOrder2) {
		timestamp = new Timestamp(System.currentTimeMillis());
		
//		Logic that keeps order regarding the passed PostOrders
		if ( postOrder1.isSell() ) {
			this.sellOrder = postOrder1;
			this.buyOrder = postOrder2;
		}
		else {
			this.sellOrder = postOrder2;
			this.buyOrder = postOrder1;
		}
	}
	
	
	
	
//	Accessors and mutators
	public PostOrder getBuyOrder() {
		return buyOrder;
	}
	
	public void setBuyOrder(PostOrder buyOrder) {
		this.buyOrder = buyOrder;
	}
	
	public PostOrder getSellOrder() {
		return sellOrder;
	}
	
	public void setSellOrder(PostOrder sellOrder) {
		this.sellOrder = sellOrder;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
} // end class Pair
