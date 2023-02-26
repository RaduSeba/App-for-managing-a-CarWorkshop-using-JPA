package uo.ri.cws.associations;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Vehicle;


public class OwnTests {
	private Vehicle vehicle;
	private Client client;

	@Before
	public void setUp() {
		client = new Client("dni-cliente", "nombre", "apellidos");
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		Associations.Own.link(client, vehicle);
	}
	
	@Test
	public void testLinkOnOwn() {
		assertTrue( client.getVehicles().contains( vehicle ));
		assertTrue( vehicle.getClient() == client );
	}

	@Test
	public void testUnlinkOnOwn() {
		Associations.Own.unlink(client, vehicle);

		assertTrue( ! client.getVehicles().contains( vehicle ));
		assertTrue( vehicle.getClient() == null );
	}

	@Test
	public void testSafeReturn() {
		Set<Vehicle> vehicles = client.getVehicles();
		vehicles.remove( vehicle );

		assertTrue( vehicles.size() == 0 );
		assertTrue( "It must be a copy of the collection or a read-only version", 
			client.getVehicles().size() == 1
		);
	}


}
