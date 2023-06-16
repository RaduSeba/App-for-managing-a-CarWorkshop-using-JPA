package uo.ri.cws.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TVOUCHERS")
public class Voucher extends PaymentMean {
	@Column(unique = true)private String code;
	private double available = 0.0;
	@Basic(optional=false)private String description;

	/**
	 * Augments the accumulated (super.pay(amount) ) and decrements the available
	 * @throws IllegalStateException if not enough available to pay
	 */
	
	public Voucher(String code,double available)
	{
		this(code,"no-description" ,available);
	}
	
	
	
	
	 Voucher() {}




	@Override
	public void pay(double amount) {
		if(amount>this.available)
		{
			throw new IllegalStateException("amount is too high ");
		}
		
		this._setAccumulated(amount+this.getAccumulated());
	}
	
	
	
	public Voucher(String code,String description, double pay)
	{
		this.code=code;
		this.description=description;
		this.available=pay;
	}
	
	
	public String getDescription()
	{
		return this.description;
	}
	
	public String getCode()
	{
		return this.code;
	}
	
	public double getAvailable()
	{
		return this.available-this.getAccumulated();
	}
	
	
	
	public Client getClient()
	{
		return this.getClient();
	}
	
	

}
