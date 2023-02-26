package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;


@Entity
@Table(name="TVEHICLETYPES")
public class VehicleType extends BaseEntity {
	// natural attributes
	@Column(unique = true)private String name;
	private double pricePerHour;

	// accidental attributes
	@OneToMany(mappedBy ="VehicleType") private Set<Vehicle> vehicles = new HashSet<>();
	
	VehicleType(){}




	public Set<Vehicle> getVehicles() {
		return new HashSet<>( vehicles );
	}

	Set<Vehicle> _getVehicles() {
		return vehicles;
	}

	public VehicleType(String name, double pricePerHour) {
		
		
		ArgumentChecks.isNotBlank(name);
		ArgumentChecks.isTrue(pricePerHour >=0);
		
		
		
		this.name = name;
		this.pricePerHour = pricePerHour;
		
	}

public VehicleType(String name) {
		
		
		ArgumentChecks.isNotBlank(name);
		
		this.name = name;
}
	
		

	public String getName() {
		return name;
	}

	public double getPricePerHour() {
		return pricePerHour;
	}

	public void setVehicles(Set<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	@Override
	public String toString() {
		return "VehicleType [name=" + name + ", pricePerHour=" + pricePerHour
				+  "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, pricePerHour);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VehicleType other = (VehicleType) obj;
		return Objects.equals(name, other.name)
				&& Double.doubleToLongBits(pricePerHour) == Double
						.doubleToLongBits(other.pricePerHour);
	}

	
	
	
	
	
	

}
