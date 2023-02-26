package uo.ri.cws.ext.associations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;

public class TypeTests {
    private Mechanic mechanic;
    private Contract contract;
    private ContractType type;
    private ProfessionalGroup group;

    @Before
    public void setUp() {
	mechanic = new Mechanic("dni", "nombre", "apellidos");
	type = new ContractType("type", 1.5);
	group = new ProfessionalGroup("group", 100.0, 10.0);
	double wage = 10000.0;

	contract = new Contract(mechanic, type, group, wage);

    }

    @Test
    public void testLinkOnRun() {
	assertTrue(contract
		.getContractType()
		.equals(type));
	assertTrue(type
		.getContracts()
		.contains(contract));
    }

    @Test
    public void testUnlinkOnRun() {

	Associations.Type
		.unlink(contract, type);

	assertTrue(contract
		.getContractType() == null);
	assertFalse(type
		.getContracts()
		.contains(contract));

    }

    @Test
    public void testSafeReturn() {
	Set<Contract> contracts = type
		.getContracts();
	int num = contracts
		.size();

	contracts
		.remove(contract);

	assertTrue(type
		.getContracts()
		.size() == num);
	assertTrue("It must be a copy of the collection or a read-only version", type
		.getContracts()
		.contains(contract));
    }

}
