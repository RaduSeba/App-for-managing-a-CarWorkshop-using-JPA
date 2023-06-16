package uo.ri.cws.application.repository;

import java.util.Optional;

import uo.ri.cws.domain.ProfessionalGroup;

public interface ProfessionalGroupRepository extends Repository<ProfessionalGroup>{

	/**
	 * @param name
	 * @return the contract category object 
	 */
	Optional<ProfessionalGroup> findByName(String name);

}
