package uo.ri.cws.associations;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;


public class ClassifyTests {
	private Vehicle vehicle;
	private VehicleType vehicleType;

	@Before
	public void setUp() {
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		vehicleType = new VehicleType("coche", 50.0);
		Associations.Classify.link(vehicleType, vehicle);
	}
	
	@Test
	public void testLinkOnClassify() {
		assertTrue( vehicleType.getVehicles().contains( vehicle ));
		assertTrue( vehicle.getVehicleType() == vehicleType );
	}

	@Test
	public void testUnlinkOnClassify() {
		Associations.Classify.unlink(vehicleType, vehicle);

		assertTrue( ! vehicleType.getVehicles().contains( vehicle ));
		assertTrue( vehicle.getVehicleType() == null );
	}

	@Test
	public void testSafeReturn() {
		Set<Vehicle> vehiculos = vehicleType.getVehicles();
		vehiculos.remove( vehicle );

		assertTrue( vehiculos.size() == 0 );
		assertTrue( "It must be a copy of the collection", 
			vehicleType.getVehicles().size() == 1
		);
	}

}
