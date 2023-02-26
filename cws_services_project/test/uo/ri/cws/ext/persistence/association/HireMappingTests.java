package uo.ri.cws.ext.persistence.association;

import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.persistence.util.UnitOfWork;

public class HireMappingTests {

    private UnitOfWork unitOfWork;
    private EntityManagerFactory factory;
    private Mechanic mechanic;
    private Contract contract;
    private ContractType type;
    private ProfessionalGroup group;

    @Before
    public void setUp() {
	factory = Persistence
		.createEntityManagerFactory("carworkshop");
	unitOfWork = UnitOfWork
		.over(factory);

	mechanic = new Mechanic("mechanic-dni");

	type = new ContractType("contract-type-name", 2.0);
	group = new ProfessionalGroup("professional-group-name", 300.0, 10.0);
	contract = new Contract(mechanic, type, group, 3000.0);
	unitOfWork
		.persist(contract, group, type, mechanic);
    }

    @After
    public void tearDown() {
	unitOfWork
		.remove(contract, group, type, mechanic);
	factory
		.close();
    }

    /**
     * A contract in force recovers its mechanic
     */
    @Test
    public void testContractInForceRecoversMechanic() {

	Contract restored = unitOfWork
		.findById(Contract.class, contract
			.getId());

	assertTrue(restored
		.getMechanic()
		.isPresent());
	assertTrue(restored
		.getMechanic()
		.get()
		.equals(mechanic));
    }

    /**
     * A mechanic in force recovers its contract
     */
    @Test
    public void testMechanicRecoversContractInForce() {

	Mechanic restored = unitOfWork
		.findById(Mechanic.class, mechanic
			.getId());

	assertTrue(restored
		.getContractInForce()
		.isPresent());
	assertTrue(restored
		.getContractInForce()
		.get()
		.equals(contract));
    }

}
