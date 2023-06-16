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
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Substitution;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

public class SubstituteMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private Invoice invoice;
	private Mechanic mechanic;
	private Intervention intervention;
	private VehicleType vehicleType;
	private SparePart sparePart;
	private Substitution substitution;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic("mechanic-dni");
		vehicleType = new VehicleType("vehicle-type", 50);
		vehicle = new Vehicle("plate-1010", "make", "model" );
		Associations.Classify.link(vehicleType, vehicle);

		sparePart = new SparePart("code", "description", 100);

		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		intervention = new Intervention(mechanic, workOrder, 60);
		substitution = new Substitution(sparePart, intervention, 1);
		workOrder.markAsFinished();

		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType,
				substitution, sparePart);
	}

	@After
	public void tearDown() {
		unitOfWork.remove(
				workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType,
				substitution, sparePart
			);
		factory.close();
	}

	/**
	 * A spare part recovers its substitutions
	 */
	@Test
	public void testSparePartRecoversSubstitutions() {

		SparePart restored = unitOfWork.findById( SparePart.class, sparePart.getId() );

		assertTrue( restored.getSustitutions().contains( substitution ) );
		assertEquals( 1, restored.getSustitutions().size() );
	}

	/**
	 * A intervention recovers its substitutions
	 */
	@Test
	public void testInterventionRecoversSubstitutions() {

		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId()
			);

		assertTrue( restored.getSubstitutions().contains( substitution ) );
		assertEquals( 1, restored.getSubstitutions().size() );
	}

	/**
	 * An substitution recovers its intervention
	 */
	@Test
	public void testSubstitutionRecovrsItsInterventions() {

		Substitution restored = unitOfWork.findById( Substitution.class,
				substitution.getId() );

		assertEquals( intervention, restored.getIntervention() );
	}

	/**
	 * An substitution recovers its spare part
	 */
	@Test
	public void testSubstitutionRecoversSparePart() {

		Substitution restored = unitOfWork.findById( Substitution.class,
				substitution.getId() );

		assertEquals( sparePart, restored.getSparePart() );
	}

}
