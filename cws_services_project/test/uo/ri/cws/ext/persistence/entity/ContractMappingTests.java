package uo.ri.cws.ext.persistence.entity;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.persistence.util.UnitOfWork;

public class ContractMappingTests {

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
     * All fields of contract type are persisted properly
     */
    @Test
    public void testAllFieldsPersisted() {
	Contract restored = unitOfWork
		.findById(Contract.class, contract
			.getId());

	assertEquals(contract
		.getId(), restored
			.getId());
	assertEquals(restored
		.getState(), contract
			.getState());
	assertEquals(contract
		.getContractType(), restored
			.getContractType());
	assertEquals(contract
		.getProfessionalGroup(), restored
			.getProfessionalGroup());
	assertEquals(contract
		.getStartDate(), restored
			.getStartDate());
	assertEquals(contract
		.getEndDate(), restored
			.getEndDate());
	assertEquals(contract
		.getMechanic(), restored
			.getMechanic());
	assertEquals(contract
		.getFiredMechanic(), restored
			.getFiredMechanic());
	assertEquals(contract
		.getAnnualBaseWage(), restored
			.getAnnualBaseWage(), 0.001);
	assertEquals(contract
		.getSettlement(), restored
			.getSettlement(), 0.001);
    }

    /**
     * When two contract types with the same mechanic and startDate, the second
     * cannot be persisted
     */
    @Test(expected = PersistenceException.class)
    public void testRepeated() {
	unitOfWork
		.persist(contract);
	ContractType otherType = new ContractType("other-contract-type-name",
		12.0);
	ProfessionalGroup otherGroup = new ProfessionalGroup(
		"other-professional-group-name", 100.0, 20.0);
	Contract repeatedContract = new Contract(mechanic, otherType,
		otherGroup, 3000.0);

	unitOfWork
		.persist(repeatedContract);
    }

}
