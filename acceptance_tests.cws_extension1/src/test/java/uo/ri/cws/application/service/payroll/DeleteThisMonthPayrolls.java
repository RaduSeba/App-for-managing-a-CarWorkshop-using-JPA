package uo.ri.cws.application.service.payroll;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import uo.ri.cws.application.service.BusinessException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.util.sql.FindAllPayrollsSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindPayrollsThisMonthSqlUnitOfWork;

public class DeleteThisMonthPayrolls {

	private List<PayrollBLDto> before = null, after = null;
	private PayrollService service = Factory.service.forPayrollService();

	@When("I delete this month payrolls")
	public void i_delete_this_month_payrolls() throws BusinessException {
		FindAllPayrollsSqlUnitOfWork unit = new FindAllPayrollsSqlUnitOfWork(); 
		unit.execute();
		before = unit.get();

		service.deleteLastPayrolls();
	}

	@When("I delete this month payrolls twice")
	public void i_delete_this_month_payrolls_twice() throws BusinessException {
		service.deleteLastPayrolls();
		
		FindPayrollsThisMonthSqlUnitOfWork unit = new FindPayrollsThisMonthSqlUnitOfWork(
				LocalDate.now());
		unit.execute();
		before = unit.get();
		service.deleteLastPayrolls();
	}

	@Then("There is no payroll left")
	public void there_is_no_payroll_left() {
		FindPayrollsThisMonthSqlUnitOfWork unit = new FindPayrollsThisMonthSqlUnitOfWork(
				LocalDate.now());
		unit.execute();
		List<PayrollBLDto> list = unit.get();

		assertTrue(list.isEmpty());
	}

	@Then("This month payroll is deleted")
	public void this_month_payroll_is_deleted() {
		FindAllPayrollsSqlUnitOfWork unit = new FindAllPayrollsSqlUnitOfWork (); 
		unit.execute();
		after = unit.get();
		assertTrue(before.size() == after.size() + 1);
		assertTrue(noPayrollThisMonth(after));
	}

	private boolean noPayrollThisMonth(List<PayrollBLDto> arg) {
		int thisMonth = LocalDate.now().getMonthValue();
		int thisYear = LocalDate.now().getYear();
		List<PayrollBLDto> list = arg.stream().filter(p -> p.date.getMonthValue() == thisMonth)
				.filter(p -> p.date.getYear() == thisYear)
				.collect(Collectors.toList());
		return (list.isEmpty());
	}

	@Then("No payroll is deleted")
	public void no_payroll_is_deleted() {
		FindPayrollsThisMonthSqlUnitOfWork unit = new FindPayrollsThisMonthSqlUnitOfWork(
				LocalDate.now());
		unit.execute();
		after = unit.get();
		assertTrue(before.size() == after.size() );
		assertTrue(noPayrollThisMonth(after));
	}

}
