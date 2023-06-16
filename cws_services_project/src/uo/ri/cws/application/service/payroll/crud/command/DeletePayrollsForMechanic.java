package uo.ri.cws.application.service.payroll.crud.command;

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
import uo.ri.cws.domain.Payroll;
import uo.ri.util.assertion.ArgumentChecks;

public class DeletePayrollsForMechanic implements Command<Void> {
	
	private String id;
	private PayrollRepository repo= Factory.repository.forPayroll();
	private ContractRepository crepo=  Factory.repository.forContract();
	private MechanicRepository mrepo= Factory.repository.forMechanic();

	public DeletePayrollsForMechanic(String mechanicId)
	{

		ArgumentChecks.isNotNull(mechanicId, "ID cannot be null");
		ArgumentChecks.isNotEmpty(mechanicId, "The id cannot be empty");
		ArgumentChecks.isNotBlank(mechanicId, "The id cannot be empty");
		
		this.id=mechanicId;
		
	}
	
	
	@Override
	public Void execute() throws BusinessException {
		
		
		
		List<Contract> res =crepo.findByMechanicId(id);
		BusinessChecks.isFalse(res.isEmpty(),"The mechanic id doesn t  exist");
		Optional<Payroll> o = repo.findCurrentMonthByContractId(res.get(0).getId());
		
		if(o.isEmpty())
		{
			return null;
		}
		
		Payroll p = o.get();
		
		repo.remove(p);
		
		
		
		
		
		return null;
	}

}
