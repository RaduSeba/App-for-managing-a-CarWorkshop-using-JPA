package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
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
@Table(name="TCONTRACTS",uniqueConstraints = {
		
		@UniqueConstraint(columnNames= {
				"CONTRACTYPE_ID","MECHANIC_ID",
				"PROFESSIONALGROUP_ID"
		})	
})
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
	
	public Contract(Mechanic mechanic, ContractType type, ProfessionalGroup group, LocalDate endDate,double wage)
	{
		
		ArgumentChecks.isNotNull(mechanic);
		//ArgumentChecks.isNotNull(endDate);
		ArgumentChecks.isTrue(wage>= 0);
		ArgumentChecks.isNotNull(type);
		ArgumentChecks.isNotNull(group);
		
		
		
		this.mechanic=mechanic;
		this.startDate=LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());;
		this.endDate=endDate;
		this.annualbaseWage=wage;
		this.contracttype=type;
		this.professionalgroup=group;
		Associations.Group.link(this,group);
		Associations.Hire.link(mechanic,this);
		Associations.Type.link(this,type);
		
	
		
		
		
		
	}
	
	public Contract(Mechanic mechanic,ContractType type,ProfessionalGroup group,double wage)
	{
		
		this(mechanic,type,group,LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()),wage);
		
	}


	public double getAnnualBaseWage() {
		return annualbaseWage;
	}


	public void setAnnualWage(double annualWage) {
		this.annualbaseWage = annualWage;
	}


	public Optional<LocalDate> getEndDate() {
		return Optional.of(endDate);
	}


	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}


	public double getSettlement() {
		
		double payrool=calculateannualwage();
		
		this.settlement = payrool/365 * this.contracttype.getCompensationDays()*ChronoUnit.YEARS.between( 
			   this.startDate  , 
			   this.endDate
			) ;
		
		return this.settlement;
	}


	public void setSettlement() {
		
		double payrool=calculateannualwage();
		
		this.settlement = payrool/365 * this.contracttype.getCompensationDays()*ChronoUnit.YEARS.between( 
			   this.startDate  , 
			   this.endDate
			) ;
	}
	
	
	public double calculateannualwage()
	{
		double payrool=0;
		int times=this.getPayrolls().size()-12;
		int k=1;
		for(Payroll p:this.getPayrolls())
		{
			if(k<=times)
			{
				
			}
			else
			{
				payrool=payrool+p.getMonthlyWage()+p.getBonus()+p.getProductivityBonus()+p.getTrienniumPayment();
			}
			k++;
		}
		return payrool;
		
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


	public Set<Payroll> _getPayrools() {
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
		this.state=ContractState.TERMINATED;
		this.mechanic._setContract(Optional.empty());
		this.mechanic._getTerminatedContracts().add(this);
		this.firedmechanic=this.mechanic;
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
		return Objects.hash(firedmechanic, mechanic, startDate);
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
	
	
	
	
	

}
