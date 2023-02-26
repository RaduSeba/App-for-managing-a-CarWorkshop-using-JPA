package uo.ri.cws.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class SubstitutionTests {

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
		Associations.Own.link(client, vehicle);

		vehicleType = new VehicleType("coche", 50.0);
		Associations.Classify.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic("dni-mecanico", "nombre", "apellidos");

		intervention = new Intervention(mechanic, workOrder, 0);
	}

	/**
	 * A substitution with 0 spare parts throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSustitutionThrowsExceptionIfZero() {
		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(r, intervention, 0);
	}

	/**
	 * A substitution with negative spare parts throws IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSustitutionThrowsExceptionIfNegative() {
		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(r, intervention, -1);
	}

	/**
	 * It computes the right amount of a substitution
	 */
	@Test
	public void testSubstitutionAmount() {
		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		Substitution s = new Substitution(r, intervention, 2);

		assertTrue( s.getAmount() == 200.0 );
	}

}
