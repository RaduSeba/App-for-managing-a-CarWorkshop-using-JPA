package uo.ri.cws.persistence.entity;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

public class InvoiceMappingTests {

	private EntityManagerFactory factory;
	private UnitOfWork unitOfWork;

	private Invoice invoice;
	private Vehicle vehicle;
	private WorkOrder workOrder;
	private Mechanic mechanic;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("dni", "name", "surname");
		vehicle = new Vehicle( "plateNumber", "make", "model" );
		LocalDateTime now = LocalDateTime.now();
		workOrder = new WorkOrder(vehicle, now, "description");
		workOrder.assignTo(mechanic);
		workOrder.markAsFinished();

		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(vehicle, invoice, workOrder, mechanic);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( invoice, vehicle, workOrder, mechanic );
		factory.close();
	}

	/**
	 * All fields of invoice are persisted properly
	 */
	@Test
	public void testAllFieldsPersisted() {

		Invoice restored = unitOfWork.findById( Invoice.class, invoice.getId() );

		assertEquals( invoice.getId(), restored.getId() );
		assertEquals( invoice.getNumber(), restored.getNumber() );
		assertEquals( invoice.getDate(), restored.getDate() );
		assertEquals( invoice.getAmount(), restored.getAmount(), 0.0001 );
		assertEquals( invoice.getVat(), restored.getVat(), 0.0001 );
		assertEquals( invoice.getState(), restored.getState() );

		assertEquals( invoice.getWorkOrders(), restored.getWorkOrders() );
	}

	/**
	 * When two invoices have the same number, the second cannot be persisted
	 */
	@Test(expected=PersistenceException.class)
	public void testRepeated() {
		Invoice repeatedInvoice = new Invoice( invoice.getNumber() );

		unitOfWork.persist( repeatedInvoice );
	}

}
