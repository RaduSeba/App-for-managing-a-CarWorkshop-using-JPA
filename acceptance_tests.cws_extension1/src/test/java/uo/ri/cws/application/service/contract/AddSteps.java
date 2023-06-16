package uo.ri.cws.application.service.contract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.common.LocalDateConverter;
import uo.ri.cws.application.service.common.NIFUtil;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.common.TestContext.Key;
import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.ContractTypeUtil;
import uo.ri.cws.application.service.util.ContractUtil;
import uo.ri.cws.application.service.util.MechanicUtil;
import uo.ri.cws.application.service.util.PayrollUtil;
import uo.ri.cws.application.service.util.ProfessionalGroupUtil;
import uo.ri.cws.application.service.util.sql.FindContractByIdSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindContractTypeByIdSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindPayrollsByContractLastYearSqlUnitOfWork;
import uo.ri.cws.domain.Contract.ContractState;
import uo.ri.util.math.Round;

public class AddSteps {

	private static final long DAYSINAYEAR = 365;
	private ContractService service = Factory.service.forContractService();
	private TestContext ctx;
	private ContractUtil cUtil = new ContractUtil();
	private PayrollUtil pUtil = new PayrollUtil();

	private ContractDto inForce;
	private ContractDto terminated;

	public AddSteps(TestContext ctx) {
		this.ctx = ctx;
		inForce = (ContractDto) ctx.get(Key.INFORCE);
		terminated = (ContractDto) ctx.get(Key.TERMINATED);
	}

	@When("I hire the mechanic with this data")
	// start date = first day next month
	// end date = null
	public void i_register_this_contract_for_this_mechanic(DataTable dataTable)
			throws BusinessException {
		Map<String, String> row = dataTable.asMaps().get(0);

		MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);

		ContractDto previous = new ContractUtil()
				.findContractInForceForMechanic(m.id).get();
		
		if (previous != null) { // there were a previous contract in force
			FindContractByIdSqlUnitOfWork x = new FindContractByIdSqlUnitOfWork(
					previous.id);
			x.execute();
			terminated = x.get().get();
			ctx.put(Key.TERMINATED, terminated);
		}

		String type = row.get("type");
		String group = row.get("group");
		String p = row.get("pay");

		ContractTypeDto ct = new ContractTypeUtil().findContractType(type).get();
		ProfessionalGroupBLDto pg = new ProfessionalGroupUtil()
				.findProfessionalGroup(group).get();
		
		double pay = (p == null || p.isBlank()) 
					? 1000.0
					: Double.parseDouble(p);
		pay = Round.twoCents(pay);
		
		ContractDto contractDto = new ContractUtil()
				.unique()
				.forMechanic(m)
				.withType(ct)
				.withGroup(pg)
				.withAnnualWage(pay)
				.get();

		contractDto = service.addContract(contractDto);
		ctx.put(Key.INFORCE, contractDto);
	}

	@Given("a contract terminated")
	public void a_contract_terminated_for_the_mechanic()
			throws BusinessException {
		MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);
		ContractTypeDto ct = (ContractTypeDto) ctx.get(Key.CONTRACTTYPE);
		ProfessionalGroupBLDto pg = (ProfessionalGroupBLDto) ctx
				.get(Key.PROFESSIONALGROUP);

		add_a_contract_terminated_for_mechanic(m, ct, pg);
	}

	private void add_a_contract_terminated_for_mechanic(MechanicDto m,
			ContractTypeDto ct, ProfessionalGroupBLDto pg)
			throws BusinessException {
		LocalDate startDate = LocalDate.now().minusYears(1);
		LocalDate endDate = startDate.plusMonths(6);
		terminated = new ContractUtil().unique().forMechanic(m).withType(ct)
				.withGroup(pg).withState("TERMINATED").withStartDate(startDate)
				.withEndDate(endDate).register().get();
		ctx.put(Key.TERMINATED, terminated);
	}

	@Then("contract version is {int}")
	public void contract_version_is(Integer n) {
		assertTrue(inForce.version == n);

	}

	@Then("previous contract is terminated")
	public void previous_contract_is_terminated()
			throws BusinessException {
		terminated = (ContractDto) ctx.get(Key.TERMINATED);

		terminated = cUtil.findContractById(terminated.id).get();

		assertEquals( ContractState.TERMINATED, terminated.state );
		assertEquals( LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()), 
				terminated.endDate);
	}

	@Then("there is a contract in force for the mechanic with the following data")
	public void there_is_a_contract_in_force_for_the_mechanic(
			DataTable dataTable) throws BusinessException {
		MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);
		inForce = cUtil.findContractInForceForMechanic(m.id).get();
		LocalDate initDate, endDate = null;

		Map<String, String> row = dataTable.asMaps().get(0);
		String d = row.get("startDate");
		if (d != null)
			initDate = LocalDateConverter.convert(d);
		else
			initDate = LocalDate.now()
					.with(TemporalAdjusters.firstDayOfNextMonth());

		if (inForce.contractTypeName.equals("FIXED_TERM")) {
			d = row.get("endDate");
			endDate = LocalDateConverter.convert(d);
		}

		assertNotNull(inForce);
		assertTrue(inForce.dni.equals(row.get("dni")));
		assertTrue(inForce.professionalGroupName.equals(row.get("group")));
		assertTrue(inForce.contractTypeName.equals(row.get("type")));
		assertTrue(inForce.state.equals(ContractState.IN_FORCE));
		assertTrue(inForce.startDate.equals(initDate));
		if (inForce.contractTypeName.equals("FIXED_TERM"))
			assertTrue(inForce.endDate.equals(endDate));
		double sett = Double.parseDouble(row.get("settlement"));
		sett = Round.twoCents(sett);
		double pay = Double.parseDouble(row.get("pay"));
		pay = Round.twoCents(pay);
		assertTrue(Math.abs(inForce.settlement - sett) < Double.MIN_NORMAL);
		assertTrue(Math.abs(inForce.annualBaseWage - pay) < Double.MIN_NORMAL);

	}

	@Then("there is no settlement")
	public void there_is_no_settlement() {
		assertTrue(inForce.settlement == 0.0);
	}

	@Then("start date is first day next month")
	public void start_date_is_first_day_next_month() {
		LocalDate firstDayofNextMonth = LocalDate.now()
				.with(TemporalAdjusters.firstDayOfNextMonth());
		assertTrue(inForce.startDate.equals(firstDayofNextMonth));
	}

	/*
	 * All payrolls are generated, this month and before
	 */
	@Given("several payrolls")
	public void several_payrolls() {
		List<PayrollBLDto> payrolls = new ArrayList<>();
		LocalDate fromDate = LocalDate.now();
		LocalDate toDate = inForce.startDate;
		while (toDate.isBefore(fromDate) || toDate.isEqual(fromDate)) {
			payrolls.add(generateAMonthPayroll(inForce, toDate));
			toDate = toDate.plusMonths(1);
		}
		ctx.put(Key.PAYROLLS, payrolls);
	}

	private PayrollBLDto generateAMonthPayroll(ContractDto c,
			LocalDate endDate) {
		double bonus = 0.0;
		if (endDate.getMonthValue() == 6 || endDate.getMonthValue() == 12) {
			bonus = 1000.0;
		}
		pUtil.unique().forContract(c.id).forDate(endDate).forMonthlyWage(1000.0)
				.forBonus(bonus).forProductivityBonus(10.0)
				.forTrienniumPayment(15.0).forIncomeTax(100.0).forNIC(50.0);
		pUtil.register();
		return pUtil.get();
	}

	@Given("several payrolls this month only")
	public void several_payrolls_this_month_only() {
		ContractDto c = inForce;
		LocalDate beginDate = LocalDate.now();
		LocalDate endDate = beginDate;
		generateAMonthPayroll(c, endDate);
	}

	@Given("several payrolls before this month")
	public void several_old_payrolls() {
		ContractDto c = inForce;
		LocalDate beginDate = c.startDate;
		LocalDate endDate = c.startDate.plusMonths(1);
		while (!endDate.isBefore(beginDate)) {
			generateAMonthPayroll(c, endDate);
			endDate = endDate.minusMonths(1);
		}

	}

	@Then("previous contract settlement is calculated")
	public void settlement_is_calculated() {
		double settlement = calculateSettlement(terminated) * 100;
		settlement = Round.twoCents(settlement);

		assertTrue(terminated.settlement == settlement);

	}

	@When("I try to add a contract for a non existing mechanic")
	public void i_try_to_add_a_contract_for_a_non_existing_mechanic() {
		MechanicDto m = new MechanicUtil().unique().get();
		ContractTypeDto ct = new ContractTypeUtil()
				.findContractType("PERMANENT").get();
		ProfessionalGroupBLDto pg = new ProfessionalGroupUtil()
				.findProfessionalGroup("I").get();

		ContractDto c = cUtil.unique().forMechanic(m).withType(ct).withGroup(pg)
				.get();
		tryAddAndKeepException(c);
	}

	@When("I try to add a contract for a non existing contract type")
	public void i_try_to_add_a_contract_for_a_non_existing_contract_type() {
		MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);
		ContractTypeDto ct = new ContractTypeUtil().unique().get();
		ProfessionalGroupBLDto pg = new ProfessionalGroupUtil()
				.findProfessionalGroup("I").get();

		ContractDto c = cUtil.unique().forMechanic(m).withType(ct).withGroup(pg)
				.get();
		tryAddAndKeepException(c);
	}

	@When("I try to add a contract for a non existing professional group")
	public void i_try_to_add_a_contract_for_a_non_existing_professional_group() {
		MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);
		ContractTypeDto ct = new ContractTypeUtil()
				.findContractType("PERMANENT").get();
		ProfessionalGroupBLDto pg = new ProfessionalGroupUtil().unique();

		ContractDto c = cUtil.unique().forMechanic(m).withType(ct).withGroup(pg)
				.get();
		tryAddAndKeepException(c);
	}

	@When("I try to add a contract with end date not later than start date")
	public void i_try_to_add_a_contract_with_start_date_not_later_than_today() {
		MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);
		ContractTypeDto ct = new ContractTypeUtil()
				.findContractType("FIXED_TERM").get();
		ProfessionalGroupBLDto pg = new ProfessionalGroupUtil()
				.findProfessionalGroup("I").get();
		ContractDto c = cUtil.unique().forMechanic(m).withGroup(pg).withType(ct)
				.get();
		c.endDate = c.startDate;

		tryAddAndKeepException(c);
	}

	@When("I try to add a null argument")
	public void i_try_to_add_a_null_argument() {
		ContractDto c = null;
		tryAddAndKeepException(c);
	}

	@When("I try to add a contract with null dni")
	public void i_try_to_add_a_contract_with_null_mechanic_id() {

		ContractDto c = cUtil.unique().get();
		c.dni = null;

		tryAddAndKeepException(c);
	}

	@When("I try to add a contract with null contract type id")
	public void i_try_to_add_a_contract_with_null_contract_type_id() {

		ContractDto c = cUtil.unique().get();
		c.dni = NIFUtil.generateRandomNIF();
		c.contractTypeName = null;
		tryAddAndKeepException(c);
	}

	@When("I try to add a contract with null professional group id")
	public void i_try_to_add_a_contract_with_null_professional_group_id() {

		ContractDto c = cUtil.unique().get();
		c.dni = NIFUtil.generateRandomNIF();
		c.contractTypeName = null;
		tryAddAndKeepException(c);
	}

	@When("I try to add a contract with wrong dni {string}")
	public void i_try_to_add_a_contract_with_dni(String mechdni) {

		ContractDto c = cUtil.unique().get();
		c.dni = mechdni;
		tryAddAndKeepException(c);
	}

	/*
	 * 
	 * When I try to add a contract with wrong fields <type> <profGroup>
	 * <annualWage>
	 */

	@When("I try to add a contract with wrong fields {string} {string} {double}")
	public void i_try_to_add_a_contract_wrong(String type, String group,
			Double wage) {
		ContractDto c = cUtil.unique().get();

		c.contractTypeName = type;
		c.professionalGroupName = group;
		c.annualBaseWage = wage;
		tryAddAndKeepException(c);
	}

	@When("I try to add a contract with null end date for FIXED_TERM contract type")
	public void i_try_to_add_a_contract_with_null_end_date() {
		ContractDto c = cUtil.unique().get();
		c.contractTypeName = "FIXED_TERM";
		c.endDate = null;
		tryAddAndKeepException(c);
	}

	private void tryAddAndKeepException(ContractDto c) {
		try {
			service.addContract(c);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

	private double calculateSettlement(ContractDto arg) {
		long days = arg.startDate.until(arg.endDate, ChronoUnit.DAYS);
		int numberOfEntireYearsWorked = (int) (days / DAYSINAYEAR);
		Double settlement = 0.0;
		if (numberOfEntireYearsWorked > 0) {
			double dailyGrossWage = calculateDailyGrossWageLastYear(arg);
			dailyGrossWage = Round.twoCents(dailyGrossWage);
			double compensationDays = findCompensationDays(arg);
			settlement = numberOfEntireYearsWorked * dailyGrossWage
					* compensationDays;
			settlement = Round.twoCents(dailyGrossWage);
		}
		return settlement;
	}

	private double calculateDailyGrossWageLastYear(ContractDto existing) {
		FindPayrollsByContractLastYearSqlUnitOfWork unit = new FindPayrollsByContractLastYearSqlUnitOfWork(
				existing.id);
		unit.execute();
		List<PayrollBLDto> lastYear = unit.get();

		double monthlyEarnings = lastYear
				.stream().map(pr -> pr.bonus + pr.monthlyWage
						+ pr.productivityBonus + pr.trienniumPayment)
				.reduce(0.0, Double::sum);
//	monthlyEarnings = trunc2Dec(monthlyEarnings);
		return monthlyEarnings / DAYSINAYEAR;
	}

	private double findCompensationDays(ContractDto arg) {
		FindContractTypeByIdSqlUnitOfWork unit = new FindContractTypeByIdSqlUnitOfWork(
				arg.contractTypeName);
		unit.execute();
		return unit.get().get().compensationDays;

	}
}
