package uo.ri.cws.persistence.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Address;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.persistence.util.UnitOfWork;

public class OwnMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Vehicle vehicle;
	private Client client;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle("plate-1010", "make", "model" );
		client = new Client("dni", "nombre", "apellidos");
		Address address = new Address("street", "city", "zipcode");
		client.setAddress(address);

		Associations.Own.link(client, vehicle);

		unitOfWork.persist(vehicle, client);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( vehicle, client );
		factory.close();
	}

	/**
	 * A client recovers its vehicles
	 */
	@Test
	public void testClientRecoversVehicles() {

		Client restored = unitOfWork.findById( Client.class, client.getId() );

		assertTrue( restored.getVehicles().contains( vehicle ) );
		assertEquals( 1, restored.getVehicles().size() );
	}

	/**
	 * A vehicle recovers its client
	 */
	@Test
	public void testVehicleRecoversClient() {

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertEquals( client, restored.getClient() );
	}

}
