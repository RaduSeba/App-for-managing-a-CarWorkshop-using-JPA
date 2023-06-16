package uo.ri.cws.application.service.payroll;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.common.LocalDateConverter;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.common.TestContext.Key;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.ClientUtil;
import uo.ri.cws.application.service.util.ContractTypeUtil;
import uo.ri.cws.application.service.util.ContractUtil;
import uo.ri.cws.application.service.util.InterventionUtil;
import uo.ri.cws.application.service.util.MechanicUtil;
import uo.ri.cws.application.service.util.ProfessionalGroupUtil;
import uo.ri.cws.application.service.util.VehicleUtil;
import uo.ri.cws.application.service.util.WorkOrderUtil;
import uo.ri.cws.application.service.util.sql.FindPayrollsThisMonthSqlUnitOfWork;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;

public class GenerateSteps {
    private int generatedPayslips = 0;
    private PayrollService service = Factory.service.forPayrollService();
    private LocalDate currentDate = null;
    private TestContext ctx;

    public GenerateSteps ( TestContext ctx ) {
	this.ctx = ctx;

    }

    @When("I generate payrolls")
    public void i_generate_payrolls ( ) throws BusinessException {
	service.generatePayrolls(currentDate);

    }

    @Then("zero payrolls are generated")
    public void zero_payrolls_are_generated ( ) {
	List<PayrollBLDto> found = findGeneratedPayrolls();
	this.generatedPayslips = found.size();
	assertTrue(this.generatedPayslips == 0);

    }

    @Given("today is {string}")
    public void set_today ( String d ) {
	currentDate = LocalDateConverter.convert(d);

    }

    @Given("today is")
    public void set_today ( DataTable t ) {

	String d = t.asMaps().get(0).get("present");
	currentDate = LocalDateConverter.convert(d);

    }

    @Then("one payroll is generated with {double}, {double}, {double}, {double}, {double}, {double}")
    public void one_payroll_is_generated_with ( Double moneyMonth, Double bonus,
	    Double productivity, Double tri, Double tax, Double nic ) {
	List<PayrollBLDto> founds = findGeneratedPayrolls();
	this.generatedPayslips = founds.size();
	assertTrue(this.generatedPayslips == 1);
	PayrollBLDto found = founds.get(0);
	assertTrue(Double.compare(found.monthlyWage, moneyMonth) == 0);
	assertTrue(Double.compare(found.bonus, bonus) == 0);
	assertTrue(Double.compare(found.productivityBonus, productivity) == 0);
	assertTrue(Double.compare(found.trienniumPayment, tri) == 0);
	assertTrue(Double.compare(found.incomeTax, tax) == 0);
	assertTrue(Double.compare(found.nic, nic) == 0);
	assertTrue(found.version == 1L);
    }

    @Then("{int} payrolls are generated with {double}, {double}, {double}, {double}, {double}, {double}")
    public void payrolls_are_generated_with ( Integer howMany,
	    Double moneyMonth, Double bonus, Double productivity, Double tri,
	    Double tax, Double nic ) {
	List<PayrollBLDto> founds = findGeneratedPayrolls();
	this.generatedPayslips = founds.size();
	assertTrue(this.generatedPayslips == howMany);
	if ( howMany > 0 ) {
	    PayrollBLDto found = founds.get(0);
	    assertTrue(Double.compare(found.monthlyWage, moneyMonth) == 0);
	    assertTrue(Double.compare(found.bonus, bonus) == 0);
	    assertTrue(
		    Double.compare(found.productivityBonus, productivity) == 0);
	    assertTrue(Double.compare(found.trienniumPayment, tri) == 0);
	    assertTrue(Double.compare(found.incomeTax, tax) == 0);
	    assertTrue(Double.compare(found.nic, nic) == 0);
	    assertTrue(found.version == 1L);

	}

    }

    private List<PayrollBLDto> findGeneratedPayrolls ( ) {
	if ( currentDate == null )
	    currentDate = LocalDate.now();
	FindPayrollsThisMonthSqlUnitOfWork unit = new FindPayrollsThisMonthSqlUnitOfWork(
		currentDate);
	unit.execute();

	return unit.get();
    }

    @Then("two payrolls are generated")
    public void two_payrolls_are_generated ( ) {
	List<PayrollBLDto> found = findGeneratedPayrolls();
	this.generatedPayslips = found.size();
	assertTrue(this.generatedPayslips == 2);

    }

    @Given("Exactly {int} invoiced workorders for the mechanic with amount {double}")
    public void workorders_for_the_mechanic_with_date_and_amount (
	    Integer howmany, Double amount ) {
	MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);
	LocalDate openWorkorder = currentDate;
	WorkOrderDto workOrderDto = null;
	/*
	 * Add client, vehicle, workorder and link workorder to mechanic
	 */
	ClientDto client = new ClientUtil().register().get();
	VehicleDto vehicle = new VehicleUtil().withOwner(client.id).register()
		.get();
	for ( int i = 1; i <= howmany; i++ ) {
	    LocalDateTime ldt = openWorkorder.withDayOfMonth(i).atStartOfDay();
	    workOrderDto = new WorkOrderUtil().forMechanic(m.id)
		    .forVehicle(vehicle.id).withDate(ldt).withAmount(amount)
		    .withState("INVOICED").register().get();
	    new InterventionUtil().withDate(ldt).withMechanic(m.id)
		    .withWorkorder(workOrderDto.id).register();

	}
    }

    @Given("A contract in force for a mechanic with {string} start date and {double} base salary")
    public void a_contract_in_force_for_the_mechanic_with ( String date,
	    Double salary ) throws BusinessException {
	MechanicDto m = new MechanicUtil().unique().register().get();
	ctx.put(Key.MECHANIC, m);

	ContractTypeDto ct = new ContractTypeUtil()
		.findContractType("PERMANENT").get();
	ProfessionalGroupBLDto pg = new ProfessionalGroupUtil()
		.findProfessionalGroup("I").get();
	LocalDate localDate = LocalDateConverter.convert(date);

	ContractUtil cUtil = new ContractUtil().unique().forMechanic(m)
		.withType(ct).withGroup(pg).withStartDate(localDate)
		.withAnnualWage(salary).register();

	ctx.put(Key.INFORCE, cUtil.get());

    }

    @Given("A contract terminated for a mechanic, with {string} start date, {string} end date, {double} base salary")
    public void a_contract_terminated_for_the_mechanic_with_start_date_end_date_base_salary (
	    String startDate, String endDate, Double salary )
	    throws BusinessException {
	MechanicDto m = new MechanicUtil().unique().register().get();
	ctx.put(Key.MECHANIC, m);
	ContractTypeDto ct = new ContractTypeUtil()
		.findContractType("PERMANENT").get();
	ProfessionalGroupBLDto pg = new ProfessionalGroupUtil()
		.findProfessionalGroup("I").get();

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	LocalDate first = LocalDate.parse(startDate, formatter);
	LocalDate last = LocalDate.parse(endDate, formatter)
		.with(TemporalAdjusters.lastDayOfMonth());

	ContractUtil cUtil = new ContractUtil().unique().forMechanic(m)
		.withType(ct).withGroup(pg).withState("TERMINATED")
		.withStartDate(first).withEndDate(last).withAnnualWage(salary)
		.register();

	ctx.put(Key.TERMINATED, cUtil.get());
    }

    @Given("A contract for the mechanic terminated long ago with {double} base salary")
    public void a_contract_terminated_long_ago_with_base_salary (
	    Double salary ) throws BusinessException {
	MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);
	ContractTypeDto ct = (ContractTypeDto) ctx.get(Key.CONTRACTTYPE);
	ProfessionalGroupBLDto pg = (ProfessionalGroupBLDto) ctx
		.get(Key.PROFESSIONALGROUP);

	LocalDate end = this.currentDate.minusMonths(6);
	LocalDate start = end.minusMonths(6);
	ContractUtil cUtil = new ContractUtil().unique().forMechanic(m)
		.withType(ct).withGroup(pg).withState("TERMINATED")
		.withStartDate(start).withEndDate(end).withAnnualWage(salary)
		.register();

	ctx.put(Key.TERMINATED, cUtil.get());

    }

    @Given("a contract type PERMANENT")
    public void a_contract_type_permanent ( ) {
	ContractTypeUtil util = new ContractTypeUtil()
		.findContractType("PERMANENT");
	ContractTypeDto ct_dto = util.get();
	ctx.put(Key.CONTRACTTYPE, ct_dto);

    }

    @Given("a professional group I")
    public void a_prof_group_1 ( ) {
	ProfessionalGroupUtil util = new ProfessionalGroupUtil()
		.findProfessionalGroup("I");
	ProfessionalGroupBLDto pg_dto = util.get();
	ctx.put(Key.PROFESSIONALGROUP, pg_dto);

    }

}
