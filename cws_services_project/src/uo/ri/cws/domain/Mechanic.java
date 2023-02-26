package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import uo.ri.cws.domain.Contract.ContractState;
import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name="TMECHANICS")
public class Mechanic extends BaseEntity {
	
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getSurname() {
		return surname;
	}

	public String getName() {
		return name;
	}

	public void setInterventions(Set<Intervention> interventions) {
		this.interventions = interventions;
	}

	// natural attributes
	@Column(unique = true)private String dni;
	@Basic(optional=false)private String surname;
	@Basic(optional=false)private String name;

	// accidental attributes
	
	@OneToMany(mappedBy ="Mechanic") 
	private Set<WorkOrder> assigned = new HashSet<>();
	

	@OneToOne(mappedBy="mechanic")private Contract contractinforce ;
	
	
	@OneToMany(mappedBy ="Mechanic") 
	private Set<Contract> contractsterminated = new HashSet<>();
	
	
	@OneToMany(mappedBy ="Mechanic") 
	 private Set<Intervention> interventions = new HashSet<>();

	Mechanic(){}
		
	public Set<WorkOrder> getAssigned() {
		return new HashSet<>( assigned );
	}

	Set<WorkOrder> _getAssigned() {
		return assigned;
	}

	public Set<Intervention> getInterventions() {
		return new HashSet<>( interventions );
	}

	Set<Intervention> _getInterventions() {
		return interventions;
	}
	
	public Optional<Contract> getContractInForce() {
		if(this.contractinforce==null)
		{
			return Optional.empty();
		}
		else
		{
			return  Optional.of(this.contractinforce);
		}
	}
	

	
	public boolean isInForce()
	{
		if(this.contractinforce.getState()==ContractState.IN_FORCE)
		{
			return true;
		}
		return false;
	}
	
	
	

	public Mechanic(String dni) {
		
		this(dni,"no-surname","no-name");
	}

	
	
	public Mechanic(String dni, String surname, String name,
			Set<WorkOrder> assigned, Set<Intervention> interventions) {
		
		
		
		
		
		this.dni = dni;
		this.surname = surname;
		this.name = name;
		this.assigned = assigned;
		this.interventions = interventions;
	}

	public Mechanic(String dni2, String name2, String surname2) {
		
		this.dni = dni2;
		this.surname = surname2;
		this.name = name2;
	}
	
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public void setSurname(String name)
	{
		this.surname=name;
	}

	@Override
	public String toString() {
		return "Mechanic [dni=" + dni + ", surname=" + surname + ", name="
				+ name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(dni);
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
		Mechanic other = (Mechanic) obj;
		return Objects.equals(dni, other.dni);
	}
	
	
	public Set<Contract> getTerminatedContracts() {
		return new HashSet<>(contractsterminated);
	}
	
	public Set<Contract> _getTerminatedContracts() {
		return contractsterminated;
	}

	
	
	public void _setContract(Optional<Contract> c)
	{
		if(c.isEmpty())
		{
			this.contractinforce=null;
		}
		else
		{
			this.contractinforce=c.get();
		}
	}
	
	

}
