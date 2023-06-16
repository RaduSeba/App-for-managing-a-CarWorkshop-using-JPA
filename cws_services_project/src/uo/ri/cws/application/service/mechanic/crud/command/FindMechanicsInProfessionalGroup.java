package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;

import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;

public class FindMechanicsInProfessionalGroup  implements Command<List<MechanicDto>>{

	
	private String name;
	private MechanicRepository repo = Factory.repository.forMechanic();
	private ProfessionalGroupRepository repop= Factory.repository.forProfessionalGroup();

	public FindMechanicsInProfessionalGroup(String name) {
		ArgumentChecks.isNotNull(name, "THe name is null");
		ArgumentChecks.isNotEmpty(name, "THe name is empty");
		ArgumentChecks.isNotBlank(name, "The name cannot be blank");
		this.name = name;
	}
	
	@Override
	public List<MechanicDto> execute() throws BusinessException {
		
		Optional<ProfessionalGroup> pg= repop.findByName(name);
		
		
		
		if (pg.isEmpty())
		{
		    return DtoAssembler.toMechanicDtoList(new ArrayList<Mechanic>());
		}

		
		ProfessionalGroup p = new ProfessionalGroup(pg.get().getName(),pg.get().getTrienniumPayment(),pg.get().getProductivityBonusPercentage());
		
		List<Mechanic> res=repo.findAllInProfessionalGroup(p);
		
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
