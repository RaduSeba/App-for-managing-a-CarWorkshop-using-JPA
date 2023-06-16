package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contract.ContractAssembler;
import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;

public class FindContractById implements Command<Optional<ContractDto>> {

	
	private String id;
	private ContractRepository repo=  Factory.repository.forContract();
	
	public FindContractById(String id) {
		
		ArgumentChecks.isNotNull(id, "ID cannot be null");
		ArgumentChecks.isNotEmpty(id, "The id cannot be empty");
		
		this.id=id;
	}

	@Override
	public Optional<ContractDto> execute() throws BusinessException {
	
		return  repo.findById(id).map(m->ContractAssembler.toDto(m));
	}

}
