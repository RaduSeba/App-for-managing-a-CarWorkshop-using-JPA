package uo.ri.cws.domain;

import static org.junit.Assert.assertEquals;
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
	 * Charge round amounts two cents using conventional rounding
	 */
	@Test
	public void testChargeRaoundsTwoCents() {
		Cash c = new Cash( new Client("123", "n", "a") );
		Invoice f = new Invoice( 123L );

		Charge c1 = new Charge(f, c, 100.001);
		Charge c2 = new Charge(f, c, 100.002);
		Charge c3 = new Charge(f, c, 100.003);
		Charge c4 = new Charge(f, c, 100.004);
		Charge c5 = new Charge(f, c, 100.005);
		Charge c6 = new Charge(f, c, 100.006);
		Charge c7 = new Charge(f, c, 100.007);
		Charge c8 = new Charge(f, c, 100.008);
		Charge c9 = new Charge(f, c, 100.009);

		assertEquals( 100.00, c1.getAmount(), 0.00001 );
		assertEquals( 100.00, c2.getAmount(), 0.00001 );
		assertEquals( 100.00, c3.getAmount(), 0.00001 );
		assertEquals( 100.00, c4.getAmount(), 0.00001 );
		assertEquals( 100.01, c5.getAmount(), 0.00001 );
		assertEquals( 100.01, c6.getAmount(), 0.00001 );
		assertEquals( 100.01, c7.getAmount(), 0.00001 );
		assertEquals( 100.01, c8.getAmount(), 0.00001 );
		assertEquals( 100.01, c9.getAmount(), 0.00001 );
	}


	/**
	 * A charge to a card increases the accumulated
	 */
	@Test
	public void testCardCharge() {
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
	public void testCashCharge() {
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
	public void testVoucherCharge() {
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
