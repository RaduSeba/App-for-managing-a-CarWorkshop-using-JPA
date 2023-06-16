package uo.ri.cws.application.service.payroll.crud.command;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryBLDto;
import uo.ri.cws.application.service.payroll.assembler.PayrollAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Payroll;
import uo.ri.util.assertion.ArgumentChecks;

public class FindPayroll implements Command<Optional<PayrollBLDto>> {
	
	private PayrollRepository repo= Factory.repository.forPayroll();
	private String id;
	
	public FindPayroll(String Id)
	{
		ArgumentChecks.isNotNull(Id, "ID cannot be null");
		ArgumentChecks.isNotEmpty(Id, "The id cannot be empty");
		ArgumentChecks.isNotBlank(Id, "The id cannot be empty");
		
		this.id=Id;
		
	}

	@Override
	public Optional<PayrollBLDto> execute() throws BusinessException {
		
		Optional<Payroll> o =repo.findById(id);
		
		if(o.isEmpty()) {
			return Optional.empty();
		}
		
		PayrollBLDto p = PayrollAssembler.toDto(o.get());
		
		
		
		return Optional.of(p);
			
	}

}
