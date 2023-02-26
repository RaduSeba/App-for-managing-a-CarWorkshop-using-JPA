package uo.ri.cws.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class InterventionTest {

	private Mechanic mechanic;
	private WorkOrder workOrder;
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
	}

	/**
	 * An intervention without no time nor substitutions amount 0.0 €
	 */
	@Test
	public void testAmountsZero() {
		Intervention i = new Intervention(mechanic, workOrder, 0);

		assertTrue( i.getAmount() == 0.0 );
	}

	/**
	 * An intervention with 60 minutes amounts the price of an labor hour
	 */
	@Test
	public void testAmountOneHour() {
		Intervention i = new Intervention(mechanic, workOrder, 60);

		assertTrue( i.getAmount() == vehicleType.getPricePerHour() );
	}

	/**
	 * An intervention with just one sparepart amounts the price of it
	 */
	@Test
	public void testImporteRepuesto() {
		Intervention i = new Intervention(mechanic, workOrder, 0);
		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(r, i, 1);

		assertTrue( i.getAmount() == r.getPrice() );
	}

	/**
	 * An intervention with time and spare parts returns the right amount
	 */
	@Test
	public void testImporteIntervencionCompleta() {
		Intervention i = new Intervention(mechanic, workOrder, 60);

		SparePart r = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(r, i, 2);

		final double TOTAL =
					   50.0  // 60 mins * 50 €/hour for the vehicle type
				+ 2 * 100.0; // 2 spare parts sold at 100.0 €

		assertTrue( i.getAmount() == TOTAL );
	}

}
