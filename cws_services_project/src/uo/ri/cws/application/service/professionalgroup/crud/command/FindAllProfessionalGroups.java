package uo.ri.cws.application.service.professionalgroup.crud.command;

import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.professionalgroup.assembler.ProfessionalGroupAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ProfessionalGroup;

public class FindAllProfessionalGroups implements  Command< List<ProfessionalGroupBLDto>>{

	
	private ProfessionalGroupRepository prepo = Factory.repository.forProfessionalGroup();

	
	
	@Override
	public List<ProfessionalGroupBLDto> execute() throws BusinessException {
		
		List<ProfessionalGroup> l = prepo.findAll();
		
		return ProfessionalGroupAssembler.toProfessionalGroupDtoList(l);
	}

}
