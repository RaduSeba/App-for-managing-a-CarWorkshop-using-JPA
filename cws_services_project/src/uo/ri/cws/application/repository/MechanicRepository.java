package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Mechanic;

public interface MechanicRepository extends Repository<Mechanic> {

	/**
	 * @param dni
	 * @return the mechanic identified by the dni or null if none
	 */
	Optional<Mechanic> findByDni(String dni);

	/**
	 * @return a list with all mechanics (might be empty)
	 */
	List<Mechanic> findAll();
}
