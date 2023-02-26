package uo.ri.cws.persistence.entity;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.persistence.util.UnitOfWork;

public class MechanicMappingTests {

	private Mechanic mechanic;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("dni", "nombre", "apellidos");
	}

	@After
	public void tearDown() {
		unitOfWork.remove( mechanic );
		factory.close();
	}

	/**
	 * All fields of mechanic are persisted properly
	 */
	@Test
	public void testAllFieldsPersisted() {
		unitOfWork.persist(mechanic);

		Mechanic restored = unitOfWork.findById(Mechanic.class, mechanic.getId() );

		assertEquals( mechanic.getId(), restored.getId() );
		assertEquals( mechanic.getDni(), restored.getDni() );
		assertEquals( mechanic.getName(), restored.getName() );
		assertEquals( mechanic.getSurname(), restored.getSurname() );
	}

	/**
	 * When two mechanics with the same DNI, the second cannot be persisted
	 */
	@Test(expected=PersistenceException.class)
	public void testRepeated() {
		unitOfWork.persist(mechanic);
		Mechanic repeatedDniMechanic = new Mechanic( mechanic.getDni() );

		unitOfWork.persist( repeatedDniMechanic );
	}

}
