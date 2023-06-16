package uo.ri.cws.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CashTest {

	@Before
	public void setUp() throws Exception {
	}

	/** 
	 * A new cash object has no accumulated
	 */
	@Test
	public void testConstructor() {
		Client c = new Client("123", "name", "surname");
		Cash m = new Cash( c );

		assertTrue( m.getClient().equals( c ) );
		assertTrue( m.getAccumulated() == 0.0 );
	}
	
	/**
	 * After paying with cash its accumulated increases
	 */
	@Test
	public void testCashPay() {
		Client c = new Client("123", "name", "surname");
		Cash m = new Cash( c );
		m.pay( 10 );
		
		assertTrue( m.getAccumulated() == 10.0 );
	}

}
