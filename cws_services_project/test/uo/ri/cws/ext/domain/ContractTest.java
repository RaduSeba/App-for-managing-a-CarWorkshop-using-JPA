package uo.ri.cws.ext.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Contract.ContractState;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.domain.ProfessionalGroup;

public class ContractTest {

	private Contract contract = null;

	private Mechanic mechanic;
	private double wage = 51100; // salario base anual
	private double dailyWage = 140; // salario diario sin productividay

	private double monthlyWage = 3650;
	private double compensationDays = 2.5;
	private LocalDate endDate = LocalDate	.now()
											.with(TemporalAdjusters.lastDayOfMonth()),
			startDate = null;
	private ContractType type = new ContractType("T", compensationDays);
	private ProfessionalGroup group = new ProfessionalGroup("G", 100.0, 10.0);

	private double productivity;

	private double trienniums;

	private double tax;

	private double nic;

	@Before
	public void setUp() throws Exception {
		mechanic = new Mechanic("dni", "nombre", "apellidos");
		contract = new Contract(mechanic, type, group, endDate, wage);

	}

	/**
	 * A new contract sets mechanic, type, group, start date, end date and wage
	 * No payrolls yet
	 */
	@Test
	public void testConstructor() {

		assertTrue(contract.getAnnualBaseWage() == wage);
		assertTrue(contract	.getContractType()
							.equals(type));
		assertTrue(contract	.getProfessionalGroup()
							.equals(group));
		assertTrue(contract	.getMechanic()
							.get()
							.equals(mechanic));
		assertTrue(ContractState.IN_FORCE.equals(contract.getState()));
		assertTrue(contract.getAnnualBaseWage() == wage);
		assertTrue(contract.getSettlement() == 0.0);
		assertTrue(contract	.getPayrolls()
							.isEmpty());

	}

	@Test
	public void testTerminateNoActivityShorterThanOneYear() {
		startDate = LocalDate	.now()
								.minusMonths(7)
								.with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		createPayrolls();
		contract.terminate();

		assertTrue(contract	.getStartDate()
							.equals(startDate));
		assertTrue(contract	.getEndDate()
							.get()
							.equals(endDate));
		assertTrue(ContractState.TERMINATED.equals(contract.getState()));
		assertTrue(mechanic	.getContractInForce()
							.isEmpty());
		assertTrue(contract	.getEndDate()
							.get()
							.equals(LocalDate	.now()
												.with(TemporalAdjusters.lastDayOfMonth())));
		assertTrue(contract.getSettlement() == 0);
	}

	@Test
	public void testTerminateNoActivityOneYear() {
		startDate = LocalDate	.now()
								.minusMonths(17)
								.with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		createPayrolls();

		contract.terminate();

		assertTrue(contract	.getStartDate()
							.equals(startDate));
		assertTrue(contract	.getEndDate()
							.get()
							.equals(endDate));
		assertTrue(ContractState.TERMINATED.equals(contract.getState()));
		assertTrue(mechanic	.getContractInForce()
							.isEmpty());
		assertTrue(contract	.getEndDate()
							.get()
							.equals(LocalDate	.now()
												.with(TemporalAdjusters.lastDayOfMonth())));
		assertEquals(contract.getSettlement(),
				this.dailyWage * this.compensationDays, 0.01);
	}

	@Test
	public void testTerminateNoActivitySeveralYears() {
		startDate = LocalDate	.now()
								.minusMonths(36)
								.with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		createPayrolls();

		contract.terminate();

		assertTrue(contract	.getStartDate()
							.equals(startDate));
		assertTrue(contract	.getEndDate()
							.get()
							.equals(endDate));
		assertTrue(ContractState.TERMINATED.equals(contract.getState()));
		assertTrue(mechanic	.getContractInForce()
							.isEmpty());
		assertTrue(contract	.getEndDate()
							.get()
							.equals(LocalDate	.now()
												.with(TemporalAdjusters.lastDayOfMonth())));
		assertEquals(contract.getSettlement(),
				this.dailyWage * this.compensationDays * 3, 0.01);
	}

	@Test
	public void testTerminateActivityShorterThanOneYear() {
		startDate = LocalDate	.now()
								.minusMonths(7)
								.with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		productivity = 365.0; // productividad mensual
		createPayrolls();

		contract.terminate();

		assertTrue(contract	.getStartDate()
							.equals(startDate));
		assertTrue(contract	.getEndDate()
							.get()
							.equals(endDate));
		assertTrue(ContractState.TERMINATED.equals(contract.getState()));
		assertTrue(mechanic	.getContractInForce()
							.isEmpty());
		assertTrue(contract	.getEndDate()
							.get()
							.equals(LocalDate	.now()
												.with(TemporalAdjusters.lastDayOfMonth())));
		assertTrue(contract.getSettlement() == 0);
	}

	@Test
	public void testTerminateActivityOneYear() {
		startDate = LocalDate	.now()
								.minusMonths(17)
								.with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		productivity = 365.0; // productividad mensual
		dailyWage = 152; // salario diario con productividad
		createPayrolls();
		contract.terminate();

		assertTrue(contract	.getStartDate()
							.equals(startDate));
		assertTrue(contract	.getEndDate()
							.get()
							.equals(endDate));
		assertTrue(ContractState.TERMINATED.equals(contract.getState()));
		assertTrue(mechanic	.getContractInForce()
							.isEmpty());
		assertTrue(contract	.getEndDate()
							.get()
							.equals(LocalDate	.now()
												.with(TemporalAdjusters.lastDayOfMonth())));
		assertEquals(contract.getSettlement(),
				this.dailyWage * this.compensationDays, 0.01);
	}

	@Test
	public void testTerminateActivitySeveralYears() {
		startDate = LocalDate	.now()
								.minusMonths(36)
								.with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		productivity = 365.0; // productividad mensual
		dailyWage = 152; // salario diario con productividad
		createPayrolls();
		contract.terminate();

		assertTrue(contract	.getStartDate()
							.equals(startDate));
		assertTrue(contract	.getEndDate()
							.get()
							.equals(endDate));
		assertTrue(ContractState.TERMINATED.equals(contract.getState()));
		assertTrue(mechanic	.getContractInForce()
							.isEmpty());
		assertTrue(contract	.getEndDate()
							.get()
							.equals(LocalDate	.now()
												.with(TemporalAdjusters.lastDayOfMonth())));
		assertEquals(contract.getSettlement(),
				this.dailyWage * this.compensationDays * 3, 0.01);
	}

	@Test
	public void testTerminateActivitySeveralTriennium() {
		startDate = LocalDate	.now()
								.minusMonths(48)
								.with(TemporalAdjusters.firstDayOfMonth());
		contract.setStartDate(startDate);
		productivity = 365.0; // productividad mensual
		dailyWage = 154.16; // salario diario con productividad y trienios
		trienniums = 65.70; // plus mensual por triennios
		createPayrolls();
		contract.terminate();

		assertTrue(contract	.getStartDate()
							.equals(startDate));
		assertTrue(contract	.getEndDate()
							.get()
							.equals(endDate));
		assertTrue(ContractState.TERMINATED.equals(contract.getState()));
		assertTrue(mechanic	.getContractInForce()
							.isEmpty());
		assertTrue(contract	.getEndDate()
							.get()
							.equals(LocalDate	.now()
												.with(TemporalAdjusters.lastDayOfMonth())));

		assertEquals(contract.getSettlement(),
				this.dailyWage * this.compensationDays * 4, 0.01);
	}

	private void createPayrolls() {
		double extra = 0.0;
		LocalDate lastYear = LocalDate	.now()
										.minusYears(1);
		LocalDate d = (contract	.getStartDate()
								.isBefore(lastYear)) ? lastYear
										: contract.getStartDate();
		while (d.isBefore(LocalDate	.now()
									.plusMonths(1))) {
			if (d.getMonth() == Month.JUNE || d.getMonth() == Month.DECEMBER)
				extra = this.monthlyWage;
			else
				extra = 0.0;
			new Payroll(contract, d, this.monthlyWage, extra, productivity,
					trienniums, tax, nic);
			d = d.plusMonths(1);
		}
	}

}
