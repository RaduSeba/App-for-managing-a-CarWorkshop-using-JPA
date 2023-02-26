package uo.ri.cws.application.service.contract.crud.command;

import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contract.ContractService.ContractSummaryDto;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;

public class FindAllContracts implements Command<List<ContractSummaryDto>> {

	private ContractRepository repo=  Factory.repository.forContract();
	
	@Override
	public List<ContractSummaryDto> execute() throws BusinessException {

		List<Contract> res =repo.findAll();
		
		
		return DtoAssembler.toContractSummaryDtoList(res);
	}

}
