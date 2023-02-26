package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name="TVEHICLES")
public class Vehicle extends BaseEntity {
	@Column(unique = true)private String plateNumber;
	@Basic(optional=false) @Column(name="brand")private String make;
	@Basic(optional=false)private String model;
	
	
	
	@ManyToOne  private Client client;
	
	@ManyToOne private VehicleType vehicleType; 
	
	@OneToMany(mappedBy="Vehicle") private Set<WorkOrder> workOrder = new HashSet<>();
	
	
	Vehicle(){}
	
	
	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrder);
	}
	

	public Set<WorkOrder> _getWorkOrders() {
		return workOrder;
	}





	public void setWorkOrder(Set<WorkOrder> workOrder) {
		this.workOrder = workOrder;
	}





	public VehicleType getVehicleType() {
		return vehicleType;
	}





	public void _setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}





	public Client getClient() {
		return client;
	}





	public void _setClient(Client client) {
		this.client = client;
	}





	public Vehicle(String plateNumber, String make, String model) {
		
		ArgumentChecks.isNotBlank(model);
		ArgumentChecks.isNotBlank(make);
		ArgumentChecks.isNotBlank(plateNumber);
		
		
		this.plateNumber = plateNumber;
		this.make = make;
		this.model = model;
	}

public Vehicle(String plateNumber) {
		
		
		ArgumentChecks.isNotBlank(plateNumber);
		
		
		this.plateNumber = plateNumber;
		
}



	





	@Override
public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + Objects.hash(plateNumber);
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
	Vehicle other = (Vehicle) obj;
	return Objects.equals(plateNumber, other.plateNumber);
}


	public String getPlateNumber() {
		return plateNumber;
	}





	public String getMake() {
		return make;
	}





	public String getModel() {
		return model;
	}


	public void _setWorkOrder(WorkOrder workOrder2) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	

}


