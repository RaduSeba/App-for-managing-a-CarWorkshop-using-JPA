package uo.ri.cws.application.service.payroll.crud.command;

import java.time.LocalDate;
import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Contract.ContractState;
import uo.ri.cws.domain.Payroll;
import uo.ri.util.assertion.ArgumentChecks;

public class GeneratePayrollsDate implements Command<Void>  {
	
	private PayrollRepository repo= Factory.repository.forPayroll();
	private ContractRepository crepo=  Factory.repository.forContract();
	private LocalDate date;
	
	public GeneratePayrollsDate(LocalDate present) {
		
	
		
		this.date=present;
		
	}

	@Override
	public Void execute() throws BusinessException {
	
		if(date==null)
		{
			return null;
		}
		
		List<Contract> res =crepo.findAll();
		
		if(res.isEmpty())
		{
			return null;
		}
		
		
		
		for(Contract c : res) {
			
			if(c.getEndDate().isEmpty()&& c.getState().equals(ContractState.TERMINATED)) {
				
			}
			else if(c.getEndDate().isEmpty()) {
				repo.add(new Payroll(c,date));
			}
			else {
				if(date.getMonth()==c.getEndDate().get().getMonth())
				{
					repo.add(new Payroll(c,date));
				}
				
			}
			
			
			
			
			
		}
		
		
		
		return null;
	}

}
