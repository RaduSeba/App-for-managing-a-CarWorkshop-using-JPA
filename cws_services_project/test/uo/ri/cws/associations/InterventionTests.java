package uo.ri.cws.associations;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;


public class InterventionTests {
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private Vehicle vehicle;
	private VehicleType vehicleType;
	private Client client;

	@Before
	public void setUp() {
		client = new Client("dni-cliente", "nombre", "apellidos");
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		Associations.Own.link(client, vehicle );

		vehicleType = new VehicleType("coche", 50.0);
		Associations.Classify.link(vehicleType, vehicle);
		
		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");

		mechanic = new Mechanic("dni-mecanico", "nombre", "apellidos");
	
		intervention = new Intervention(mechanic, workOrder, 60);
	}
	
	@Test
	public void testWorkOrderInterventionLinked() {
		assertTrue( workOrder.getInterventions().contains( intervention ));
		assertTrue( intervention.getWorkOrder() == workOrder );
	}

	@Test
	public void testUnlinkOnInterventionByWorkOrder() {
		Associations.Intervene.unlink(intervention);
		
		assertTrue( ! workOrder.getInterventions().contains( intervention ));
		assertTrue( workOrder.getInterventions().size() == 0 );
		assertTrue( intervention.getWorkOrder() == null );
	}

	@Test
	public void testMechanicInterventionLinked() {
		assertTrue( mechanic.getInterventions().contains( intervention ));
		assertTrue( intervention.getMechanic() == mechanic );
	}

	@Test
	public void testUnlinkOnInterventionByMechanic() {
		Associations.Intervene.unlink(intervention);
		
		assertTrue( ! mechanic.getInterventions().contains( intervention ));
		assertTrue( mechanic.getInterventions().size() == 0 );
		assertTrue( intervention.getMechanic() == null );
	}

	@Test
	public void testSafeReturnOnMechanic() {
		Set<Intervention> intervenciones = mechanic.getInterventions();
		intervenciones.remove( intervention );

		assertTrue( intervenciones.size() == 0 );
		assertTrue( "It must be a copy of the collection or a read-only version", 
			mechanic.getInterventions().size() == 1
		);
	}

	@Test
	public void testSafeReturnOnWorkOrders() {
		Set<Intervention> intervenciones = workOrder.getInterventions();
		intervenciones.remove( intervention );

		assertTrue( intervenciones.size() == 0 );
		assertTrue( "It must be a copy of the collection or a read-only version", 
			workOrder.getInterventions().size() == 1
		);
	}



}
