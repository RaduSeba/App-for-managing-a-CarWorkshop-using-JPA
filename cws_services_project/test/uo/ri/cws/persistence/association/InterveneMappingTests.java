package uo.ri.cws.persistence.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

public class InterveneMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private Invoice invoice;
	private Mechanic mechanic;
	private Intervention intervention;
	private VehicleType vehicleType;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("mechanic-dni");
		vehicleType = new VehicleType("vehicle-type", 50);
		vehicle = new Vehicle("plate-1010", "make", "model" );
		Associations.Classify.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		intervention = new Intervention(mechanic, workOrder, 60);
		workOrder.markAsFinished();

		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType);
	}

	@After
	public void tearDown() {
		unitOfWork.remove(
				workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType
			);
		factory.close();
	}

	/**
	 * A work order recovers its interventions
	 */
	@Test
	public void testWorkOrderRecoversInterventions() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertTrue( restored.getInterventions().contains( intervention ) );
		assertEquals( 1, restored.getInterventions().size() );
	}

	/**
	 * A mechanic recovers its interventions
	 */
	@Test
	public void testMechanicRecoversInterventions() {

		Mechanic restored = unitOfWork.findById( Mechanic.class, mechanic.getId() );

		assertTrue( restored.getInterventions().contains( intervention ) );
		assertEquals( 1, restored.getInterventions().size() );
	}

	/**
	 * An intervention recovers its work order
	 */
	@Test
	public void testInterventionRecoversWorkOrder() {

		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId() );

		assertEquals( workOrder, restored.getWorkOrder() );
	}

	/**
	 * An intervention recovers its mechanic
	 */
	@Test
	public void testInterventionRecoversMechanic() {

		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId() );

		assertEquals( mechanic, restored.getMechanic() );
	}

}
