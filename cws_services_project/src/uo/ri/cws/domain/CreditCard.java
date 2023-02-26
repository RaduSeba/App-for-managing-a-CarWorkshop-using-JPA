package uo.ri.cws.domain;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TCREDITCARDS")
public class CreditCard extends PaymentMean {
	
	@Column(unique = true)private String number;
	@Basic(optional=false)	private String type;
	@Basic(optional=false) private LocalDate validThru;
	
	CreditCard(){}
	
	public CreditCard(String number, String type, LocalDate validThru) {
		
		
		
		
		this.number = number;
		this.type = type;
		this.validThru = validThru;
		
		
	}
	
	public CreditCard(String code)
	{
		this(code,"UNKNOWN",LocalDate.now().plusDays(1));
	}
	
	public LocalDate getValidThru()
	{
		return this.validThru;
	}
	public void setValidThru(LocalDate validThru)
	{
		if(validThru.isBefore(this.validThru))
		{
			throw new IllegalStateException("date is not valid ");
		}
		
		this.validThru=validThru;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public String getNumber()
	{
		return this.number;
	}

	
	@Override
	public void pay(double importe) {
		
		if(validThru.isBefore(LocalDate.now()))
		{
			throw new IllegalStateException("date is not valid ");
		}
		
		this._setAccumulated(importe+this.getAccumulated());
	}
	
	public boolean isValidNow()
	{
		if(this.validThru.isBefore(LocalDate.now()))
		{
			return false;
		}
		return true;
	}
	
	public Client getClient()
	{
		return this.getClient();
	}
	
	
	
	
}
