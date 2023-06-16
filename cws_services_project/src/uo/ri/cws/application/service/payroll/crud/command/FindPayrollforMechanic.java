package uo.ri.cws.application.service.payroll.crud.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryBLDto;
import uo.ri.cws.application.service.payroll.assembler.PayrollAssembler;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Payroll;
import uo.ri.util.assertion.ArgumentChecks;

public class FindPayrollforMechanic implements Command<List<PayrollSummaryBLDto>> {

	private String id;
	private PayrollRepository repo= Factory.repository.forPayroll();
	private ContractRepository crepo=  Factory.repository.forContract();
	private MechanicRepository mrepo= Factory.repository.forMechanic();
	
	public FindPayrollforMechanic(String Id) {
		
		ArgumentChecks.isNotNull(Id, "ID cannot be null");
		ArgumentChecks.isNotEmpty(Id, "The id cannot be empty");
		ArgumentChecks.isNotBlank(Id, "The id cannot be empty");
		
		this.id=Id;
	}
	@Override
	public List<PayrollSummaryBLDto> execute() throws BusinessException {
		
		List<Contract> res =crepo.findByMechanicId(id);
		BusinessChecks.isFalse(res.isEmpty(),"The mechanic id doesn t  exist");
		List<Payroll> li = repo.findByContract(res.get(0).getId());
		

		
		List<PayrollSummaryBLDto> li3 = new ArrayList<>();
		
		for(Payroll p: li)
		{
			li3.add(PayrollAssembler.toBlSummaryDto(p));
		}
		
		return li3;
		
	}

}
