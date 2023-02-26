package uo.ri.cws.domain;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

public class ChargeTests {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * A charge to a card increases the accumulated
	 */
	@Test
	public void testCargoTarjeta() {
		LocalDate tomorrow = LocalDate.now().plus(1, ChronoUnit.DAYS);
		CreditCard t = new CreditCard("123", "visa", tomorrow);
		Invoice f = new Invoice( 123L );

		new Charge(f, t, 100.0);

		assertTrue(t.getAccumulated() == 100.0);
	}

	/**
	 * A charge to cash increases the accumulated
	 */
	@Test
	public void testCargoMetalico() {
		Cash m = new Cash( new Client("123", "n", "a") );
		Invoice f = new Invoice( 123L );

		new Charge(f, m, 100.0);

		assertTrue(m.getAccumulated() == 100.0);
	}

	/**
	 * A charge to a voucher increases the accumulated and decreases the
	 * available
	 */
	@Test
	public void testCargoBono() {
		Voucher b = new Voucher("123", "For testing", 150.0);
		Invoice f = new Invoice( 123L );

		new Charge(f, b, 100.0);

		assertTrue(b.getAccumulated() == 100.0);
		assertTrue(b.getAvailable() == 50.0);
	}

	/**
	 * A credit card cannot be charged if its expiration date is over
	 * @throws IllegalStateException
	 */
	@Test(expected = IllegalStateException.class)
	public void tesChargeExpiredCard() {
		LocalDate expired = LocalDate.now().minus(1, ChronoUnit.DAYS);
		CreditCard t = new CreditCard("123", "TTT", expired);
		Invoice f = new Invoice(123L);

		new Charge(f, t, 100.0); // Card validity date expired exception
	}

	/**
	 * A voucher cannot be charged if it has no available money
	 * @throws IllegalStateException
	 */
	@Test(expected = IllegalStateException.class)
	public void testEmptyVoucherCannotBeCharged() {
		Voucher b = new Voucher("123", "For testing", 150.0);
		Invoice f = new Invoice(123L);

		new Charge(f, b, 151.0);  // Not enough money exception
	}

}
