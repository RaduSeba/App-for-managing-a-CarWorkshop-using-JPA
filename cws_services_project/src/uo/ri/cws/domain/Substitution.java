package uo.ri.cws.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name="TSUBSTITUTIONS",uniqueConstraints = {
		
		@UniqueConstraint(columnNames= {
				"SPAREPART_ID","INTERVENTION_ID"
		})	
})
public class Substitution extends BaseEntity {
	// natural attributes
	private int quantity;

	// accidental attributes
	@ManyToOne private SparePart sparePart;
	@ManyToOne private Intervention intervention;
	
	
	



	public Substitution( SparePart sparePart,
			Intervention intervention,int quantity) {
		
		ArgumentChecks.isNotNull(intervention);
		ArgumentChecks.isNotNull(sparePart);
		ArgumentChecks.isTrue(quantity > 0);
		
		
		this.quantity = quantity;
		this.sparePart = sparePart;
		this.intervention = intervention;
		
		Associations.Substitute.link(sparePart, this, intervention);
		
	}
	
	
	
	
	
	
	
	

	public Substitution() {
		super();
	}









	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}









	public void setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}









	public void setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}









	public int getQuantity() {
		return quantity;
	}









	public SparePart getSparePart() {
		return sparePart;
	}









	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(quantity);
		return result;
	}









	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Substitution other = (Substitution) obj;
		return quantity == other.quantity;
	}









	public Intervention getIntervention() {
		return intervention;
	}




	public double getAmount()
	{
		return this.quantity*this.sparePart.getPrice();
	}




	void _setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}

	void _setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}

}
