package uo.ri.cws.application.service.contract.crud.command;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.util.assertion.ArgumentChecks;

public class DeleteContract implements Command<Void>{
	
	private String id;
	private ContractRepository repo=  Factory.repository.forContract();
	private MechanicRepository mrepo= Factory.repository.forMechanic();
	private PayrollRepository prepo=Factory.repository.forPayroll();
	
	public DeleteContract(String id) {
		
		ArgumentChecks.isNotNull(id, "ID cannot be null");
		ArgumentChecks.isNotEmpty(id, "The id cannot be empty");
		
		this.id=id;
		
	}

	@Override
	public Void execute() throws BusinessException {
		
		Optional<Contract> co=repo.findById(id);
		BusinessChecks.isFalse(co.isEmpty(),"The contract doesn t  exist");
		
		Contract c=co.get();
		
		Optional<Mechanic> om=mrepo.findById(c.getMechanic().get().getId());
		
		
		BusinessChecks.isTrue(om.get().getAssigned().isEmpty(),"The mechanic has workorders");
		
		List<Payroll> po=prepo.findByContract(id);
		
		BusinessChecks.isTrue(po.isEmpty(), "THE contrat has genrated payrools");
			
		repo.remove(c);
			
		
		
		
		return null;
	}

}
