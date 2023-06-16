package uo.ri.cws.application.service.payroll.crud.command;

import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Payroll;

public class DeletePayrolls implements Command<Void> {
	
	
	private PayrollRepository repo= Factory.repository.forPayroll();


	@Override
	public Void execute() throws BusinessException {
		
		List<Payroll> p = repo.findCurrentMonthPayrolls();
		
		for(Payroll pr: p) {
			repo.remove(pr);
		}
		
		
		return null;
	}

}
