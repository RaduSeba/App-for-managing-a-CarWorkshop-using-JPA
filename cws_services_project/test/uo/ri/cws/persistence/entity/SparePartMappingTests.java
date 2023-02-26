package uo.ri.cws.persistence.entity;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.SparePart;
import uo.ri.cws.persistence.util.UnitOfWork;

public class SparePartMappingTests {

	private SparePart sparePart;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		sparePart = new SparePart("code", "description", 100.0);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( sparePart );
		factory.close();
	}

	/**
	 * All fields of sparePart are persisted properly
	 */
	@Test
	public void testAllFieldsPersisted() {
		unitOfWork.persist(sparePart);

		SparePart restored = unitOfWork.findById( SparePart.class, sparePart.getId() );

		assertEquals( sparePart.getId(), restored.getId() );
		assertEquals( sparePart.getCode(), restored.getCode() );
		assertEquals( sparePart.getDescription(), restored.getDescription() );
		assertEquals( sparePart.getPrice(), restored.getPrice(), 0.0001 );
	}

	/**
	 * There cannot be persisted two spareParts with the same code
	 */
	@Test(expected=PersistenceException.class)
	public void testRepeated() {
		unitOfWork.persist(sparePart);
		SparePart repeatedCodeSparePart = new SparePart( sparePart.getCode() );

		unitOfWork.persist( repeatedCodeSparePart );
	}

}
