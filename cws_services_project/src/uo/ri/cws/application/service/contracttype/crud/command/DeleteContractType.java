package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;

public class DeleteContractType  implements Command<Void>{

	private String n;
	private ContractTypeRepository repo= Factory.repository.forContractType();

	
	public DeleteContractType(String name) {
		ArgumentChecks.isNotEmpty(name,"The name cannot be empty");
		ArgumentChecks.isNotNull(name, "The name cannot be null");
		this.n=name;
	}

	@Override
	public Void execute() throws BusinessException {
	
		Optional<ContractType> co=repo.findByName(n);
		BusinessChecks.isFalse(co.isEmpty(),"The contract type doesn t  exist");
		
		ContractType c=co.get();
		
		repo.remove(c);
		
		return null;
	}

}
