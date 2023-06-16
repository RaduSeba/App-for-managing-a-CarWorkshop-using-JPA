package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;

public class FindMechanicsInForce implements Command<List<MechanicDto>> {
	
	private MechanicRepository repo = Factory.repository.forMechanic();

	@Override
	public List<MechanicDto> execute() throws BusinessException {
	
		List<Mechanic> res=repo.findAllInForce();
		
		 // BusinessChecks.isFalse(res.get(0)==null,"The contract doesn t have mechanics in force");
		if(!res.isEmpty()) {
			
		

			for (int i = res.size() - 1; i >= 0; i--) {
			    if (res.get(i) == null) {
			        res.remove(i); // Remove the null object
			    }
			}

		}
		
		
		return DtoAssembler.toMechanicDtoList(res);
	}

}
