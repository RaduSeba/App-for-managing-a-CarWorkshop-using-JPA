package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;

public class AddContractType implements Command<ContractTypeDto> {

	private ContractTypeDto cdto;
	private ContractTypeRepository ctrepo= Factory.repository.forContractType();
	
	
	public AddContractType(ContractTypeDto dto) {
		ArgumentChecks.isNotNull(dto,"The contracttype cannot be null");
		ArgumentChecks.isNotEmpty(dto.name, "Id cannot be empty");
		ArgumentChecks.isNotBlank(dto.name, "Id cannot be empty");
		ArgumentChecks.isNotNull(dto.name, "Id cannot be empty");
		ArgumentChecks.isNotNull(dto.compensationDays, "Compensation days cannot be null");
		
		if(dto.compensationDays<0)
		{
			throw new IllegalArgumentException( "Days cannot be negative" );
		}
		
		this.cdto=dto;
	}

	@Override
	public ContractTypeDto execute() throws BusinessException {
		
		Optional<ContractType> ct=ctrepo.findByName(cdto.name);
		
		BusinessChecks.isTrue(cdto.compensationDays>=0, "Compensation days must be higher than 0");
		
		BusinessChecks.isTrue(ct.isEmpty(),"The contract already exist");
		
		ContractType c=new ContractType(cdto.name,cdto.compensationDays);

		ctrepo.add(c);
		
		
		
		
		return DtoAssembler.toDto(c);
	}

}
