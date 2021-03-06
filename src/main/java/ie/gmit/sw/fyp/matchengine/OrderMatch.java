package ie.gmit.sw.fyp.matchengine;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Scope;




@Scope("prototype")
@Entity
public class OrderMatch {
//	Fields
	@Id
	private String Id;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="sell_order")
	private MarketOrder sellOrder;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="buy_order")
	private MarketOrder buyOrder;
	
	@NotNull
	@CreationTimestamp
	@PastOrPresent
	private Instant timestamp;
	private int filledShares;
	
	
	
//	Constructors
	public OrderMatch() {
		this.timestamp = Instant.now();
		this.Id = UUID.randomUUID().toString();
	}
	
	public OrderMatch(MarketOrder sellOrder, MarketOrder buyOrder) {
		this.timestamp = Instant.now();
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
	
	public Instant getTimestamp() {
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
