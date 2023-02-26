package uo.ri.cws.persistence.entity;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.persistence.util.UnitOfWork;

public class VehicleTypeMappingTests {

	private VehicleType vehicleType;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicleType = new VehicleType("vehicleType", 50.0 );
	}

	@After
	public void tearDown() {
		unitOfWork.remove( vehicleType );
		factory.close();
	}

	/**
	 * All fields of client are persisted properly
	 */
	@Test
	public void testAllFieldsPersisted() {
		unitOfWork.persist(vehicleType);

		VehicleType restored = unitOfWork.findById( VehicleType.class, vehicleType.getId() );

		assertEquals( vehicleType.getId(), restored.getId() );
		assertEquals( vehicleType.getName(), restored.getName() );
		assertEquals( vehicleType.getPricePerHour(), restored.getPricePerHour(), 0.001 );
	}

	/**
	 * When two vehicle types have the same name, the second cannot be persisted
	 */
	@Test(expected=PersistenceException.class)
	public void testRepeated() {
		unitOfWork.persist(vehicleType);
		VehicleType repeatedNameVehicleType = new VehicleType( vehicleType.getName() );

		unitOfWork.persist( repeatedNameVehicleType );
	}

}
