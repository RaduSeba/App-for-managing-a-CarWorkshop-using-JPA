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

public class ContractTypeMappingTests {

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
	ContractType restored = unitOfWork
		.findById(ContractType.class, type
			.getId());

	assertEquals(type
		.getId(), restored
			.getId());
	assertEquals(restored
		.getName(), type
			.getName());
	assertEquals(type
		.getCompensationDays(), restored
			.getCompensationDays(), 0.001);
    }

    /**
     * When two contract types with the same name, the second cannot be
     * persisted
     */
    @Test(expected = PersistenceException.class)
    public void testRepeated() {
	unitOfWork
		.persist(type);
	ContractType repeatedType = new ContractType(type
		.getName(), 150.0);

	unitOfWork
		.persist(repeatedType);
    }

}
