package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Contract.ContractState;
import uo.ri.util.assertion.ArgumentChecks;

public class UpdateContract implements Command<Void>{
	
	private ContractDto cdto;
	private ContractRepository repo=  Factory.repository.forContract();

	public UpdateContract(ContractDto dto) {
		
		ArgumentChecks.isNotNull(dto, "THe contract is null");
		ArgumentChecks.isNotEmpty(dto.id, "The id cannot be null");
		ArgumentChecks.isNotBlank(dto.id,"The id cannot be blank");
		ArgumentChecks.isNotNull(dto.contractTypeName, "The contract type cannot be null");
		ArgumentChecks.isNotNull(dto.professionalGroupName, "The contract type cannot be null");
		ArgumentChecks.isTrue(dto.annualBaseWage>0,"The wage is wrong");
		
		this.cdto=dto;
		
	}

	@Override
	public Void execute() throws BusinessException {
		
		
		if(cdto.endDate!=null)
		{
			BusinessChecks.isFalse(cdto.startDate.isAfter(cdto.endDate),"the dates are wrong");
		}
		
		Optional<Contract> co=repo.findById(cdto.id);
		
		BusinessChecks.exists(co,"The contract doesn t exist");
		
		Contract c=co.get();
		
		
		BusinessChecks.isTrue(cdto.version==c.getVersion(),"You are working on a stale data");
		BusinessChecks.isFalse(c.getState().equals(ContractState.TERMINATED), "THe contract is terminated");

		c.setAnnualWage(cdto.annualBaseWage);
		c.setEndDate(cdto.endDate);
		
		return null;
	}

}
