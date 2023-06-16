package uo.ri.cws.application.service.contract.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contract.ContractService;
import uo.ri.cws.application.service.contract.crud.command.AddContract;
import uo.ri.cws.application.service.contract.crud.command.DeleteContract;
import uo.ri.cws.application.service.contract.crud.command.FindAllContracts;
import uo.ri.cws.application.service.contract.crud.command.FindContractById;
import uo.ri.cws.application.service.contract.crud.command.FindContractsByMechanic;
import uo.ri.cws.application.service.contract.crud.command.TerminateContract;
import uo.ri.cws.application.service.contract.crud.command.UpdateContract;
import uo.ri.cws.application.util.command.CommandExecutor;

public class ContractServiceImpl implements ContractService {

	public ContractServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	
	private CommandExecutor executor = Factory.executor.forExecutor();
	
	@Override
	public ContractDto addContract(ContractDto c) throws BusinessException {
		return executor.execute(new AddContract( c ));
	}

	@Override
	public void updateContract(ContractDto dto) throws BusinessException {
		executor.execute(new UpdateContract( dto ));
		
	}

	@Override
	public void deleteContract(String id) throws BusinessException {
		 executor.execute(new DeleteContract( id ));
		
	}

	@Override
	public void terminateContract(String contractId) throws BusinessException {
		executor.execute(new TerminateContract( contractId ));
		
	}

	@Override
	public Optional<ContractDto> findContractById(String id)
			throws BusinessException {
		
		return executor.execute(new FindContractById( id ));
	}

	

	@Override
	public List<ContractSummaryDto> findAllContracts()
			throws BusinessException {
		
		return executor.execute(new FindAllContracts());
	}

	@Override
	public List<ContractSummaryDto> findContractsByMechanicDni(String mechanicDni) throws BusinessException {
		// TODO Auto-generated method stub
		 return executor.execute(new FindContractsByMechanic( mechanicDni));
	}

}
