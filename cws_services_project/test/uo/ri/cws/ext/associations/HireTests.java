package uo.ri.cws.ext.associations;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;

public class HireTests {
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
    public void testLinkOnHire() {
	Optional<Mechanic> m = contract
		.getMechanic();
	Optional<Contract> contractInForce = mechanic
		.getContractInForce();

	assertTrue(m
		.isPresent());
	assertTrue(m
		.get()
		.equals(mechanic));
	assertTrue(contractInForce
		.isPresent());
	assertTrue(contractInForce
		.get()
		.equals(contract));

    }

    @Test
    public void testUnlinkOnHire() {
	Associations.Hire
		.unlink(contract, mechanic);

	assertTrue(mechanic
		.getContractInForce()
		.isEmpty());
	assertTrue(contract
		.getMechanic()
		.isPresent());
    }

    @Test
    public void testSecondLinkOnHire() {

	Contract secondContract = new Contract(mechanic,
		new ContractType("othertype", 2.5),
		new ProfessionalGroup("othergroup", 200.0, 20.0), 2000.0);

	assertTrue(secondContract
		.getMechanic()
		.isPresent());
	assertTrue(secondContract
		.getMechanic()
		.get()
		.equals(mechanic));
	assertTrue(mechanic
		.isInForce());
	assertTrue(mechanic
		.getContractInForce()
		.get()
		.equals(secondContract));
    }

}
