package uo.ri.cws.persistence.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.persistence.util.UnitOfWork;

public class ClassifyMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Vehicle vehicle;
	private VehicleType vehicleType;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle("plate-1010", "make", "model" );
		vehicleType = new VehicleType("type", 50);

		Associations.Classify.link(vehicleType, vehicle);

		unitOfWork.persist(vehicle, vehicleType);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( vehicle, vehicleType );
		factory.close();
	}

	/**
	 * A vehicle type recovers its vehicles
	 */
	@Test
	public void testVehicleTypeRecoversVehicles() {

		VehicleType restored = unitOfWork.findById( VehicleType.class, vehicleType.getId() );

		assertTrue( restored.getVehicles().contains( vehicle ) );
		assertEquals( 1, restored.getVehicles().size() );
	}

	/**
	 * A vehicle recovers its vehicle type
	 */
	@Test
	public void testVehicleRecoversVehicleType() {

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertEquals( vehicleType, restored.getVehicleType() );
	}

}
