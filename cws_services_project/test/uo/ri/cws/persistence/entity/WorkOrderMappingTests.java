package uo.ri.cws.persistence.entity;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

public class WorkOrderMappingTests {

	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private UnitOfWork unitOfWork;
	private Vehicle vehicle;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle( "plateNumber", "make", "model" );
		LocalDateTime now = LocalDateTime.now();
		workOrder = new WorkOrder(vehicle, now, "description");

		unitOfWork.persist(vehicle, workOrder);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( workOrder, vehicle );
		factory.close();
	}

	/**
	 * All fields of workOrder are persisted properly
	 */
	@Test
	public void testAllFieldsPersisted() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( workOrder.getId(), restored.getId() );
		assertEquals( workOrder.getVehicle(), restored.getVehicle() ); // same id
		assertEquals( workOrder.getDate().truncatedTo(ChronoUnit.MILLIS),
				restored.getDate().truncatedTo(ChronoUnit.MILLIS));
		assertEquals( workOrder.getDescription(), restored.getDescription() );
		assertEquals( workOrder.getAmount(), restored.getAmount(), 0.0001 );
		assertEquals( workOrder.getStatus(), restored.getStatus() );
	}

	/**
	 * When two workOrders are for the same vehicle at the same timestamp,
	 * the second cannot be persisted
	 */
	@Test(expected=PersistenceException.class)
	public void testRepeated() {
		WorkOrder repeatedWorkOrder = new WorkOrder(
				workOrder.getVehicle(),
				workOrder.getDate(),
				"another description"
			);

		unitOfWork.persist( repeatedWorkOrder );
	}

}
