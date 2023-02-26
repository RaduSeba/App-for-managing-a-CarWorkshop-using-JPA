package uo.ri.cws.application.service.contract.crud.command;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;

public class TerminateContract implements Command<Void> {
	
	private String id;
	private ContractRepository repo=  Factory.repository.forContract();
	private MechanicRepository mrepo= Factory.repository.forMechanic();

	public TerminateContract(String contractId) {
		
		ArgumentChecks.isNotNull(id, "ID cannot be null");
		ArgumentChecks.isNotEmpty(id, "The id cannot be empty");
		
		this.id=contractId;
		
	}
	
	
	public static LocalDate getEndOfNextMonth() { return
			LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()); }
	
	

	@Override
	public Void execute() throws BusinessException {
	
		Optional<Contract> co=repo.findById(id);
		BusinessChecks.isFalse(co.isEmpty(),"The contract doesn t  exist");
		
		Contract c=co.get();
		
		BusinessChecks.isFalse(c.getState().equals("TERMINATED"), "THe contract is already terminated");
		
		Optional<Mechanic> om=mrepo.findById(c.getMechanic().get().getId());
		
		
		BusinessChecks.isTrue(om.get().getAssigned().isEmpty(),"The mechanic has workorders");
		
		//c.setEndDate(getEndOfNextMonth());
		
		c.terminate();
		
		
		
		
		
		
		return null;
	}

}
