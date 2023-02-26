package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import uo.ri.cws.application.repository.VehicleRepository;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class VehicleJpaRepository extends BaseJpaRepository<Vehicle>
	implements VehicleRepository {

	@Override
	public Optional<Vehicle> findByPlate(String plate) {
		return Jpa.getManager()
				.createNamedQuery("Vehicle.findByPlate", Vehicle.class)
				.setParameter(1, plate)
				.getResultList().stream()
				.findFirst();
	}

}
