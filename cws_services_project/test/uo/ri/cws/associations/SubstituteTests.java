package uo.ri.cws.associations;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Substitution;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;


public class SubstituteTests {
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private SparePart sparePart;
	private Substitution sustitution;
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
		
		sparePart = new SparePart("R1001", "junta la trocla", 100.0);
		sustitution = new Substitution(sparePart, intervention, 2);
	}
	
	@Test
	public void testSustitutionInterventionLinked() {
		assertTrue( sustitution.getIntervention().equals( intervention ));
		assertTrue( sustitution.getSparePart().equals( sparePart ));
		
		assertTrue( sparePart.getSubstitutions().contains( sustitution ));
		assertTrue( intervention.getSubstitutions().contains( sustitution ));
	}

	@Test
	public void testSustitutionSparePartUnlinked() {
		Associations.Substitute.unlink( sustitution );
		
		assertTrue( sustitution.getIntervention() == null);
		assertTrue( sustitution.getSparePart() == null);
		
		assertTrue( ! sparePart.getSubstitutions().contains( sustitution ));
		assertTrue( sparePart.getSubstitutions().size() == 0 );

		assertTrue( ! intervention.getSubstitutions().contains( sustitution ));
		assertTrue( intervention.getSubstitutions().size() == 0 );
	}

	@Test
	public void testSafeReturnOnIntervention() {
		Set<Substitution> sustituciones = intervention.getSubstitutions();
		sustituciones.remove( sustitution );

		assertTrue( sustituciones.size() == 0 );
		assertTrue( "It must be a copy of the collection or a read-only version", 
			intervention.getSubstitutions().size() == 1
		);
	}

	@Test
	public void testSafeReturnOnSparePart() {
		Set<Substitution> sustituciones = sparePart.getSubstitutions();
		sustituciones.remove( sustitution );

		assertTrue( sustituciones.size() == 0 );
		assertTrue( "It must be a copy of the collection or a read-only version", 
			sparePart.getSubstitutions().size() == 1
		);
	}

}
