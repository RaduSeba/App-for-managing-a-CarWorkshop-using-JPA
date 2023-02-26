package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;


@Entity
@Table(name="TPROFESSIONALGROUPS")
public class ProfessionalGroup extends BaseEntity {

	@Basic(optional=false)private String name;
	private double PRODUCTIVITYBONUSPERCENTAGE;
	private double trienniumpayment;
	
	
	@OneToMany(mappedBy="professionalgroup")private Set<Contract> contracts= new HashSet<>();

	ProfessionalGroup(){};
	
	public ProfessionalGroup(String name,double triennium,double productivityrate)
	{
		
		ArgumentChecks.isNotNull(name);

		ArgumentChecks.isTrue(triennium>= 0);
		
		this.name=name;
		this.PRODUCTIVITYBONUSPERCENTAGE=productivityrate;
		this.trienniumpayment=triennium;
		
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getProductivityBonusPercentage() {
		return PRODUCTIVITYBONUSPERCENTAGE;
	}


	public void setProductivityRate(double productivityRate) {
		this.PRODUCTIVITYBONUSPERCENTAGE = productivityRate;
	}


	public double getTrienniumPayment(){
		return trienniumpayment;
	}


	public void setTrienniumPayment(double trienniumSalary) {
		this.trienniumpayment = trienniumSalary;
	}


	public Set<Contract> _getContracts() {
		return contracts;
	}
	
	public Set<Contract> getContracts() {
		return new HashSet<>( contracts );
	}


	public void setContracts(Set<Contract> contracts) {
		this.contracts = contracts;
	}


	@Override
	public int hashCode() {
		return Objects.hash(name);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfessionalGroup other = (ProfessionalGroup) obj;
		return Objects.equals(name, other.name);
	}
	
	
	
	
	

}
