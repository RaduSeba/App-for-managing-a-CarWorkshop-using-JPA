package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;

public class FindAllContractTypes implements  Command<List<ContractTypeDto>> {

	private ContractTypeRepository repo= Factory.repository.forContractType();

	
	@Override
	public List<ContractTypeDto> execute() throws BusinessException {
	
		List<ContractType> res=repo.findAll();
		
		return DtoAssembler.toContractTypeDtoList(res);
		
	}

}
