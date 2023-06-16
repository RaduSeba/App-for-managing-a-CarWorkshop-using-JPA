package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCONTRACTS", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "MECHANIC_ID", "STARTDATE" }) })
public class Contract extends BaseEntity {

	
	private double annualbaseWage;
	@Basic(optional=false)private LocalDate endDate;
	private double settlement;
	
	
	@Basic(optional=false)private LocalDate startDate;
	
	@OneToOne private Mechanic mechanic;
	@ManyToOne private Mechanic firedmechanic;
	
	@ManyToOne private ContractType contracttype;
	
	@ManyToOne private ProfessionalGroup professionalgroup;
	
	@Enumerated(EnumType.STRING)private ContractState state = ContractState.IN_FORCE;
	
	public enum ContractState {
		IN_FORCE,
		TERMINATED
	}
	
	
	@OneToMany(mappedBy="Contract")private Set<Payroll> payrools= new HashSet<>();
	
	Contract(){};
	

	
	public Contract(Mechanic mechanic, ContractType type, ProfessionalGroup group, LocalDate endDate, double wage) {
	    ArgumentChecks.isNotNull(mechanic);
	    ArgumentChecks.isTrue(wage >= 0);
	    ArgumentChecks.isNotNull(type);
	    ArgumentChecks.isNotNull(group);

	    this.mechanic = mechanic;
	    this.startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
	    this.endDate = endDate;
	    this.annualbaseWage = wage;
	    this.contracttype = type;
	    this.professionalgroup = group;
	    Associations.Group.link(this, group);
	    Associations.Hire.link(mechanic, this);
	    Associations.Type.link(this, type);
	  
	}

	

	
	public Contract(Mechanic mechanic, ContractType type, ProfessionalGroup group, double wage) {
	    this(mechanic, type, group,LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()), wage);
	}

	




	public double getAnnualBaseWage() {
		return annualbaseWage;
	}


	public void setAnnualWage(double annualWage) {
		this.annualbaseWage = annualWage;
	}


	public Optional<LocalDate> getEndDate() {
		return Optional.ofNullable(endDate);
	}


	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}


	public double getSettlement() {
		
		double payrool=calculateAnnualWage();
		int years = LocalDate.now().getYear() - startDate.getYear();
		
		int months = LocalDate.now().getMonthValue() - startDate.getMonthValue();
		
		if(months < 0 && years==1)
		{
			return 0;
		}
		
		payrool=payrool/365;
		
		this.settlement = payrool * this.contracttype.getCompensationDays()*years;
		
		return this.settlement;
	}


	public void setSettlement() {
		
		double payrool=calculateAnnualWage();
		
		this.settlement = payrool/365 * this.contracttype.getCompensationDays()*ChronoUnit.YEARS.between( 
			   this.startDate  , 
			   this.endDate
			) ;
	}
	
	
	public double calculateAnnualWage()
	{

		
		
		double payroll = 0;
	    
	    
	    
	 

	 // Convert set to a list
	 List<Payroll> list = new ArrayList<>(this.getPayrolls());
	 Collections.sort(list, payrollComp);

	 // Get the sublist containing the last 12 elements
	 int startIndex = Math.max(list.size() - 12, 0);  // Starting index
	 List<Payroll> last12Elements = list.subList(startIndex, list.size());

	 // Iterate through the last 12 elements
	 for (Payroll p : last12Elements) {
	    
		  payroll += p.getMonthlyWage() + p.getBonus()
		  + p.getProductivityBonus() + p.getTrienniumPayment();
	 }

	    

	    if (payroll==0)
	    {
	    	return payroll;
	    }
	    
	    return payroll;
		

		
	}


	public LocalDate getStartDate() {
		return startDate;
	}


	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}


	public Optional<Mechanic> getMechanic() {
		if(this.mechanic==null)
		{
			return Optional.empty();
		}
		else
		{
			return  Optional.of(this.mechanic);
		}
	}


	public void setMechanic(Optional<Mechanic> m) {
		if(m.isEmpty())
		{
			this.mechanic=null;
		}
		else
		{
			this.mechanic=m.get();
		}
	}


	public ContractType getContractType() {
		return contracttype;
	}


	public void setContractType(ContractType contracttype) {
		this.contracttype = contracttype;
	}


	public ProfessionalGroup getProfessionalGroup() {
		return professionalgroup;
	}


	public void setProffesionalGroup(ProfessionalGroup proffesionalgroup) {
		this.professionalgroup = proffesionalgroup;
	}


	 Set<Payroll> _getPayrools() {
		return payrools;
	}
	
	public Set<Payroll> getPayrolls() {
		return new HashSet<>( payrools );
	}


	public void setPayrools(Set<Payroll> payrools) {
		this.payrools = payrools;
	}
	
	public ContractState getState()
	{
		return this.state;
	}
	
	public void terminate()
	{
		Associations.Fire.link(this,mechanic);
		this.state=ContractState.TERMINATED;
		
		this.endDate= LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
		this.settlement =Math.floor(getSettlement() * 100) / 100;
		//this.mechanic=null;
		
	}
	
	

	
	public Optional<Mechanic> getFiredMechanic()
	{
		if(this.firedmechanic==null)
		{
			return Optional.empty();
		}
		else
		{
			return  Optional.of(this.firedmechanic);
		}
		
		
	}
	
	
	public void  setFiredMechanic(Mechanic m)
	{
		this.firedmechanic=m;
	}
	public void  _setFiredMechanic(Optional<Mechanic> m)
	{
		if(m.isEmpty())
		{
			this.firedmechanic=null;
		}
		else
		{
			this.firedmechanic=m.get();
		}
		
	}

	@Override
	public int hashCode() {
		//return Objects.hash(firedmechanic, mechanic, startDate);
		return Objects.hash( mechanic, startDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contract other = (Contract) obj;
		return Objects.equals(firedmechanic, other.firedmechanic)
				&& Objects.equals(mechanic, other.mechanic)
				&& Objects.equals(startDate, other.startDate);
	}
	
	private static Comparator<Payroll> payrollComp = new Comparator<Payroll>() {
		@Override
		public int compare(Payroll p1, Payroll p2) {
			return p1.getDate().compareTo(p2.getDate());
		}

	};
	
	
	
	
	

}
