package uo.ri.cws.application.service.contract.crud.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contract.ContractAssembler;
import uo.ri.cws.application.service.contract.ContractService.ContractSummaryDto;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;

public class FindContractsByMechanic implements Command<List<ContractSummaryDto>>{
	
	private String dni;
	private ContractRepository repo=  Factory.repository.forContract();
	private MechanicRepository mrepo= Factory.repository.forMechanic();
	
	private List<ContractSummaryDto> result = new ArrayList<>();

	public FindContractsByMechanic(String mechanicdni ) {
		
		ArgumentChecks.isNotNull(mechanicdni, "ID cannot be null");
		ArgumentChecks.isNotEmpty(mechanicdni, "The id cannot be empty");
		
		this.dni=mechanicdni;
	}

	@Override
	public List<ContractSummaryDto> execute() throws BusinessException {
		
		Optional<Mechanic> om=mrepo.findByDni(dni);
		
		if(om.isEmpty())
		{
			return result;
		}
		
	
		
		String id= om.get().getId();
		
		List<Contract> res =repo.findByMechanicId(id);
		
		
		
		//return DtoAssembler.toContractSummaryDtoList(res);
		return ContractAssembler.toDtoList(res);
	}

}
