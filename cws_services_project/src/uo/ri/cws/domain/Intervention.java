package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name="TINTERVENTIONS",uniqueConstraints = {
		
		@UniqueConstraint(columnNames= {
				"WORKORDER_ID","MECHANIC_ID"
		})	
})
public class Intervention extends BaseEntity{

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	// natural attributes
	@Basic(optional=false) private LocalDateTime date;
	private int minutes;

	// accidental attributes
	@ManyToOne private WorkOrder workOrder;
	@ManyToOne private Mechanic mechanic;
	
	@OneToMany(mappedBy ="intervention") private Set<Substitution> substitutions = new HashSet<>();
	
	Intervention(){}
	
	public WorkOrder getWorkOrder() {
		return workOrder;
	}




	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}




	public Mechanic getMechanic() {
		return mechanic;
	}




	public void setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	
	

	public Intervention(Mechanic mechanic, WorkOrder workOrder, int minutes) {
		this(mechanic,workOrder,LocalDateTime.now(),minutes);
	}

	
	public double getAmount()
	{
		double amount=0;
		
		amount=this.workOrder.getVehicle().getVehicleType().getPricePerHour()*this.getMinutes()/60;
		
		if(this._getSubstitutions().isEmpty()&&this.minutes==0)
		{
			return 0;
		}
		if(this._getSubstitutions().isEmpty())
		{
			return amount;
		}
		
		amount=amount+this.workOrder._getInterventions().iterator().next()._getSubstitutions().iterator().next().getSparePart().getPrice()*this.workOrder._getInterventions().iterator().next()._getSubstitutions().iterator().next().getQuantity();
		

		return amount;
	}


	public Intervention(Mechanic mechanic, WorkOrder workOrder,
			LocalDateTime date, int minutes) {
		
		ArgumentChecks.isNotNull(mechanic);
		ArgumentChecks.isNotNull(workOrder);
		ArgumentChecks.isNotNull(date);
		ArgumentChecks.isTrue(minutes>= 0);
		
		
		this.mechanic = mechanic;
		this.workOrder = workOrder;
		this.date = date;
		this.minutes = minutes;
		
		
		Associations.Intervene.link(workOrder, this, mechanic);
	}
	
	
	

	@Override
	public String toString() {
		return "Intervention [date=" + date + ", minutes=" + minutes
				+ ", substitutions=" + substitutions + "]";
	}








	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(mechanic, workOrder);
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
		Intervention other = (Intervention) obj;
		return Objects.equals(mechanic, other.mechanic)
				&& Objects.equals(workOrder, other.workOrder);
	}

	void _setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	void _setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	public Set<Substitution> getSubstitutions() {
		return new HashSet<>( substitutions );
	}

	Set<Substitution> _getSubstitutions() {
		return substitutions;
	}

}
