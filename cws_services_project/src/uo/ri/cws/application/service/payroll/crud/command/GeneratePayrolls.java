package uo.ri.cws.application.service.payroll.crud.command;

import java.time.LocalDate;
import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Payroll;

public class GeneratePayrolls implements Command<Void>{

	private PayrollRepository repo= Factory.repository.forPayroll();
	private ContractRepository crepo=  Factory.repository.forContract();
	
	private LocalDate currentDate = LocalDate.now();

	
	@Override
	public Void execute() throws BusinessException {
		
		List<Contract> res =crepo.findAll();
		
		if(res.isEmpty())
		{
			return null;
		}
		
		for(Contract c : res) {
			
			repo.add(new Payroll(c,currentDate));
			
		}
		
		
		
		
		
		
		return null;
	}
	
	

}
