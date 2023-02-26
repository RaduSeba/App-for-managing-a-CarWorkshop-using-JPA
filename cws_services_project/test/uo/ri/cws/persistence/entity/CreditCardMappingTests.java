package uo.ri.cws.persistence.entity;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Address;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.persistence.util.UnitOfWork;

public class CreditCardMappingTests {

	private Client client;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private CreditCard creditCard;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		client = new Client("dni", "nombre", "apellidos");
		Address address = new Address("street", "city", "zipcode");
		client.setAddress(address);

		creditCard = new CreditCard("1234-5678", "card-type",
				LocalDate.now().plus(1, ChronoUnit.YEARS)
			);

		Associations.Pay.link(client, creditCard);

		unitOfWork.persist(client, creditCard);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( client, creditCard );
		factory.close();
	}

	/**
	 * All fields of credit card are persisted properly
	 */
	@Test
	public void testAllFieldsPersisted() {
		CreditCard restored = unitOfWork.findById( CreditCard.class,
				creditCard.getId()
			);

		assertEquals( creditCard.getId(), restored.getId() );
		assertEquals( creditCard.getAccumulated(), restored.getAccumulated(), 0.001 );
		assertEquals( creditCard.getNumber(), restored.getNumber() );
		assertEquals( creditCard.getType(), restored.getType() );
		assertEquals( creditCard.getValidThru(), restored.getValidThru() );
	}

	/**
	 * When two cards with the same number, the second cannot be persisted
	 */
	@Test(expected=PersistenceException.class)
	public void testRepeated() {
		CreditCard repeated = new CreditCard( creditCard.getNumber(),
				"another-card-type",
				LocalDate.now()
			);

		unitOfWork.persist( repeated );
	}

}
