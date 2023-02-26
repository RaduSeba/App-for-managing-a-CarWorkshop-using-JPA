package uo.ri.cws.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 */
public class WorkOrderTests {

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

		vehicleType = new VehicleType("coche", 50.0 /* €/hour */);
		Associations.Classify.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic("dni-mecanico", "nombre", "apellidos");
		workOrder.assignTo( mechanic );

		intervention = new Intervention(mechanic, workOrder, 60);

		sparePart = new SparePart("R1001", "junta la trocla", 100.0 /* € */);
		new Substitution(sparePart, intervention, 2);

		workOrder.markAsFinished(); // changes status & computes price
	}

	/**
	 * The amount of the work order is 250.0 €
	 */
	@Test
	public void testBasicWorkOrderAmount() {
		assertTrue( workOrder.getAmount() == 250.0 );
	}

	/**
	 * With two interventions the amount is computed correctly
	 */
	@Test
	public void testComputeAmountForTwoInterventions() {
		workOrder.reopen();  // changes from FINISHED to OPEN again
		Mechanic another = new Mechanic("1", "a", "n");
		workOrder.assignTo( another );
		new Intervention(another, workOrder, 30);

		workOrder.markAsFinished();

		assertTrue( workOrder.getAmount() == 275.0 );
	}

	/**
	 * Removing one intervention the amount is correctly recomputed
	 * The (re)computation is done when the work order passes to FINISHED
	 */
	@Test
	public void testRcomputeAmountWhenRemoveingIntervention() {
		workOrder.reopen();
		Mechanic another = new Mechanic("1", "a", "n");
		workOrder.assignTo( another );
		new Intervention(another, workOrder, 30);

		Associations.Intervene.unlink( intervention );
		workOrder.markAsFinished(); // <-- recomputes here

		assertTrue( workOrder.getAmount() == 25.0 );
	}

	/**
	 * An invoice cannot be added to a non FINISHED work order
	 * @throws IllegalStateException
	 */
	@Test( expected = IllegalStateException.class )
	public void testNotFinishedWorkOrderException() {
		workOrder.reopen();
		List<WorkOrder> workOrders = new ArrayList<>();
		workOrders.add( workOrder );
		new Invoice( 0L,  workOrders ); // <-- must throw IllegalStateExcepcion
	}

	/**
	 * A just created invoice is in NOT_YET_PAID status
	 */
	@Test
	public void testNotYetPaidForNewInvoice() {
		List<WorkOrder> averias = new ArrayList<>();
		averias.add( workOrder );
		Invoice invoice = new Invoice( 0L, averias );

		assertTrue( invoice.isNotSettled() );
	}

	/**
	 * A work order cannot be marked as INVOICED without an assigned invoice
	 * @throws IllegalStateException
	 */
	@Test(expected = IllegalStateException.class)
	public void testCannotBeMarkedAsInvoiced() {
		workOrder.markAsInvoiced();  // must throw exception "not assigned"
	}

}
