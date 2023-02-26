package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;


@Entity
@Table(name="TPAYROLLS")
public class Payroll extends BaseEntity {
	
	@Basic(optional=false)private LocalDate date;
	private double bonus;
	private double incomeTax;
	private double monthlyWage;
	private double nic;
	private double productivityBonus;
	private double trienniumPayment;
	
	@ManyToOne private Contract contract;
	

	
	Payroll(){};
	
	public Payroll( Contract contract,LocalDate date) {
		
		ArgumentChecks.isNotNull(contract);
		ArgumentChecks.isNotNull(date);
		
		
		this.date = date;
		this.contract = contract;
		this.monthlyWage=this.contract.getAnnualBaseWage()/14;
		this.nic=Math.floor(((this.contract.getAnnualBaseWage() * 0.05) / 12) * 100) / 100;
		calculatebonus();
		
		calculatetrinennium();
		calculateproductivity();
		calculateincometax();
		Associations.Run.link(contract,this);
	}


	public Payroll(Contract contract) {
		
		ArgumentChecks.isNotNull(contract);
		
		
		this.contract = contract;
		this.date=LocalDate.now();
		this.monthlyWage=this.contract.getAnnualBaseWage()/14;
		this.bonus=this.contract.getAnnualBaseWage()/14;
		this.nic=Math.floor(((this.contract.getAnnualBaseWage() * 0.05) / 12) * 100) / 100;
		calculatebonus();
	
		calculateproductivity();
		calculatetrinennium();
		calculateincometax();
		Associations.Run.link(contract,this);
	}


	public Payroll(Contract contract,LocalDate d, double monthlyWage,double extra,double productivity,
			double trienniums, double tax, double nic)
	{
		ArgumentChecks.isNotNull(contract);
		ArgumentChecks.isNotNull(d);

		ArgumentChecks.isTrue(monthlyWage>=0);
		
		this.contract=contract;
		this.date=d;
		this.monthlyWage=monthlyWage;
		this.bonus=extra;
		this.productivityBonus=productivity;
		this.trienniumPayment=trienniums;
		this.incomeTax=tax;
		this.nic=nic;
		
		Associations.Run.link(contract,this);
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public double getBonus() {
		return bonus;
	}


	public void setBonus(double bonus) {
		this.bonus = bonus;
	}


	public double getIncomeTax() {
		return incomeTax;
	}


	public void setIncomeTax(double incomeTax) {
		this.incomeTax = incomeTax;
	}


	public double getMonthlyWage() {
		return monthlyWage;
	}


	public void setMonthlyWage(double monthlyWage) {
		this.monthlyWage = monthlyWage;
	}


	public double getNIC() {
		return nic;
	}


	public void setNic(double nic) {
		this.nic = nic;
	}


	public double getProductivityBonus() {
		return productivityBonus;
	}


	public void setProductivityBonus(double productivityBonus) {
		this.productivityBonus = productivityBonus;
	}


	public double getTrienniumPayment() {
		return trienniumPayment;
	}


	public void setTrenniumPayment(double trenniumPayment) {
		this.trienniumPayment = trenniumPayment;
	}


	public Contract getContract() {
		return contract;
	}


	public void setContract(Contract contract) {
		this.contract = contract;
	}


	@Override
	public int hashCode() {
		return Objects.hash(contract, date);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payroll other = (Payroll) obj;
		return Objects.equals(contract, other.contract)
				&& Objects.equals(date, other.date);
	}
	
	
	public void calculateincometax()
	{
		
		if(this.bonus!=0)
		{
			this.incomeTax= this.monthlyWage * 2  + this.productivityBonus + this.trienniumPayment;
		}
		else
		{
			this.incomeTax= this.monthlyWage  + this.productivityBonus + this.trienniumPayment;
		}
		
		
		
		if(this.contract.getAnnualBaseWage()<12450)
		{
			this.incomeTax=this.incomeTax*0.19;
		}
		if(this.contract.getAnnualBaseWage()>12450 &&this.contract.getAnnualBaseWage()<20200 )
		{
			this.incomeTax=this.incomeTax*0.24;
		}
		if(this.contract.getAnnualBaseWage()>20200 &&this.contract.getAnnualBaseWage()<35200)
		{
			this.incomeTax=this.incomeTax*0.30;
		}
		if(this.contract.getAnnualBaseWage()>35200 && this.contract.getAnnualBaseWage()<60000)
		{
			this.incomeTax=this.incomeTax*0.37;
		}
		if(this.contract.getAnnualBaseWage()>60000 &&this.contract.getAnnualBaseWage()<300000)
		{
			this.incomeTax=this.incomeTax*0.45;
		}
		if(this.contract.getAnnualBaseWage()>300000)
		{
			this.incomeTax=this.incomeTax*0.47;
		}
	}
	
	
	public void calculateproductivity()
	{
		if(this.contract.getMechanic().get()._getAssigned().isEmpty()==false)
		{
			 this.productivityBonus=  this.contract.getMechanic().get()._getAssigned().iterator().next().getAmount() *this.contract.getProfessionalGroup().getProductivityBonusPercentage() / 100;

		}
	}
	
	
	
	public void calculatetrinennium()
	{
		if(ChronoUnit.YEARS.between( 
				   this.contract.getStartDate()  , 
				   this.date
				)>=3)
		{
			this.trienniumPayment=this.contract.getProfessionalGroup().getTrienniumPayment();
		}
		
	}
	
	public void calculatebonus()
	{
		if(this.date.equals(LocalDate.of(2022, 11, 30)) )
		{
			this.bonus=0;
		}
		else
		{
			this.bonus=this.contract.getAnnualBaseWage()/14;
		}
		
	}
	
	
	
	
	
	
	

}
