package uo.ri.cws.application.service.professionalgroup.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;

public class UpdateProfessionalGroup implements Command<Void> {
	
	private ProfessionalGroupRepository prepo = Factory.repository.forProfessionalGroup();
	private ProfessionalGroupBLDto pg = null;

	
	public UpdateProfessionalGroup(ProfessionalGroupBLDto dto) {
		
	ArgumentChecks.isNotNull(dto,"The professionalGroup cannot be null");
	ArgumentChecks.isNotEmpty(dto.name, "Id cannot be empty");
	ArgumentChecks.isNotBlank(dto.name, "Id cannot be empty");
	ArgumentChecks.isNotNull(dto.name, "Id cannot be empty");
	//ArgumentChecks.isNotNull(dto, "Compensation days cannot be null");
	
	if(dto.productivityRate<0)
	{
		throw new IllegalArgumentException( "productivity cannot be negative" );
	}
	
	if(dto.trieniumSalary<0)
	{
		throw new IllegalArgumentException( "trieniumsalary cannot be negative" );
	}
	
	this.pg=dto;
	}

	@Override
	public Void execute() throws BusinessException {
		
		
		Optional<ProfessionalGroup> o = prepo.findByName(pg.name);
		BusinessChecks.isFalse(o.isEmpty(),"The professionalGroup doesn t exist");
	
		
		
		
		ProfessionalGroup p= o.get();
		BusinessChecks.isTrue(pg.version==p.getVersion(),"You are working on a stale data");
		
		p.setProductivityRate(pg.productivityRate);
		p.setTrienniumPayment(pg.trieniumSalary);

		
		
		
		return null;
	}

}
