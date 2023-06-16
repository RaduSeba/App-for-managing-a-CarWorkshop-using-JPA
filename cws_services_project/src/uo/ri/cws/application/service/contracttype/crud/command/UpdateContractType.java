package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;

public class UpdateContractType  implements Command<Void> {

	private ContractTypeDto cdto;
	private ContractTypeRepository repo= Factory.repository.forContractType();
	
	
	
	public UpdateContractType(ContractTypeDto dto) {
		
		ArgumentChecks.isNotNull(dto, "please not null");
		ArgumentChecks.isNotEmpty(dto.name, "name cannot be empty");
		ArgumentChecks.isNotBlank(dto.name,"The name cannot be blank");
		ArgumentChecks.isNotNull(dto.name, "name cannot be null");
	
		
		if(dto.compensationDays<0)
		{
			throw new IllegalArgumentException( "Days cannot be negative" );
		}
		
		
		this.cdto=dto;
		
		
	}

	@Override
	public Void execute() throws BusinessException {
		
		
		Optional<ContractType> co=repo.findByName(cdto.name);
		
		BusinessChecks.exists(co);
		
		ContractType c=co.get();
		BusinessChecks.isTrue(cdto.version==c.getVersion(),"You are working on a stale data");
		
		c.setCompensationDays(cdto.compensationDays);
		
		return null;
	}

}
