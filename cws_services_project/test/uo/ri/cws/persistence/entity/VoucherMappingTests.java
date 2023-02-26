package uo.ri.cws.persistence.entity;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Address;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.persistence.util.UnitOfWork;

public class VoucherMappingTests {

	private Client client;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Voucher voucher;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		client = new Client("dni", "nombre", "apellidos");
		Address address = new Address("street", "city", "zipcode");
		client.setAddress(address);

		voucher = new Voucher("voucher-code", "voucher-description", 100);

		Associations.Pay.link(client, voucher);

		unitOfWork.persist(client, voucher);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( client, voucher );
		factory.close();
	}

	/**
	 * All fields of credit card are persisted properly
	 */
	@Test
	public void testAllFieldsPersisted() {
		Voucher restored = unitOfWork.findById( Voucher.class, voucher.getId());

		assertEquals( voucher.getId(), restored.getId() );
		assertEquals( voucher.getCode(), restored.getCode() );
		assertEquals( voucher.getDescription(), restored.getDescription() );
		assertEquals( voucher.getAccumulated(), restored.getAccumulated(), 0.001 );
		assertEquals( voucher.getAvailable(), restored.getAvailable(), 0.001 );
	}

	/**
	 * When two vouchers with the same code, the second cannot be persisted
	 */
	@Test(expected=PersistenceException.class)
	public void testRepeated() {
		Voucher repeated = new Voucher( voucher.getCode(),"another-voucher", 50);

		unitOfWork.persist( repeated );
	}

}
