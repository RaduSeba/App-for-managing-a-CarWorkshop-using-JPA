package uo.ri.cws.associations;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;


public class AssignTests {
	private Mechanic mechanic;
	private WorkOrder workOrder;
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
		Associations.Assign.link(mechanic, workOrder);
	}
	
	@Test
	public void testLinkOnAssign() {
		assertTrue( mechanic.getAssigned().contains( workOrder ));
		assertTrue( workOrder.getMechanic() == mechanic );
	}

	@Test
	public void testUnlinkOnAssign() {
		Associations.Assign.unlink(mechanic, workOrder );
		
		assertTrue( ! mechanic.getAssigned().contains( workOrder ));
		assertTrue( mechanic.getAssigned().size() == 0 );
		assertTrue( workOrder.getMechanic() == null );
	}

	@Test
	public void testSafeReturn() {
		Set<WorkOrder> assigned = mechanic.getAssigned();
		assigned.remove( workOrder );

		assertTrue( assigned.size() == 0 );
		assertTrue( "It must be a copy of the collection", 
			mechanic.getAssigned().size() == 1
		);
	}

}
