package uo.ri.cws.persistence.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

public class AssignMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Mechanic mechanic;
	private Vehicle vehicle;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("plate-1010", "make", "model" );
		vehicle = new Vehicle("1234-Z", "make", "model");
		workOrder = new WorkOrder( vehicle );

		Associations.Assign.link(mechanic, workOrder);

		unitOfWork.persist(workOrder, mechanic, vehicle);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( workOrder, mechanic, vehicle );
		factory.close();
	}

	/**
	 * A mechanic recovers its work orders
	 */
	@Test
	public void testMechanicRecoversWorkOrders() {

		Mechanic restored = unitOfWork.findById( Mechanic.class, mechanic.getId() );

		assertTrue( restored.getAssigned().contains( workOrder ) );
		assertEquals( 1, restored.getAssigned().size() );
	}

	/**
	 * A workOrder recovers its mechanic
	 */
	@Test
	public void testWorkOrderRecoversMechanic() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( mechanic, restored.getMechanic() );
	}

}
