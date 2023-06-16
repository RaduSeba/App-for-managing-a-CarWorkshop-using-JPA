package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;

public class DeleteMechanic implements Command<Void> {

	private String mechanicId;
	private MechanicRepository repo = Factory.repository.forMechanic();
	
	
	public DeleteMechanic(String mechanicId) {
		
		ArgumentChecks.isNotNull(mechanicId, "THe mechanic id is null");
		ArgumentChecks.isNotEmpty(mechanicId, "The id cannot be null");
		ArgumentChecks.isNotBlank(mechanicId, "The id cannot be null");
		
		this.mechanicId = mechanicId;
	}

	@Override
	public Void execute() throws BusinessException {

	
		
		 //Optional<Mechanic> om=repo.findById(Mechanic.class, mechanicId);
		 Optional<Mechanic> om=repo.findById(mechanicId);
		  BusinessChecks.exists(om, "THe mechanic doesn t exist");
		  Mechanic m =om.get();
		  
		
		  BusinessChecks.isFalse(m.getInterventions().size()>
		  0,"The mechanic has interventions");
		  BusinessChecks.isTrue(m.getAssigned().size()==
		  0,"The mechanic has workOrders");
		  
		  repo.remove(m);
		 
		
		
	
		
		
		return null;
	}

}
