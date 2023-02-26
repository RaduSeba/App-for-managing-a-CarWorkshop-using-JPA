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

import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.persistence.util.UnitOfWork;

public class InterventionMappingTests {

	private EntityManagerFactory factory;
	private UnitOfWork unitOfWork;

	private Vehicle vehicle;
	private WorkOrder workOrder;
	private Mechanic mechanic;
	private Intervention intervention;

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

		intervention = new Intervention(mechanic, workOrder, 60);

		unitOfWork.persist(vehicle, intervention, workOrder, mechanic);
	}

	@After
	public void tearDown() {
		unitOfWork.remove( vehicle, workOrder, mechanic, intervention );
		factory.close();
	}

	/**
	 * All fields of intervention are persisted properly
	 */
	@Test
	public void testAllFieldsPersisted() {

		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId() );

		assertEquals( intervention.getId(), restored.getId() );
		assertEquals( intervention.getMechanic(), restored.getMechanic() );
		assertEquals( intervention.getWorkOrder(), restored.getWorkOrder() );
		assertEquals( intervention.getMinutes(), restored.getMinutes() );
		assertEquals( intervention.getDate().truncatedTo(ChronoUnit.MILLIS),
				   restored.getDate().truncatedTo(ChronoUnit.MILLIS) 
			);
		assertEquals( intervention.getSubstitutions(), restored.getSubstitutions() );
	}

	/**
	 * When two interventions have the same mechanic, workOrder and date, then
	 * the second cannot be added
	 */
	@Test(expected=PersistenceException.class)
	public void testRepeated() {
		Intervention repeatedIntervention = new Intervention(
				intervention.getMechanic(),
				intervention.getWorkOrder(),
				intervention.getDate(),
				100
			);

		unitOfWork.persist( repeatedIntervention );
	}

}
