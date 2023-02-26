package uo.ri.cws.domain;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class InvoiceTests {

	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private SparePart sparePart;
	private Vehicle vehicle;
	private VehicleType vehicleType;
	private Client client;

	@Before
	public void setUp() {
		client = new Client("dni-cliente", "nombre", "apellidos");
		vehicle = new Vehicle("1234 GJI", "ibiza", "seat");
		Associations.Own.link(client, vehicle);

		vehicleType = new VehicleType("coche", 50.0);
		Associations.Classify.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic("dni-mecanico", "nombre", "apellidos");
		workOrder.assignTo(mechanic);

		intervention = new Intervention(mechanic, workOrder, 60);

		sparePart = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(sparePart, intervention, 2);

		workOrder.markAsFinished();
	}

	/**
	 * Right computation of the invoice amount ( 260 € + IVA 21%)
	 */
	@Test
	public void testInvoiceAmount() {
		List<WorkOrder> workOrders = new ArrayList<>();
		workOrders.add( workOrder );
		Invoice invoice = new Invoice( 0L, workOrders );

		assertTrue( invoice.getAmount() ==  302.5 );
	}

	/**
	 * Constructor with two work orders added through the constructor
	 */
	@Test
	public void testAmountForTwoWorkOrders() {
		List<WorkOrder> workOrders = new ArrayList<>();
		workOrders.add( workOrder );
		workOrders.add( createAnotherWorkOrder() );
		Invoice invoice = new Invoice( 0L, workOrders );

		// amount = (137.5 new work order + 250.0 first one) * 21% iva
		assertTrue( invoice.getAmount() ==  468.88 ); // 2 cents rounded
	}

	/**
	 * Computation of the new amount after adding a new work order to the invoice
	 * Added by association
	 */
	@Test
	public void testNewAmountAfterAddingWorkOrder() {
		Invoice invoice = new Invoice( 0L ); // 0L as dummy invoice number
		invoice.addWorkOrder(workOrder);

		assertTrue( invoice.getAmount() ==  302.5 );
	}

	/**
	 * Computation for two work orders added by association
	 */
	@Test
	public void testInvoiceAmountAddingTwoWorkOrders() {
		Invoice invoice = new Invoice( 0L );
		invoice.addWorkOrder( workOrder );
		invoice.addWorkOrder( createAnotherWorkOrder() );

		assertTrue( invoice.getAmount() ==  468.88 ); // 2 cents rounding
	}

	/**
	 * A new invoice with work orders is in NOT_YET_PAID status
	 */
	@Test
	public void testNewInvoiceIsNotYetPaidStatus() {
		List<WorkOrder> workOrders = new ArrayList<>();
		workOrders.add( workOrder );
		Invoice invoice = new Invoice( 0L, workOrders );

		assertTrue( invoice.isNotSettled() );
	}

	/**
	 * If the date of the invoice is before 1/7/2012 the VAT (IVA) is 18% and
	 * it amounts 250€ + VAT 18%
	 */
	@Test
	public void testAmountForInvoicesPriorJuly2012() {
		LocalDate JUNE_6_2012 = LocalDate.of(2012, 6, 15);

		List<WorkOrder> workOrders = new ArrayList<>();
		workOrders.add( workOrder );
		Invoice invoice = new Invoice( 0L, JUNE_6_2012, workOrders ); // vat 18%

		assertTrue( invoice.getAmount() ==  295.0 );
	}

	/**
	 * A work order, when added to an invoice, changes its status to INVOICED
	 * Added through the constructor
	 */
	@Test
	public void testInvoicedWorkOrthersStatusInvoiced() {
		List<WorkOrder> workOrders = Arrays.asList( workOrder );
		new Invoice( 0L, workOrders );

		assertTrue( workOrder.isInvoiced() );
	}

	/**
	 * A work order, when added to an invoice, changes its status to INVOICED
	 * Added by association
	 */
	@Test
	public void testAveriasFacturadasAddAveria() {
		new Invoice( 0L ).addWorkOrder( workOrder );

		assertTrue( workOrder.isInvoiced() );
	}

	/**
	 * All the work orders changes its status to INVOICED when added to
	 * an invoice
	 */
	@Test
	public void testDosAveriasFacturadasAddAveria() {
		WorkOrder otherWorkOrther = createAnotherWorkOrder();

		Invoice f = new Invoice( 0L );
		f.addWorkOrder( workOrder );
		f.addWorkOrder( otherWorkOrther );

		assertTrue( workOrder.isInvoiced() );
		assertTrue( otherWorkOrther.isInvoiced() );
	}

	/**
	 * Creates a new invoiced witha delay of 100 milliseconds to avoid a
	 * collision in the dates field (same millisecond)
	 *
	 * It could be problematic if the identity of the work order depends on
	 * the date
	 * @return the newly created work order
	 */
	private WorkOrder createAnotherWorkOrder() {
		sleep( 100 );
		WorkOrder workOrder = new WorkOrder(vehicle, "falla la junta la trocla otra vez");
		workOrder.assignTo( mechanic );

		Intervention i = new Intervention(mechanic, workOrder, 45);
		new Substitution(sparePart, i, 1);

		workOrder.markAsFinished();

		// amount = 100 € spare part + 37.5 laboring time
		return workOrder;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
			// dont't care if this occurs
		}
	}

}
