package uo.ri.cws.application.service.payroll;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryBLDto;
import uo.ri.cws.application.service.util.ContractUtil;
import uo.ri.cws.application.service.util.PayrollUtil;
import uo.ri.util.math.Round;

public class FindPayrollsSteps {

	private PayrollService service = Factory.service.forPayrollService();
	private List<PayrollBLDto> list = new ArrayList<>();
	private List<PayrollSummaryBLDto> listSummary = new ArrayList<>();

	private Optional<PayrollBLDto> optionalPayroll = Optional.empty();
	private PayrollBLDto existentPayroll = null;

	private TestContext ctx;

	public FindPayrollsSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@When("I try to find payroll with null argument")
	public void i_try_to_find_payroll_with_null_argument() {
		tryGetPayrollDetailsAndKeepException(null);

	}

	@When("I try to find payroll with {string} argument")
	public void i_try_to_find_payroll_with_argument(String id) {
		tryGetPayrollDetailsAndKeepException(id);

	}

	@When("I find a payroll with a non existent id")
	public void i_find_a_payroll_with_a_non_existent_id()
			throws BusinessException {
		optionalPayroll = service
				.getPayrollDetails(UUID.randomUUID().toString());
	}

	@Then("Returns empty")
	public void returns_empty() {
		assertTrue(optionalPayroll.isEmpty());
	}

	@When("I search a payroll by id")
	public void i_find_a_payroll() throws BusinessException {
		this.existentPayroll = (PayrollBLDto) ctx.get(Key.PAYROLL);

		optionalPayroll = service.getPayrollDetails(existentPayroll.id);

	}

	@Then("This payroll is returned")
	public void this_payroll_is_returned() {

		assertTrue(optionalPayroll.isPresent());
		assertTrue(PayrollUtil.match(existentPayroll, optionalPayroll.get()));
		double net = existentPayroll.bonus + existentPayroll.monthlyWage
				+ existentPayroll.productivityBonus
				+ existentPayroll.trienniumPayment - existentPayroll.incomeTax
				- existentPayroll.nic;
		assertTrue(Math
				.abs(optionalPayroll.get().netWage - net) < Double.MIN_NORMAL);
	}

	@When("I search payrolls")
	public void i_search_payrolls() throws BusinessException {
		listSummary = service.getAllPayrolls();
	}

	/*
	 * All payrolls are generated, this month and before for all the mech
	 */
	@Given("several payrolls for all the contracts")
	public void several_payrolls() {
		List<ContractDto> allInForce = new ContractUtil()
				.findAllContractsInForce()
				.getList();
		List<PayrollBLDto> payrolls = generateAllPayrolls(allInForce);
		ctx.put(Key.PAYROLLS, payrolls);
	}

	private List<PayrollBLDto> generateAllPayrolls(
			List<ContractDto> allInForce) {
		List<PayrollBLDto> payrolls = new ArrayList<>();
		for (ContractDto c : allInForce) {
			payrolls.addAll(generateAllPayrolls(c));
		}
		return payrolls;
	}

	private List<PayrollBLDto> generateAllPayrolls(ContractDto c) {
		List<PayrollBLDto> payrolls = new ArrayList<>();

		LocalDate today = LocalDate.now();
		LocalDate payrollDate = c.startDate;
		while (payrollDate.isBefore(today) || payrollDate.isEqual(today)) {
			PayrollBLDto p = generateAMonthPayroll(c, payrollDate);
			payrolls.add( p );
			payrollDate = payrollDate.plusMonths(1);
		}

		return payrolls;
	}

	private PayrollBLDto generateAMonthPayroll(ContractDto c,
			LocalDate endDate) {
		double bonus = 0.0;
		double monthPay = c.annualBaseWage / 14.0;
		if (endDate.getMonthValue() == 6 || endDate.getMonthValue() == 12) {
			bonus = monthPay;
		}
		long yearsBetween = ChronoUnit.YEARS.between(endDate, LocalDate.now());
		double tri = 0.0;
		if (yearsBetween >= 3) {
			tri = (yearsBetween * 46.74);
		}
		
		return new PayrollUtil()
				.unique()
				.forContract(c.id).forDate(endDate)
				.forMonthlyWage(Round.twoCents(monthPay))
				.forBonus(Round.twoCents(bonus)).forProductivityBonus(0.0)
				.forTrienniumPayment(tri)
				.forIncomeTax(Round.twoCents((monthPay + bonus + tri) * 0.24))
				.forNIC(70.0)
				.register()
				.get();
	}

	@Then("Result is empty")
	public void result_is_empty() {
		assertTrue(list.isEmpty());
	}

	@Then("All payrolls summary are returned")
	public void all_payrolls_summary_are_returned() {
		@SuppressWarnings("unchecked")
		List<PayrollSummaryBLDto> aux = summarize(
				(List<PayrollBLDto>) ctx.get(Key.PAYROLLS));
		assertTrue(aux.size() == listSummary.size());

		assertTrue(PayrollUtil.matchPayrollSummaries(aux, listSummary));
	}

	private PayrollSummaryBLDto summarize(PayrollBLDto arg) {
		PayrollSummaryBLDto summary = null;
		summary = new PayrollSummaryBLDto();
		summary.id = arg.id;
		summary.version = arg.version;
		summary.date = arg.date;
		summary.netWage = arg.monthlyWage + arg.bonus + arg.productivityBonus
				+ arg.trienniumPayment - arg.incomeTax - arg.nic;
		return summary;
	}

	private List<PayrollSummaryBLDto> summarize(List<PayrollBLDto> arg) {
		PayrollSummaryBLDto summary = null;
		List<PayrollSummaryBLDto> list = new ArrayList<>();
		for (PayrollBLDto dal : arg) {
			summary = summarize(dal);
			list.add(summary);
		}
		return list;
	}

	@When("I try to find payrolls with null mechanic id")
	public void i_try_to_find_payrolls_with_null_mechanic_id() {
		tryGetPayrollsForMechanicAndKeepException(null);

	}

	@When("I try to find payrolls with a non existent id")
	public void i_try_to_find_payrolls_with_a_non_existent_id() {
		tryGetPayrollsForMechanicAndKeepException(UUID.randomUUID().toString());
	}

	@When("I try to find payrolls with mechanic {string} argument")
	public void i_try_to_find_payrolls_with_mechanic_argument(String arg) {
		tryGetPayrollsForMechanicAndKeepException(arg);

	}

	@When("I search payrolls for the mechanic")
	public void i_search_payrolls_for_the_mechanic() throws BusinessException {
		MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);
		listSummary = service.getAllPayrollsForMechanic(m.id);

	}

	@Then("The payroll summary for this mechanic is found")
	public void the_payroll_for_this_mechanic_is_found() {
		PayrollSummaryBLDto aux = summarize(
				(PayrollBLDto) ctx.get(Key.PAYROLL));
		assertTrue(listSummary.size() == 1);

		assertTrue(PayrollUtil.matchPayrollSummaries(aux, listSummary.get(0)));

	}

	@SuppressWarnings("unchecked")
	@Then("All payroll summaries for this mechanic are found")
	public void all_payrolls_for_this_mechanic_are_found() {
		List<PayrollSummaryBLDto> aux = summarize(
				(List<PayrollBLDto>) ctx.get(Key.PAYROLLS));
		assertTrue(aux.size() == listSummary.size());
		assertTrue(PayrollUtil.matchPayrollSummaries(aux, listSummary));
	}

	@When("I search payrolls for a professional group with no payrolls")
	public void i_search_payrolls_for_a_professional_group_with_no_payrolls()
			throws BusinessException {
		listSummary = service.getAllPayrollsForProfessionalGroup("II");
	}

	@When("I try to find payrolls with null professional group name")
	public void i_try_to_find_payrolls_with_null_professional_group_id() {
		tryGetPayrollsForProfessionalGroupAndKeepException(null);

	}

	@When("I try to find payrolls with professional group {string} argument")
	public void i_try_to_find_payrolls_with_professional_group_argument(
			String arg) {
		tryGetPayrollsForProfessionalGroupAndKeepException(arg);

	}

	@When("I try to find payrolls with a non existent professional group name")
	public void i_try_to_find_payrolls_with_a_non_existent_professional_group_id() {
		tryGetPayrollsForProfessionalGroupAndKeepException(
				UUID.randomUUID().toString());

	}

	@When("I search payrolls for the professional group")
	public void i_search_payrolls_for_the_professional_group()
			throws BusinessException {
		listSummary = service.getAllPayrollsForProfessionalGroup("I");
	}

	@Then("The payroll summary for this professional group is found")
	public void the_payroll_for_this_professional_group_is_found() {

		PayrollSummaryBLDto aux = summarize(
				(PayrollBLDto) ctx.get(Key.PAYROLL));
		assertTrue(listSummary.size() == 1);
		assertTrue(PayrollUtil.matchPayrollSummaries(aux, listSummary.get(0)));
	}

	@SuppressWarnings("unchecked")
	@Then("All payroll summaries for this professional group are found")
	public void all_payrolls_for_this_professional_group_are_found() {
		List<PayrollSummaryBLDto> aux = summarize(
				(List<PayrollBLDto>) ctx.get(Key.PAYROLLS));
		assertTrue(aux.size() == listSummary.size());
		assertTrue(PayrollUtil.matchPayrollSummaries(aux, listSummary));
	}

	private void tryGetPayrollDetailsAndKeepException(String id) {
		try {
			service.getPayrollDetails(id);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

	private void tryGetPayrollsForMechanicAndKeepException(String arg) {
		try {
			service.getAllPayrollsForMechanic(arg);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

	private void tryGetPayrollsForProfessionalGroupAndKeepException(
			String arg) {
		try {
			service.getAllPayrollsForProfessionalGroup(arg);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

}
