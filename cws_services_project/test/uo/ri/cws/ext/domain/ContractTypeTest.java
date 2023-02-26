package uo.ri.cws.ext.domain;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;

public class ContractTypeTest {

	private ContractType ct = null;

	@Before
	public void setUp() throws Exception {
		ct = new ContractType("name", 1.5);

	}

	/**
	 * A new contract type object has no contracts
	 */
	@Test
	public void testConstructor() {

		assertTrue(ct	.getName()
						.equals("name"));
		assertTrue(ct.getCompensationDays() == 1.5);
		assertTrue(ct	.getContracts()
						.isEmpty());
	}

	/**
	 * After creating a contract, contract type references the contract
	 */
	@Test
	public void testNewContract() {

		LocalDate endDate = LocalDate	.now()
										.plusMonths(6)
										.with(TemporalAdjusters.lastDayOfMonth());

		Contract contract = new Contract(new Mechanic("dni", "name", "surname"),
				ct, new ProfessionalGroup("name", 1, 1), endDate, 1000.0);

		assertTrue(ct	.getContracts()
						.contains(contract));
	}

}
