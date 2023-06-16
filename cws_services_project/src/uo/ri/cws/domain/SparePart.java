package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name="TSPAREPARTS")
public class SparePart extends BaseEntity {
	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}

	// natural attributes
	@Column(unique = true)private String code;
	@Basic(optional=false) private String description;
	private double price;

	// accidental attributes
	@OneToMany(mappedBy ="sparePart") private Set<Substitution> substitutions = new HashSet<>();
	
	SparePart(){}


	public SparePart(String code,String description,double price)
	{
		this.code=code;
		this.description=description;
		this.price=price;
	}
	
	public SparePart(String code)
	{
		this.code=code;
		
	}


	public Set<Substitution> getSustitutions() {
		return new HashSet<>( substitutions );
	}

	Set<Substitution> _getSubstitutions() {
		return substitutions;
	}


	
	
	

}
