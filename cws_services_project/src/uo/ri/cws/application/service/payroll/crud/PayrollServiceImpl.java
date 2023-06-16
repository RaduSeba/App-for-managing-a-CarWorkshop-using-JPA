package uo.ri.cws.application.service.payroll.crud;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.crud.command.DeletePayrolls;
import uo.ri.cws.application.service.payroll.crud.command.DeletePayrollsForMechanic;
import uo.ri.cws.application.service.payroll.crud.command.FindAllPayrolls;
import uo.ri.cws.application.service.payroll.crud.command.FindPayroll;
import uo.ri.cws.application.service.payroll.crud.command.FindPayrollProfessional;
import uo.ri.cws.application.service.payroll.crud.command.FindPayrollforMechanic;
import uo.ri.cws.application.service.payroll.crud.command.GeneratePayrolls;
import uo.ri.cws.application.service.payroll.crud.command.GeneratePayrollsDate;
import uo.ri.cws.application.util.command.CommandExecutor;

public class PayrollServiceImpl implements PayrollService {
	
	private CommandExecutor executor = Factory.executor.forExecutor();

	public PayrollServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generatePayrolls() throws BusinessException {
		// TODO Auto-generated method stub
		
		executor.execute(new GeneratePayrolls());
		
	}

	@Override
	public void generatePayrolls(LocalDate present) throws BusinessException {
		// TODO Auto-generated method stub
		executor.execute(new GeneratePayrollsDate(present));
		
	}

	@Override
	public void deleteLastPayrollFor(String mechanicId)
			throws BusinessException {
		
		executor.execute(new DeletePayrollsForMechanic(mechanicId));
		
	}

	@Override
	public void deleteLastPayrolls() throws BusinessException {
		// TODO Auto-generated method stub
		
		executor.execute(new DeletePayrolls());
	}

	@Override
	public Optional<PayrollBLDto> getPayrollDetails(String id)
			throws BusinessException {
		// TODO Auto-generated method stub
		return executor.execute(new FindPayroll(id));
	}

	@Override
	public List<PayrollSummaryBLDto> getAllPayrolls() throws BusinessException {
		// TODO Auto-generated method stub
		return executor.execute(new FindAllPayrolls());
	}

	@Override
	public List<PayrollSummaryBLDto> getAllPayrollsForMechanic(String id)
			throws BusinessException {
		
		return executor.execute(new FindPayrollforMechanic(id));
	}

	@Override
	public List<PayrollSummaryBLDto> getAllPayrollsForProfessionalGroup(
			String name) throws BusinessException {
		// TODO Auto-generated method stub
		return executor.execute(new FindPayrollProfessional(name));
	}

}
