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
@Table(name="TCONTRACTTYPES")
public class ContractType extends BaseEntity {

	@Basic(optional=false)private String name;
	private double compensationDays;
	
	
	
	
	@OneToMany(mappedBy="contracttype")private Set<Contract> contracts= new HashSet<>();

	ContractType(){};
	
	public ContractType(String name,double compensation) {
		
		
		ArgumentChecks.isNotNull(name);

		ArgumentChecks.isTrue(compensation>= 0);
		this.name=name;
		this.compensationDays=compensation;
		
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public double getCompensationDays() {
		return compensationDays;
	}



	public void setCompensationDays(double compensationDays) {
		this.compensationDays = compensationDays;
	}



	public Set<Contract> getContracts() {
		return new HashSet<>( contracts );
	}
	
	 Set<Contract> _getContracts() {
		return contracts ;
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
		ContractType other = (ContractType) obj;
		return Objects.equals(name, other.name);
	}
	
	
	
	
	

}
