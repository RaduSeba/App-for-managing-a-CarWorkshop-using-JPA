package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;


@Entity
@Table(name="TWORKORDERS", uniqueConstraints = {
		
		@UniqueConstraint(columnNames= {
				"VEHICLE_ID","DATE"
		})	
})
public class WorkOrder extends BaseEntity {
	public LocalDateTime getDate() {
		return date;
	}


	public void setDate(LocalDateTime date) {
		this.date = date;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public WorkOrderState getState() {
		return state;
	}


	public void setState(WorkOrderState state) {
		this.state = state;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}

	public enum WorkOrderState {
		OPEN,
		ASSIGNED,
		FINISHED,
		INVOICED
	}

	// natural attributes
	@Basic(optional=false)private LocalDateTime date;
	@Basic(optional=false)private String description;
	private double amount = 0.0;
	@Column(name="Status")
	@Enumerated(EnumType.STRING)private WorkOrderState state = WorkOrderState.OPEN;

	// accidental attributes
	@ManyToOne private Vehicle vehicle;
	@ManyToOne private Mechanic mechanic;
	@ManyToOne private Invoice invoice;
	
	@OneToMany(mappedBy="workOrder") private Set<Intervention> interventions = new HashSet<>();
	
	
	

	
	
	
	
	
	
	public WorkOrder(Vehicle v,String description ){
		

		ArgumentChecks.isNotNull(v);
		
		ArgumentChecks.isNotEmpty(description);
		
		this.vehicle=v;
		this.description=description;
		Associations.Fix.link(v,this);
	}
	

	public WorkOrder(Vehicle v,LocalDateTime date,String description ){
		

		ArgumentChecks.isNotNull(v);
		
		ArgumentChecks.isNotEmpty(description);
		
		this.vehicle=v;
		this.description=description;
		this.date=date;
	}
	
	
	public WorkOrder() {
		super();
	}
	
	
	
	

	@Override
	public String toString() {
		return "WorkOrder [date=" + date + ", description=" + description
				+ ", amount=" + amount + ", state=" + state + ", vehicle="
				+ vehicle + "]";
	}

	
	
	
	

	


	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(vehicle);
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
		WorkOrder other = (WorkOrder) obj;
		return Objects.equals(vehicle, other.vehicle);
	}

	
	
	
	

	public WorkOrder(Vehicle vehicle, LocalDateTime date) {
		super();
		this.vehicle = vehicle;
		this.date = date;
	}


	public WorkOrder(Vehicle vehicle) {
		
		this(LocalDateTime.now(), "no-description", vehicle);
		this.vehicle = vehicle;
	}

	public WorkOrder(LocalDateTime date, String description, Vehicle vehicle) {
		ArgumentChecks.isNotNull(vehicle);
		ArgumentChecks.isNotNull(date);
		ArgumentChecks.isNotBlank(description);
		
		
		this.date = date;
		this.description = description;
		this.vehicle = vehicle;
		
		Associations.Fix.link(vehicle, this);
	}

	/**
	 * Changes it to INVOICED state given the right conditions
	 * This method is called from Invoice.addWorkOrder(...)
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if
	 * 	- The work order is not FINISHED, or
	 *  - The work order is not linked with the invoice
	 */
	public void markAsInvoiced() {
		
		if(this.state != WorkOrderState.FINISHED)
		{
			throw new IllegalStateException("the workorder is not finished");
		}
		
		if(this.invoice==null)
		{
			throw new IllegalStateException("the workorder is not linked");
		}
		
		
		this.state=WorkOrderState.INVOICED;
	}

	/**
	 * Changes it to FINISHED state given the right conditions and
	 * computes the amount
	 *
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if
	 * 	- The work order is not in ASSIGNED state, or
	 *  - The work order is not linked with a mechanic
	 */
	public void markAsFinished() {
		
		this.state=WorkOrderState.FINISHED;
		
		Iterator<Intervention> i=this._getInterventions().iterator();
		this.amount=0;
		int k=this._getInterventions().size();
		
		while(k>0)
		{
			this.amount=this.amount+i.next().getAmount();
			k--;
		}
		
		
		

		
		
		
		   
		
		
	}

	/**
	 * Changes it back to FINISHED state given the right conditions
	 * This method is called from Invoice.removeWorkOrder(...)
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if
	 * 	- The work order is not INVOICED, or
	 *  - The work order is still linked with the invoice
	 */
	public void markBackToFinished() {
		
		if(this.state != WorkOrderState.INVOICED)
		{
			throw new IllegalStateException("the workorder is not finished");
		}
		this.state=WorkOrderState.FINISHED;
	}

	/**
	 * Links (assigns) the work order to a mechanic and then changes its state
	 * to ASSIGNED
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if
	 * 	- The work order is not in OPEN state, or
	 *  - The work order is already linked with another mechanic
	 */
	public void assignTo(Mechanic mechanic) {
		
		if(this.state != WorkOrderState.OPEN)
		{
			throw new IllegalStateException("the workorder is not finished");
		}
		Associations.Assign.link(mechanic, this);
		this.state=WorkOrderState.ASSIGNED;

	}

	/**
	 * Unlinks (deassigns) the work order and the mechanic and then changes
	 * its state back to OPEN
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if
	 * 	- The work order is not in ASSIGNED state
	 */
	public void desassign() {
		if(this.state != WorkOrderState.ASSIGNED)
		{
			throw new IllegalStateException("the workorder is not finished");
		}
		this.state=WorkOrderState.OPEN;
		Associations.Assign.unlink(mechanic, this);

	}

	/**
	 * In order to assign a work order to another mechanic is first have to
	 * be moved back to OPEN state and unlinked from the previous mechanic.
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if
	 * 	- The work order is not in FINISHED state
	 */
	public void reopen() {
		
		if(this.state != WorkOrderState.FINISHED)
		{
			throw new IllegalStateException("the workorder is not finished");
		}
		this.state=WorkOrderState.OPEN;
		this.mechanic=null;

	}

	public Set<Intervention> getInterventions() {
		return new HashSet<>( interventions );
	}

	public Set<Intervention> _getInterventions() {
		return interventions;
	}

	void _setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	void _setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	void _setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	public Mechanic getMechanic()
	{
		return this.mechanic;
	}
	
	public double getAmount()
	{
		return this.amount;
	}
	
	public Vehicle getVehicle()
	{
		return this.vehicle;
	}
	
	public boolean isInvoiced()
	{
		if(this.state==WorkOrderState.INVOICED)
		{
			return true;
		}
		return false;
	}
	
	public boolean isFinished()
	{
		if(this.state==WorkOrderState.FINISHED)
		{
			return true;
		}
		return false;
	}
	
	public Invoice getInvoice()
	{
		return this.invoice;
	}

}
