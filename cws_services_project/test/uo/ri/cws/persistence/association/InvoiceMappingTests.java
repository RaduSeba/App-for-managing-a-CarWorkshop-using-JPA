package uo.ri.cws.persistence.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

public class InvoiceMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private Invoice invoice;
	private Mechanic mechanic;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("mechanic-dni");
		vehicle = new Vehicle("plate-1010", "make", "model" );
		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		workOrder.markAsFinished();

		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(workOrder, vehicle, invoice, mechanic);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( workOrder, vehicle, invoice, mechanic );
		factory.close();
	}

	/**
	 * An invoice recovers its work orders
	 */
	@Test
	public void testInvoiceRecoversWorkOrders() {

		Invoice restored = unitOfWork.findById( Invoice.class, invoice.getId() );

		assertTrue( restored.getWorkOrders().contains( workOrder ) );
		assertEquals( 1, restored.getWorkOrders().size() );
	}

	/**
	 * A workOrder recovers its invoice
	 */
	@Test
	public void testWorkOrderRecoversVehicle() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( invoice, restored.getInvoice() );
	}

}
