package ie.gmit.sw.fyp.me;

import java.sql.Timestamp;
import java.util.UUID;




public class Match {
//	Fields
	private String Id;
	private MarketOrder sellOrder;
	private MarketOrder buyOrder;
	private Timestamp timestamp;
	private int filledShares;
	
	
	
//	Constructors
	public Match() {
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.Id = UUID.randomUUID().toString();
	}
	
	public Match(MarketOrder postOrder1, MarketOrder postOrder2) {
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.Id = UUID.randomUUID().toString();
		
//		Logic that keeps order regarding the passed PostOrders
		if ( postOrder1.isSell() ) {
			this.sellOrder = postOrder1;
			this.buyOrder = postOrder2;
		}
		else {
			this.sellOrder = postOrder2;
			this.buyOrder = postOrder1;
		}
		
		setFilledShares();
	}
	
	
	

//	Accessors and mutators
	public String getId() {
		return Id;
	}
	
	public MarketOrder getSellOrder() {
		return sellOrder;
	}
	
	public void setSellOrder(MarketOrder sellOrder) {
		this.sellOrder = sellOrder;
	}
	
	public void setBuyOrder(MarketOrder buyOrder) {
		this.buyOrder = buyOrder;
	}
	
	public MarketOrder getBuyOrder() {
		return buyOrder;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public int getFilledShares() {
		return filledShares;
	}
	
	
	
	
//	Methods
	public void setFilledShares() {
		filledShares = Math.abs( sellOrder.getVolume() - buyOrder.getVolume() );
		
		if ( filledShares == 0 ) {
			filledShares = sellOrder.getVolume();
		}
		
	} // setMatchedShares()
	
	
	public void setVolumes() {
		if ( sellOrder.getVolume() > buyOrder.getVolume() ) {
			sellOrder.setVolume(filledShares);
		}
		else if ( sellOrder.getVolume() < buyOrder.getVolume() ) {
			buyOrder.setVolume(filledShares);
		}

	} // initMatch()
	
} // end class Match
