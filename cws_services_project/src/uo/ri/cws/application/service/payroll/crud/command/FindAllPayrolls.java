package uo.ri.cws.application.service.payroll.crud.command;

import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryBLDto;
import uo.ri.cws.application.service.payroll.assembler.PayrollAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Payroll;

public class FindAllPayrolls implements  Command<List<PayrollSummaryBLDto>> {
	
	private PayrollRepository repo= Factory.repository.forPayroll();

	@Override
	public List<PayrollSummaryBLDto> execute() throws BusinessException {
		
		List<Payroll> li= repo.findAll();
		
		
	
		
		List<PayrollSummaryBLDto> li3 = new ArrayList<>();
		
		for(Payroll p: li)
		{
			li3.add(PayrollAssembler.toBlSummaryDto(p));
		}
		
		return li3;
	}

}
