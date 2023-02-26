package uo.ri.cws.persistence.entity;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Address;
import uo.ri.cws.domain.Client;
import uo.ri.cws.persistence.util.UnitOfWork;

public class ClientMappingTests {

	private Client client;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		client = new Client("dni", "nombre", "apellidos");
		Address address = new Address("street", "city", "zipcode");
		client.setAddress(address);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( client );
		factory.close();
	}

	/**
	 * All fields of client are persisted properly
	 */
	@Test
	public void testAllFieldsPersisted() {
		unitOfWork.persist(client);

		Client restored = unitOfWork.findById( Client.class, client.getId() );

		assertEquals( client.getId(), restored.getId() );
		assertEquals( client.getDni(), restored.getDni() );
		assertEquals( client.getName(), restored.getName() );
		assertEquals( client.getSurname(), restored.getSurname() );
		assertEquals( client.getEmail(), restored.getEmail() );
		assertEquals( client.getPhone(), restored.getPhone() );
		assertEquals( client.getAddress(), restored.getAddress() );
	}

	/**
	 * When two clients with the same DNI, the second cannot be persisted
	 */
	@Test(expected=PersistenceException.class)
	public void testRepeated() {
		unitOfWork.persist(client);
		Client repeatedDniClient = new Client( client.getDni() );

		unitOfWork.persist( repeatedDniClient );
	}

}
