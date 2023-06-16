package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;

public class FindContractTypeByName implements Command<Optional<ContractTypeDto>> {

	
	private String n;
	private ContractTypeRepository repo= Factory.repository.forContractType();

	
	public FindContractTypeByName(String name) {
		
		ArgumentChecks.isNotEmpty(name,"The name cannot be empty");
		ArgumentChecks.isNotNull(name, "The name cannot be null");
		ArgumentChecks.isNotBlank(name,"The name cannot be blank");
		
		
		this.n=name;
	}

	@Override
	public Optional<ContractTypeDto> execute() throws BusinessException {
		// TODO Auto-generated method stub
		
		return repo.findByName(n).map(m->DtoAssembler.toDto(m));
	}

}
