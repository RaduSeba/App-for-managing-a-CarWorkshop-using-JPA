package uo.ri.cws.domain;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class SettleInvoiceTest {

	private WorkOrder w;
	private Cash cash;
	private Mechanic m;
	private Vehicle v;

	@Before
	public void setUp() throws Exception {
		Client c = new Client("123", "n", "a");
		cash = new Cash( c );
		m = new Mechanic("123a");
		v = new Vehicle("123-ABC");
		VehicleType tv = new VehicleType("v", 300 /* €/hour */);
		Associations.Classify.link(tv, v);

		w = createFinishedWorkOrder(m, v, 83/* min */); // gives 500 € ii
	}

	private WorkOrder createFinishedWorkOrder(Mechanic m, Vehicle v, int min){
		w = new WorkOrder(v, "for test");
		w.assignTo( m );
		new Intervention(m, w, min);
		w.markAsFinished();
		return w;
	}


	/**
	 * An invoiced with 0 charges but finished workOrders can be settled
	 */
	@Test
	public void test0Ammount() {
		WorkOrder a = createFinishedWorkOrder(m, v, 0 /*mins*/); // 0€ importe
		Invoice f = new Invoice(123L, Arrays.asList(a));
		f.settle();

		assertTrue( f.isSettled() );
	}

	/**
	 * An invoice which is not totally paid with a 0.01 € margin cannot
	 * be settled
	 */
	@Test(expected = IllegalStateException.class)
	public void testUnderPaid() {
		Invoice f = new Invoice(123L, Arrays.asList(w));
		double importe = f.getAmount() - 0.02 /*€*/;
		new Charge(f, cash, importe);
		f.settle();
	}

	/**
	 * An invoice which is overpaid with a 0.01 margin cannot be settled
	 */
	@Test(expected = IllegalStateException.class)
	public void testOverPaid()  {
		Invoice f = new Invoice(123L, Arrays.asList(w));
		double importe = f.getAmount() + 0.02 /*€*/;
		new Charge(f, cash, importe);
		f.settle();
	}

	/**
	 * An invoice which is perfectly paid within the 0.01 margin can
	 * be settled
	 */
	@Test
	public void testPerfectlyPaid()  {
		Invoice f = new Invoice(123L, Arrays.asList(w));
		new Charge(f, cash, f.getAmount() );
		f.settle();

		assertTrue( f.isSettled() );
	}

	/**
	 * An invoice under paid within the 0.01 margin can be settled
	 */
	@Test
	public void testUnderPaidInMargin() {
		Invoice f = new Invoice(123L, Arrays.asList(w));
		double importe = f.getAmount() - 0.009 /*€*/;
		new Charge(f, cash, importe);
		f.settle();

		assertTrue( f.isSettled() );
	}

	/**
	 * An invoice over paid within the 0.01 margin can be settled
	 */
	@Test
	public void testOverPaidInMargin() {
		Invoice f = new Invoice(123L, Arrays.asList(w));
		double importe = f.getAmount() + 0.009 /*€*/;
		new Charge(f, cash, importe);
		f.settle();

		assertTrue( f.isSettled() );
	}


}
