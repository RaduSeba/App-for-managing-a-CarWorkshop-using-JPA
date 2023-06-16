package uo.ri.cws.domain;

import java.util.Optional;

public class Associations {

	public static class Own {

		public static void link(Client client, Vehicle vehicle) {
			
			vehicle._setClient(client);
			client._getVehicles().add(vehicle);
			
		}

		public static void unlink(Client client, Vehicle vehicle) {
			
			client._getVehicles().remove(vehicle);
			vehicle._setClient(null);
			
			
		}

	}

	public static class Classify {

		public static void link(VehicleType vehicleType, Vehicle vehicle) {
			
			vehicle._setVehicleType(vehicleType);
			vehicleType._getVehicles().add(vehicle);
			// TODO Auto-generated method stub
		}

		public static void unlink(VehicleType vehicleType, Vehicle vehicle) {
			
			vehicleType._getVehicles().remove(vehicle);
			vehicle._setVehicleType(null);
			
			
		}

	}

	public static class Pay {

		public static void link( PaymentMean pm,Client client) {
			
			pm._setClient(client);
			client._getPaymeantMean().add(pm);
		}

		public static void unlink(Client client, PaymentMean pm) {
			
			client._getPaymeantMean().remove(pm);
			pm._setClient(null);
		}

	}

	public static class Fix {

		public static void link(Vehicle vehicle, WorkOrder workOrder) {
			
			workOrder._setVehicle(vehicle);
			vehicle._getWorkOrders().add(workOrder);
			
			
		}

		public static void unlink(Vehicle vehicle, WorkOrder workOrder) {
			
			vehicle._getWorkOrders().remove(workOrder);
			workOrder._setVehicle(null);
			
		}

	}

	public static class ToInvoice {

		public static void link(Invoice invoice, WorkOrder workOrder) {
			
			workOrder._setInvoice(invoice);
			invoice._getWorkOrders().add(workOrder);
			
		}

		public static void unlink(Invoice invoice, WorkOrder workOrder) {
			
			invoice._getWorkOrders().remove(workOrder);
			workOrder._setInvoice(null);
			
		}
	}

	public static class ToCharge {

		public static void link(PaymentMean pm, Charge charge, Invoice i) {
			
			
			pm._getCharges().add(charge);
			i._getCharges().add(charge);
			
		}

		public static void unlink(Charge charge) {
			
			
			
			charge.getPaymentMean()._getCharges().remove(charge);
			charge.getInvoice()._getCharges().remove(charge);
			charge._setPaymentMean(null);
			charge._setInvoice(null);
			
		}

	}

	public static class Assign {

		public static void link(Mechanic mechanic, WorkOrder workOrder) {
			workOrder._setMechanic(mechanic);
			mechanic._getAssigned().add(workOrder);
		}

		public static void unlink(Mechanic mechanic, WorkOrder workOrder) {
			
			mechanic._getAssigned().remove(workOrder);
			workOrder._setMechanic(null);
			
		}

	}

	public static class Intervene {

		public static void link(WorkOrder workOrder, Intervention intervention,
				Mechanic mechanic) {
			
			
			intervention._setMechanic(mechanic);
			intervention._setWorkOrder(workOrder);
			
			workOrder._getInterventions().add(intervention);
			mechanic._getInterventions().add(intervention);
		}

		
		public static void unlink(Intervention intervention) {
			
			

			intervention.getWorkOrder()._getInterventions().remove(intervention);
			intervention.getMechanic()._getInterventions().remove(intervention);
			
			
			intervention._setMechanic(null);
			intervention._setWorkOrder(null);
			
			
		}

	}

	public static class Substitute {

		public static void link(SparePart spare, Substitution subtitution,
				Intervention intervention) {
		
			subtitution._setIntervention(intervention);
			subtitution._setSparePart(spare);
			
			
			spare._getSubstitutions().add(subtitution);
			intervention._getSubstitutions().add(subtitution);
			
			
		}

		public static void unlink(Substitution sustitution) {
			
			sustitution.getIntervention()._getSubstitutions().remove(sustitution);
			//sustitution.getIntervention()._getSubstitutions().remove(sustitution);
			sustitution.getSparePart()._getSubstitutions().remove(sustitution);
			
			
			sustitution.setIntervention(null);
			sustitution._setSparePart(null);
			
			
		}

	}
	
	public static class Fire {

		public static void link(Contract contract, Mechanic mechanic) {
			
			//contract.getMechanic().get()._getTerminatedContracts().add(contract);
			mechanic._setContract(Optional.empty());
			mechanic._getTerminatedContracts().add(contract);
			
			//contract.getMechanic().get().setContract(null);
			contract._setFiredMechanic(Optional.ofNullable(mechanic));
			//contract.setMechanic(null);
			
		
			
		}

		public static void unlink( Contract contract, Mechanic mechanic) {
			Optional<Mechanic> empty = Optional.empty();
			
			//contract.getMechanic().get()._getTerminatedContracts().remove(contract);
			mechanic._getTerminatedContracts().remove(contract);
			contract._setFiredMechanic(empty);
			
			
		}

	}
	public static class Hire {

		public static void link(Mechanic mechanic, Contract contract) {
			
			contract.setMechanic(Optional.of(mechanic));
			mechanic._setContract(Optional.of(contract));
			
		}

		public static void unlink( Contract contract,Mechanic mechanic) {
			
			
			Optional<Contract> empty = Optional.empty();
			//Optional<Mechanic> empty2 = Optional.empty();
			
			//contract.setMechanic(empty2);
			contract.setFiredMechanic(mechanic);
			mechanic._getTerminatedContracts().add(contract);
			mechanic._setContract(empty);
			
			
			
		}

	}
	
	public static class Type {

		public static void link( Contract contract,ContractType type) {
			
			contract.setContractType(type);
			type._getContracts().add(contract);
			
		}

		public static void unlink(Contract contract,ContractType type) {
			
			type._getContracts().remove(contract);
			contract.setContractType(null);
			
			
		}

	}
	
	public static class Group {

		public static void link( Contract contract,ProfessionalGroup group) {
			
				contract.setProffesionalGroup(group);
				group._getContracts().add(contract);
			
		}

		public static void unlink(Contract contract,ProfessionalGroup group) {
			
			group._getContracts().remove(contract);
			contract.setProffesionalGroup(null);
			
			
		}

	}
	
	public static class Run {

		public static void link( Contract contract,Payroll payrool) {
			
			contract._getPayrools().add(payrool);
			payrool.setContract(contract);
		
			
		}

		public static void unlink(Contract contrac,Payroll payrool) {
			
			//payrool.getContract()._getPayrools().remove(payrool);
			contrac._getPayrools().remove(payrool);
			payrool.setContract(null);
			
			
		}

	}

}
