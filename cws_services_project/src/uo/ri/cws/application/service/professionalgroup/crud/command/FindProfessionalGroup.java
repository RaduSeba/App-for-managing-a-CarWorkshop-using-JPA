package uo.ri.cws.application.service.professionalgroup.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.professionalgroup.assembler.ProfessionalGroupAssembler;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;

public class FindProfessionalGroup implements Command<Optional<ProfessionalGroupBLDto>> {
	
	private String n;
	private ProfessionalGroupRepository prepo = Factory.repository.forProfessionalGroup();
	private ContractRepository repo=  Factory.repository.forContract();

	
	
	public FindProfessionalGroup(String name) {
		
		ArgumentChecks.isNotEmpty(name,"The name cannot be empty");
		ArgumentChecks.isNotNull(name, "The name cannot be null");
		ArgumentChecks.isNotBlank(name,"The name cannot be blank");
		this.n=name;
		
	}

	@Override
	public Optional<ProfessionalGroupBLDto> execute() throws BusinessException {
	
		return prepo.findByName(n).map(m->ProfessionalGroupAssembler.toDto(m));

	}

}
