package uo.ri.cws.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uo.ri.util.random.Random;

public class VoucherTest {

	/**
	 * Any new payment mean has 0 accumulated
	 */
	@Test
	public void testNewVoucher() {
		Voucher b = new Voucher( "123", 100.0 );

		assertTrue( b.getDescription().equals( "no-description" ));
		assertTrue( b.getCode().equals( "123" ));
		assertTrue( b.getAccumulated() == 0.0 );
		assertTrue( b.getAvailable() == 100.0 );
	}
	
	/**
	 * After paying with a voucher its accumulated increases 
	 * and its available decreases
	 */
	@Test
	public void testPagoBono() {
		String code = generateNewCode();
		Voucher b = new Voucher(code, "For test", 100);
		b.pay( 10 );
		
		assertTrue( b.getDescription().equals( "For test" ));
		assertTrue( b.getCode().equals( code ));
		assertTrue( b.getAccumulated() == 10.0 );
		assertTrue( b.getAvailable() == 90.0 );
	}
	
	/**
	 * A voucher cannot be charged with an amount greater than its available
	 * @return
	 * @throws IllegalStateException 
	 */
	@Test(expected=IllegalStateException.class)
	public void testCannotBeCharged() {
		Voucher b = new Voucher("123", "For test", 10.0);
		b.pay( 11.0 ); // raises exception
	}
	
	private String generateNewCode() {
		return "V-" + Random.string(5) + "-" + Random.integer(1000, 9999);
	}

}
