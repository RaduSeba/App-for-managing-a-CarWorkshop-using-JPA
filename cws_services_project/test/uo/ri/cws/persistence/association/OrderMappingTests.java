package uo.ri.cws.persistence.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

public class OrderMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle("plate-1010", "make", "model" );
		workOrder = new WorkOrder(vehicle);

		unitOfWork.persist(workOrder, vehicle);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( workOrder, vehicle );
		factory.close();
	}

	/**
	 * A vehicle recovers its work orders
	 */
	@Test
	public void testVehicleRecoversWorkOrders() {

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertTrue( restored.getWorkOrders().contains( workOrder ) );
		assertEquals( 1, restored.getWorkOrders().size() );
	}

	/**
	 * A workOrder recovers its vehicle
	 */
	@Test
	public void testWorkOrderRecoversVehicle() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( vehicle, restored.getVehicle() );
	}

}
