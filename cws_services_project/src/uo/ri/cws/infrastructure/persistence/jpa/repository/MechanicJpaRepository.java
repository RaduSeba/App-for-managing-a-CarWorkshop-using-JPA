package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class MechanicJpaRepository
		 extends BaseJpaRepository<Mechanic>
		implements MechanicRepository {

	

	@Override
	public Optional<Mechanic> findByDni(String dni) {
		
		
		
		Optional<Mechanic> om=Jpa.getManager().createNamedQuery("Mechanic.findByDni",Mechanic.class)
				.setParameter(1, dni).getResultList().stream().findFirst();
		
		return om;
	}
	
	@Override
	public List<Mechanic> findAll()
	{
		return Jpa.getManager().createNamedQuery("Mechanic.findAll",Mechanic.class)
				.getResultList();
	}
	
	public List<Mechanic> findAllInForce(){
		return Jpa.getManager().createNamedQuery("Mechanic.findAllInForce",Mechanic.class)
				.getResultList();
	}

	public List<Mechanic> findInForceInContractType(ContractType contractType){
		
		return Jpa.getManager().createNamedQuery("Mechanic.findAllInContractType",Mechanic.class).setParameter(1, contractType.getName())
				.getResultList();
	}

	public List<Mechanic> findAllInProfessionalGroup(ProfessionalGroup group){
		
		return Jpa.getManager().createNamedQuery("Mechanic.findAllInProfessionalGroup",Mechanic.class).setParameter(1, group.getName())
				.getResultList();
	}

}
