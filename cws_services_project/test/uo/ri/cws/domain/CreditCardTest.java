package uo.ri.cws.domain;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class CreditCardTest {

	/**
	 * A credit card created with the basic constructor has one day validity
	 * and is of UNKNOWN type
	 */
	@Test
	public void testSimpleConstructor() {
		CreditCard t = new CreditCard("123");
		LocalDate now = LocalDate.now();
		LocalDate in24HoursTime = now.plusDays(1);

		assertTrue( t.getValidThru().isAfter( now ) );
		assertTrue( t.getValidThru().isBefore( in24HoursTime )
				|| t.getValidThru().equals( in24HoursTime )
				);

		assertTrue( t.getType().equals( "UNKNOWN" ));
		assertTrue( t.getAccumulated() == 0.0 );
		assertTrue( t.getNumber().equals( "123" ));
	}

	/**
	 * A credit card with a past date cannot be used to pay
	 */
	@Test
	public void testNotValidAfterDate() {
		CreditCard t = new CreditCard(
				"123", 
				"VISA", 
				LocalDate.now().minusDays(1));
		assertFalse( t.isValidNow() );
	}

	/**
	 * After paying with a card its accumulated increases
	 */
	@Test
	public void testPayCard() {
		CreditCard t = new CreditCard("123");
		t.pay( 10 );

		assertTrue( t.getAccumulated() == 10.0 );
	}

	/**
	 * If a card is used to pay after its valid date an exception is raised
	 */
	@Test(expected=IllegalStateException.class)
	public void testTryToPayAfterDate() {
		CreditCard t = new CreditCard("123", 
				"VISA", 
				LocalDate.now().minusDays(1));
		t.pay( 10 );
	}

	/**
	 * If validity date is changed to past and the card is used to pay 
	 * an exception is raised
	 */
	@Test(expected=IllegalStateException.class)
	public void testSetAndTryToPayAfterDate() {
		CreditCard t = new CreditCard("123", 
				"VISA", 
				LocalDate.now().plusDays(1));
		t.pay( 10 );

		t.setValidThru( LocalDate.now().minusDays(1) );
		t.pay( 10 );
	}

}
