package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class ContractJpaRepository 
				extends BaseJpaRepository<Contract>
				implements ContractRepository{

	

	@Override
	public List<Contract> findAllInForce() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Contract> findAll()
	{
		return Jpa.getManager().createNamedQuery("Contract.findAll",Contract.class).getResultList();
	}

	@Override
	public List<Contract> findByMechanicId(String id) {
		
		List<Contract> l =Jpa.getManager().createNamedQuery("Contract.findbyMechanic",Contract.class).setParameter(1, id).getResultList();
		
		
			List<Contract> f = Jpa.getManager().createNamedQuery("Contract.findbyMechanicFired",Contract.class).setParameter(1, id).getResultList();
		
			List<Contract> mergedList = Stream.concat(l.stream(), f.stream())
                    .collect(Collectors.toList());
			
			
		return mergedList;
		
		
	}

	@Override
	public List<Contract> findByProfessionalGroupId(String id) {
		// TODO Auto-generated method stub
		return Jpa.getManager().createNamedQuery("Contract.findbyGroup",Contract.class).setParameter(1, id).getResultList();
	}

	@Override
	public List<Contract> findByContractTypeId(String id2Del) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contract> findAllInForceThisMonth(LocalDate present) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
