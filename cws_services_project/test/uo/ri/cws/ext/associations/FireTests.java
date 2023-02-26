package uo.ri.cws.ext.associations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;

public class FireTests {
    private Mechanic mechanic;
    private Contract contract;// , secondContract;
    private ContractType type;
    private ProfessionalGroup group;

    @Before
    public void setUp() {
	mechanic = new Mechanic("dni", "nombre", "apellidos");
	type = new ContractType("type", 1.5);
	group = new ProfessionalGroup("group", 100.0, 10.0);
	double wage = 1000.0;

	contract = new Contract(mechanic, type, group, wage);
    }

    @Test
    public void testLinkOnFire() {
	Associations.Fire
		.link(contract);

	Optional<Mechanic> om = contract
		.getFiredMechanic();
	assertTrue(om
		.isPresent());
	Mechanic m = om
		.get();
	assertTrue(m
		.getTerminatedContracts()
		.contains(contract));
	assertTrue(m
		.equals(contract
			.getMechanic()
			.get()));
    }

    @Test
    public void testUnlinkOnFire() {
	contract
		.setFiredMechanic(mechanic);
	mechanic
		._getTerminatedContracts()
		.add(contract);

	Mechanic m = contract
		.getFiredMechanic()
		.get();

	Associations.Fire
		.unlink(contract);

	assertFalse(m
		.getTerminatedContracts()
		.contains(contract));
	assertTrue(contract
		.getFiredMechanic()
		.isEmpty());
    }

    @Test
    public void testSafeReturn() {
	Set<Contract> contracts = mechanic
		.getTerminatedContracts();
	int num = contracts
		.size();

	contracts
		.add(contract);

	assertTrue(contracts
		.size() == num + 1);
	assertFalse("It must be a copy of the collection or a read-only version", mechanic
		.getTerminatedContracts()
		.contains(contract));
    }

}
