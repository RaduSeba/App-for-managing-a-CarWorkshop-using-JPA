package uo.ri.cws.ext.associations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.domain.ProfessionalGroup;

public class RunTests {
	private Mechanic mechanic;
	private Contract contract;
	private ContractType type;
	private ProfessionalGroup group;
	private double wage;
	private Payroll payroll;

	@Before
	public void setUp() {
		mechanic = new Mechanic("dni", "nombre", "apellidos");
		type = new ContractType("type", 1.5);
		group = new ProfessionalGroup("group", 100.0, 10.0);
		wage = 1000.0;

		contract = new Contract(mechanic, type, group, wage);
		payroll = new Payroll(contract, LocalDate.now());
	}

	@Test
	public void testLinkOnRun() {
		
		assertTrue(contract	.getPayrolls()
							.contains(payroll));
		assertTrue(payroll	.getContract()
							.equals(contract));

	}

	@Test
	public void testUnlinkOnRun() {

		Associations.Run.unlink(payroll);

		assertTrue(payroll.getContract() == null);
		assertFalse(contract.getPayrolls()
							.contains(payroll));

	}

	@Test
	public void testSafeReturn() {

		Set<Payroll> payrolls = contract.getPayrolls();
		int num = payrolls.size();

		payrolls.remove(payroll);

		assertTrue(contract	.getPayrolls()
							.size() == num);
		assertTrue("It must be a copy of the collection or a read-only version",
				contract.getPayrolls()
						.contains(payroll));
	}

}
