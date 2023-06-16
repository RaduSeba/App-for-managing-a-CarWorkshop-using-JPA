package uo.ri.cws.application.service.professionalgroup.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.professionalgroup.assembler.ProfessionalGroupAssembler;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;

public class AddProfessionalGroup implements Command<ProfessionalGroupBLDto>{
	
	private ProfessionalGroupRepository prepo = Factory.repository.forProfessionalGroup();
	private ProfessionalGroupBLDto pg = null;

	
	public AddProfessionalGroup(ProfessionalGroupBLDto dto) {
		ArgumentChecks.isNotNull(dto,"The professionalGroup cannot be null");
		ArgumentChecks.isNotEmpty(dto.name, "Id cannot be empty");
		ArgumentChecks.isNotBlank(dto.name, "Id cannot be empty");
		ArgumentChecks.isNotNull(dto.name, "Id cannot be empty");
		//ArgumentChecks.isNotNull(dto, "Compensation days cannot be null");
		
		if(dto.productivityRate<0)
		{
			throw new IllegalArgumentException( "Days cannot be negative" );
		}
		
		this.pg=dto;
	}

	@Override
	public ProfessionalGroupBLDto execute() throws BusinessException {
	
		Optional<ProfessionalGroup> o = prepo.findByName(pg.name);
		BusinessChecks.isTrue(o.isEmpty(),"The professionalGroup already exist");
		//BusinessChecks.isTrue(pg.productivityRate<0,"The productivity cannot be negative");
		
		ProfessionalGroup p= new ProfessionalGroup(pg.name,pg.trieniumSalary,pg.productivityRate);

		prepo.add(p);
		
		
		
		return ProfessionalGroupAssembler.toDto(p);
	}

}
