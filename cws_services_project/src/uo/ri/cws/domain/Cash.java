package uo.ri.cws.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TCASHES")
public class Cash extends PaymentMean {
	
	
	
	Cash(){}
	
	public Cash(Client client)
	{
		Associations.Pay.link(this,client);
	}
	
	
	public Client getClient()
	{
		return this._getClient();
	}
	
	 
	
	
	
	

}
