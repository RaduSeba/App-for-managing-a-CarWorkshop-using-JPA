package uo.ri.cws.application.service.payroll;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.common.TestContext.Key;
import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.util.PayrollUtil;
import uo.ri.cws.application.service.util.sql.FindPayrollsByMechanicSqlUnitOfWork;

public class DeleteSteps {

    private String mechanicId;
    private PayrollService service = Factory.service.forPayrollService();

    private TestContext ctx;

    public DeleteSteps ( TestContext ctx ) {
	this.ctx = ctx;
    }

    @When("I try to delete with null argument")
    public void i_try_to_delete_with_null_argument ( ) {
	tryDeleteAndKeepException(null);
    }

    @When("I try to delete with wrong mechanic {string} argument")
    public void i_try_to_delete_with_argument ( String arg ) {
	tryDeleteAndKeepException(arg);
    }

    @When("I try to delete current month payroll for a non existent mechanid")
    public void i_try_to_delete_last_payroll_for_a_non_existent_mechanid ( ) {
	tryDeleteAndKeepException(UUID.randomUUID().toString());

    }

    @When("I delete the current month payroll for the mechanic")
    public void i_delete_the_last_payroll_for_the_mechanic ( )
	    throws BusinessException {
	mechanicId = ((MechanicDto) ctx.get(Key.MECHANIC)).id;
	service.deleteLastPayrollFor(mechanicId);
    }

    @Then("There is no payroll for this mechanic anymore")
    public void there_is_no_payroll_for_this_mechanic_anymore ( )
	    throws BusinessException {

	FindPayrollsByMechanicSqlUnitOfWork munit = new FindPayrollsByMechanicSqlUnitOfWork(
		mechanicId);
	munit.execute();
	List<PayrollBLDto> results = munit.get();

	assertTrue(results.isEmpty());
    }

    @Given("One payroll for this contract this month")
    public void one_payroll_for_this_contract ( ) {
	ContractDto c = (ContractDto) ctx.get(Key.INFORCE);
	PayrollUtil pu = new PayrollUtil();
	pu.unique().forContract(c.id).forDate(LocalDate.now()).register();
	ctx.put(Key.PAYROLL, pu.get());
    }

    @Then("Current month payroll for this mechanic is deleted and the rest remain")
    public void current_month_payroll_for_this_mechanic_is_deleted ( ) {

	List<PayrollBLDto> payrollsInDB = getPayrollsInDBAfterDeletingOrderedByDate();
	List<PayrollBLDto> payrollsBefore = getPayrollsInserterdOrderedByDate();
	Optional<PayrollBLDto> thisMonthAfterDel = payrollsInDB.stream()
		.filter(pr -> pr.date.equals(LocalDate.now())).findAny();
	payrollsBefore.remove(0);

	assertTrue(thisMonthAfterDel.isEmpty());
	assertTrue(payrollsBefore.size() == payrollsInDB.size());
	for ( int index = 0; index < payrollsInDB.size(); index++ )
	    assertTrue(PayrollUtil.match(payrollsBefore.get(index),
		    payrollsInDB.get(index)));
    }

    private List<PayrollBLDto> getPayrollsInserterdOrderedByDate ( ) {
	@SuppressWarnings("unchecked")
	List<PayrollBLDto> list = (List<PayrollBLDto>) ctx.get(Key.PAYROLLS);
	Collections.sort(list, new Comparator<PayrollBLDto>() {
	    @Override
	    public int compare ( PayrollBLDto p1, PayrollBLDto p2 ) {
		return p2.date.compareTo(p1.date);
	    }
	});
	return list;
    }

    private List<PayrollBLDto> getPayrollsInDBAfterDeletingOrderedByDate ( ) {
		FindPayrollsByMechanicSqlUnitOfWork unit = new FindPayrollsByMechanicSqlUnitOfWork(mechanicId);
	unit.execute();
	List<PayrollBLDto> results = unit.get();
	Collections.sort(results, new Comparator<PayrollBLDto>() {
	    @Override
	    public int compare ( PayrollBLDto p1, PayrollBLDto p2 ) {
		return p2.date.compareTo(p1.date);
	    }
	});
	return results;
    }

    private void tryDeleteAndKeepException ( String arg ) {
	try {
	    service.deleteLastPayrollFor(arg);
	    fail();
	} catch (BusinessException ex) {
	    ctx.setException(ex);
	} catch (IllegalArgumentException ex) {
	    ctx.setException(ex);
	}
    }

}
