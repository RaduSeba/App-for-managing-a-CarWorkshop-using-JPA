package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name="TCLIENTS")
public class Client extends BaseEntity {
	
	
	
	



	@Column(unique = true)private String dni;
	@Basic(optional=false) private String name;
	@Basic(optional=false)private String surname;
	@Basic(optional=false)private String email;
	@Basic(optional=false)private String phone;
	private Address address;
	
	
	@OneToMany(mappedBy="client") private Set<PaymentMean> paymeantMeans = new HashSet<>();
	
	
	@OneToMany(mappedBy ="client") 
	private Set<Vehicle> vehicles = new HashSet<>();
	
	
	Client(){}
	
	
	
	public Set<PaymentMean> _getPaymeantMean() {
		return paymeantMeans;
	}

	public Set<PaymentMean> getPaymentMeans() {
		
		return new HashSet<>(paymeantMeans);
	}



	public void setPaymeantMeans(Set<PaymentMean> paymeantMean) {
		this.paymeantMeans = paymeantMean;
	}

	public void _setPaymeantMean(PaymentMean paymeantMean) {
		
		//return new HashSet<>(paymentMean);
	}

	public Set<Vehicle> _getVehicles() {
		return vehicles;
	}


	public Set<Vehicle> getVehicles() {
		return new HashSet<>(vehicles);
	}




	public void _setVehicles(Set<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	
	
	
	
	public Client(String dni) {
		
		this(dni, "no-name", "no-surname", "no-email", "no-phone", new Address());
	}




	public Client(String dni, String name, String surname, String email,
			String phone, Address address) {
		
		ArgumentChecks.isNotBlank(dni);
		ArgumentChecks.isNotBlank(name);
		ArgumentChecks.isNotBlank(surname);
		ArgumentChecks.isNotBlank(email);
		ArgumentChecks.isNotBlank(phone);
		ArgumentChecks.isNotNull(address);
		
		this.dni = dni;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}
	
	public Client(String dni, String name, String surname) {
		
		
		
		this(dni, name,surname, "no-email", "no-phone", new Address());
		
	}




	public String getDni() {
		return dni;
	}



	public String getName() {
		return name;
	}



	public String getSurname() {
		return surname;
	}



	public String getEmail() {
		return email;
	}



	public void setDni(String dni) {
		this.dni = dni;
	}

	
	public void setAddress(Address a)
	{
		this.address=a;
	}



	@Override
	public int hashCode() {
		return Objects.hash(address, dni, email, name, phone, surname);
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(address, other.address)
				&& Objects.equals(dni, other.dni)
				&& Objects.equals(email, other.email)
				&& Objects.equals(name, other.name)
				&& Objects.equals(phone, other.phone)
				&& Objects.equals(surname, other.surname);
	}




	@Override
	public String toString() {
		return "Client [dni=" + dni + ", name=" + name + ", surname=" + surname
				+ ", email=" + email + ", phone=" + phone + ", address="
				+ address + "]";
	}




	public String getPhone() {
		return phone;
	}



	public Address getAddress() {
		return address;
	}


	
	
	

}

