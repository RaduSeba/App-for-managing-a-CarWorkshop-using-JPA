package uo.ri.cws.persistence.entity;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.persistence.util.UnitOfWork;

public class VehicleMappingTests {

	private Vehicle vehicle;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle("plate-1010", "make", "model" );
	}

	@After
	public void tearDown() {
		unitOfWork.remove( vehicle );
		factory.close();
	}

	/**
	 * All fields of vehicle are persisted properly
	 */
	@Test
	public void testAllFieldsPersisted() {
		unitOfWork.persist(vehicle);

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertEquals( vehicle.getId(), restored.getId() );
		assertEquals( vehicle.getPlateNumber(), restored.getPlateNumber() );
		assertEquals( vehicle.getMake(), restored.getMake() );
		assertEquals( vehicle.getModel(), restored.getModel() );
	}

	/**
	 * When two vehicle types have the same plate, the second cannot be persisted
	 */
	@Test(expected=PersistenceException.class)
	public void testRepeated() {
		unitOfWork.persist(vehicle);
		Vehicle repeatedPlateVehicle = new Vehicle( vehicle.getPlateNumber() );

		unitOfWork.persist( repeatedPlateVehicle );
	}

}
