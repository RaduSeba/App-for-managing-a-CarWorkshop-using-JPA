package uo.ri.cws.ext.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.WorkOrder.WorkOrderState;

public class PayrollTest {

	private Contract contract;
	private ContractType type = new ContractType("T", 1.5);
	private double productivityPercent = 10.0;
	private double trienniumMoney = 100.0;
	private ProfessionalGroup group = new ProfessionalGroup("XI",
			trienniumMoney, productivityPercent);
	private Mechanic mechanic = new Mechanic("dni", "nombre", "apellidos");
	private LocalDate endDate = LocalDate
		.now().with(TemporalAdjusters.lastDayOfMonth()), startDate = null;
	private double monthlyWage = 3650;
	private double annualWage = 51100; // salario base anual
	private double nic = Math.floor(((annualWage * 0.05) / 12) * 100) / 100;
	private LocalDate june = LocalDate.of(2022, 6, 30);
	private LocalDate november = LocalDate.of(2022, 11, 30);

	@Before
	public void setUp() throws Exception {
		endDate = LocalDate
			.now().plusMonths(6).with(TemporalAdjusters.lastDayOfMonth());

		contract = new Contract(mechanic, type, group, endDate, annualWage);
	}

	/**
	 * A new payroll
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testPayrollIllegalExceptionIfNullArg() {
		new Payroll(null);
	}

	/**
	 * A new payroll
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testPayrollIllegalExceptionIfNullDate() {
		new Payroll(contract, null);
	}

	/**
	 * A new payroll
	 */
	@Test
	public void testConstructorNow() {

		Payroll p = new Payroll(contract);
		assertTrue(p.getContract().equals(contract));
		assertTrue(p.getDate().equals(LocalDate.now()));
	}

	/**
	 * A new payroll
	 */
	@Test
	public void testConstructorWithDate() {
		LocalDate d = LocalDate.now().minusDays(1).minusMonths(2).minusYears(3);
		Payroll p = new Payroll(contract, d);
		assertTrue(p.getContract().equals(contract));
		assertTrue(p.getDate().equals(d));
	}

	@Test
	public void testNovemberNoActivityShortTermContract() {
		startDate = LocalDate
			.now().minusMonths(7).with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);

		Payroll p = new Payroll(contract, november);

		assertTrue(p.getDate().equals(november));
		assertTrue(p.getContract().equals(contract));
		double irpf = this.monthlyWage * 0.37;

		assertEquals(p.getMonthlyWage(), this.monthlyWage, 0.01);
		assertEquals(p.getBonus(), 0.0, 0.01);
		assertEquals(p.getProductivityBonus(), 0.0, 0.01);
		assertEquals(p.getTrienniumPayment(), 0.0, 0.01);
		assertEquals(p.getNIC(), nic, 0.01);
		assertEquals(p.getIncomeTax(), irpf, 0.01);

	}

	@Test
	public void testJuneNoActivityShortTermContract() {
		startDate = LocalDate
			.now().minusMonths(7).with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		Payroll p = new Payroll(contract, june);

		assertTrue(p.getDate().equals(june));
		assertTrue(p.getContract().equals(contract));
		double irpf = this.monthlyWage * 2 * 0.37;
		
		assertEquals(p.getMonthlyWage(), this.monthlyWage, 0.01); // june ->
																	// extra
		assertEquals(p.getBonus(), this.monthlyWage, 0.01);
		assertEquals(p.getProductivityBonus(), 0.0, 0.01);
		assertEquals(p.getTrienniumPayment(), 0.0, 0.01);
		assertEquals(p.getNIC(), nic, 0.01);
		assertEquals(p.getIncomeTax(), irpf, 0.01);

	}

	@Test
	public void testNovemberWithProductivityNoTriennium() {
		startDate = LocalDate
			.now().minusMonths(7).with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		double interventions = 365.0;
		double productivity = interventions * productivityPercent / 100; // productividad
																			// mensual
		createWorkOrders(november, interventions);
		double irpf = (this.monthlyWage + productivity) * 0.37;
		Payroll p = new Payroll(contract, november);

		assertTrue(p.getDate().equals(november));
		assertTrue(p.getContract().equals(contract));
		assertEquals(p.getMonthlyWage(), this.monthlyWage, 0.01); // june ->
																	// extra
		assertEquals(p.getBonus(), 0.0, 0.01);
		assertEquals(p.getProductivityBonus(), productivity, 0.01);
		assertEquals(p.getTrienniumPayment(), 0.0, 0.01);
		assertEquals(p.getNIC(), nic, 0.01);
		assertEquals(p.getIncomeTax(), irpf, 0.01);

	}

	@Test
	public void testJuneWithProductivityNoTriennium() {
		startDate = LocalDate
			.now().minusMonths(7).with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		double interventions = 365.0;
		double productivity = interventions * productivityPercent / 100; // productividad
																			// mensual
		createWorkOrders(june, interventions);
		double irpf = (this.monthlyWage * 2 + productivity) * 0.37;
		Payroll p = new Payroll(contract, june);

		assertTrue(p.getDate().equals(june));
		assertTrue(p.getContract().equals(contract));
		assertEquals(p.getMonthlyWage(), this.monthlyWage, 0.01);
		assertEquals(p.getBonus(), this.monthlyWage, 0.01);// june -> extra
		assertEquals(p.getProductivityBonus(), productivity, 0.01);
		assertEquals(p.getTrienniumPayment(), 0.0, 0.01);
		assertEquals(p.getNIC(), nic, 0.01);
		assertEquals(p.getIncomeTax(), irpf, 0.01);
	}

	@Test
	public void testNovemberWithProductivityWithTriennium() {
		startDate = LocalDate
			.now().minusMonths(38).with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		double interventions = 365.0;
		double productivity = interventions * productivityPercent / 100; // productividad
																			// mensual
		createWorkOrders(november, interventions);
		double irpf = (this.monthlyWage + productivity + trienniumMoney) * 0.37;
		Payroll p = new Payroll(contract, november);

		assertTrue(p.getDate().equals(november));
		assertTrue(p.getContract().equals(contract));
		assertEquals(p.getMonthlyWage(), this.monthlyWage, 0.01);
		assertEquals(p.getBonus(), 0.0, 0.01);
		assertEquals(p.getProductivityBonus(), productivity, 0.01);
		assertEquals(p.getTrienniumPayment(), trienniumMoney, 0.01);
		assertEquals(p.getNIC(), nic, 0.01);
		assertEquals(p.getIncomeTax(), irpf, 0.01);

	}

	@Test
	public void testJuneWithProductivityWithTriennium() {
		startDate = LocalDate
			.now().minusMonths(55).with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		double interventions = 365.0;
		double productivity = interventions * productivityPercent / 100; // productividad
																			// mensual
		createWorkOrders(june, interventions);
		double irpf = (this.monthlyWage * 2 + productivity + trienniumMoney)
				* 0.37;
		Payroll p = new Payroll(contract, june);

		assertTrue(p.getDate().equals(june));
		assertTrue(p.getContract().equals(contract));
		assertEquals(p.getMonthlyWage(), this.monthlyWage, 0.01);
		assertEquals(p.getBonus(), monthlyWage, 0.01);// june -> extra
		assertEquals(p.getProductivityBonus(), productivity, 0.01);
		assertEquals(p.getTrienniumPayment(), trienniumMoney, 0.01);
		assertEquals(p.getNIC(), nic, 0.01);
		assertEquals(p.getIncomeTax(), irpf, 0.01);

	}

	private void createWorkOrders(LocalDate month, double money) {

		Vehicle v = new Vehicle("plate", "seat", "panda");

		WorkOrder wo = new WorkOrder(v, month.atStartOfDay());
		wo.assignTo(mechanic);
		wo.setState(WorkOrderState.INVOICED);
		wo.setAmount(money);
		new Intervention(mechanic, wo, month.atStartOfDay(), 10);

	}

}
