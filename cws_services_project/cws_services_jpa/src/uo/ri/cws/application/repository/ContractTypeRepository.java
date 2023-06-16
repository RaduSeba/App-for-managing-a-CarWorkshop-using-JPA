package uo.ri.cws.application.repository;

import java.util.Optional;

import uo.ri.cws.domain.ContractType;

public interface ContractTypeRepository extends Repository<ContractType>{

	/**
	 * @param name
	 * @return the contract type object 
	 */
	Optional<ContractType> findByName(String name);

}
