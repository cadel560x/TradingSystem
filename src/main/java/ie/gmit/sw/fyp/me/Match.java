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
	
	public Match(MarketOrder sellOrder, MarketOrder buyOrder) {
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.Id = UUID.randomUUID().toString();
		
//		Logic that keeps order regarding the passed PostOrders
		if ( !sellOrder.isSell() || !buyOrder.isBuy() ) {
			throw new IllegalArgumentException("Incorrect order type");
		}
		
		this.sellOrder = sellOrder;
		this.buyOrder = buyOrder;
		
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
		filledShares = sellOrder.getVolume();
		
		if ( sellOrder.getVolume() > buyOrder.getVolume() ) {
			filledShares = buyOrder.getVolume();
		}
		
	} // setFilledShares()
	
	
	public int getRemainingShares() {
		return Math.abs( sellOrder.getVolume() - buyOrder.getVolume() );
		
	} // getRemainingShares()
	
	
	public void setVolumes() {
		if ( sellOrder.getVolume() > buyOrder.getVolume() ) {
			sellOrder.setVolume(this.getRemainingShares());
		}
		else if ( sellOrder.getVolume() < buyOrder.getVolume() ) {
			buyOrder.setVolume(this.getRemainingShares());
		}

	} // setVolumes()
	
} // end class Match
