package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.math.Round;


@Entity
//@Table(name="TINVOICES", uniqueConstraints = {
//		
//		@UniqueConstraint(columnNames= {
//				"number"
//		})	
//})
@Table(name="TINVOICES")
public class Invoice extends BaseEntity {
	

	// natural attributes
	@Column(unique = true)private Long number;
	@Basic(optional=false)private LocalDate date;
	private double amount;
	private double vat;
	@Enumerated(EnumType.STRING)private InvoiceState status = InvoiceState.NOT_YET_PAID;

	// accidental attributes
	@OneToMany(mappedBy ="Invoice") 
		private Set<WorkOrder> workOrders = new HashSet<>();
	
	
	@OneToMany(mappedBy ="Invoice")  private Set<Charge> charges = new HashSet<>();
	
	Invoice(){}
	
	
	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public InvoiceState getState() {
		return status;
	}
	
	public InvoiceState getStatus() {
		return status;
	}

	

	
	public void setState(InvoiceState state) {
		this.status = state;
	}

	public void setCharges(Set<Charge> charges) {
		this.charges = charges;
	}

	public enum InvoiceState { NOT_YET_PAID, PAID }

	public Invoice(Long number) {
		
		this(number,LocalDate.now(),List.of());
	}

	public Invoice(Long number, LocalDate date) {
		this(number,date,List.of());
	}

	public Invoice(Long number, List<WorkOrder> workOrders) {
		this(number, LocalDate.now(), workOrders);
	}

	// full constructor
	public Invoice(Long number, LocalDate date, List<WorkOrder> workOrders) {
		
		ArgumentChecks.isNotNull(number);
		ArgumentChecks.isNotNull(workOrders);
		ArgumentChecks.isNotNull(date);
		ArgumentChecks.isTrue(number>=0);
		this.number=number;
		this.date=date;
		
		for(WorkOrder a: workOrders)
		{
			addWorkOrder(a);
		}
		
	}

	/**
	 * Computes amount and vat (vat depends on the date)
	 */
	private void computeAmount() {
		
		this.amount=0;
		   for (WorkOrder w:this._getWorkOrders()) {
			   this.amount=this.amount+w.getAmount();
			 
		   }
		   this.vat= LocalDate.parse("2012-07-01").isBefore(this.date) ? 21.0 : 18.0;
			this.amount=this.amount * (1 + this.vat/100);
			this.amount = Round.twoCents(this.amount);
		
		
		
	}

	/**
	 * Adds (double links) the workOrder to the invoice and updates the amount and vat
	 * @param workOrder
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if the invoice status is not NOT_YET_PAID
	 */
	public void addWorkOrder(WorkOrder workOrder) {
		
		Associations.ToInvoice.link(this, workOrder);
		
		workOrder.markAsInvoiced();
		
		computeAmount();
		
	}

	/**
	 * Removes a work order from the invoice and recomputes amount and vat
	 * @param workOrder
	 * @see UML_State diagrams on the problem statement document
	 * @throws IllegalStateException if the invoice status is not NOT_YET_PAID
	 */
	public void removeWorkOrder(WorkOrder workOrder) {
		
		
		this.amount=0;
		Associations.ToInvoice.unlink(this, workOrder);
		workOrder.markBackToFinished();
		
	}

	/**
	 * Marks the invoice as PAID, but
	 * @throws IllegalStateException if
	 * 	- Is already settled
	 *  - Or the amounts paid with charges to payment means do not cover
	 *  	the total of the invoice
	 */
	public void settle() throws IllegalStateException {
		
		ArgumentChecks.isFalse(this.status==InvoiceState.PAID);
		double a =0;
		
		for(Charge c: getCharges())
		{
			a=a+c.getAmount();
		}
		
		double realamount1=getAmount()+0.013;
		double realamount2=getAmount()-0.01;
		
		
		if(a > realamount1)
		{
			
			throw new IllegalStateException();
		}
		
		
		if(a < realamount2)
		{
			
			throw new IllegalStateException();
		}
		
		
		this.status=InvoiceState.PAID;

	}

	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>( workOrders );
	}

	 Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	public Set<Charge> getCharges() {
		return new HashSet<>( charges );
	}

	 Set<Charge> _getCharges() {
		return charges;
	}
	
	public double getVat()
	{
		return this.vat;
	}
	
	public boolean isNotSettled()
	{
		if(this.status==InvoiceState.NOT_YET_PAID)
		{
			return true;
		}
		return false;
	}
	
	public boolean isSettled()
	{
		if(this.status==InvoiceState.NOT_YET_PAID)
		{
			return false;
		}
		return true;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(number);
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
		Invoice other = (Invoice) obj;
		return Objects.equals(number, other.number);
	}


	
	
	
	

}
