package uo.ri.cws.application.repository;

import java.time.LocalDate;
import java.util.List;

import uo.ri.cws.domain.Contract;

public interface ContractRepository extends Repository<Contract> {


    /**
     * @return a list with all contracts (might be empty)
     */
    @Override
	List<Contract> findAll();

    
    /**
     * @return a list with all contracts in force (might be empty) 
     */
    List<Contract> findAllInForce();


	List<Contract> findByMechanicId(String id);


	List<Contract> findByProfessionalGroupId(String id);
	
	List<Contract> findByContractTypeId(String id2Del);


	List<Contract> findAllInForceThisMonth(LocalDate present);

}
