package uo.ri.cws.associations;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Charge;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Substitution;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;


public class PayTests {
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private SparePart sparePart;
	private Vehicle vehicle;
	private VehicleType vehicleType;
	private Client client;
	private Invoice invoice;
	private Cash cash;
	private Charge charge;

	@Before
	public void setUp() {
		client = new Client("dni-cliente", "nombre", "apellidos");
		vehicle = new Vehicle("1234 GJI", "seat", "ibiza");
		Associations.Own.link(client, vehicle );

		vehicleType = new VehicleType("coche", 50.0);
		Associations.Classify.link(vehicleType, vehicle);
		
		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic("dni-mecanico", "nombre", "apellidos");
		workOrder.assignTo(mechanic);
	
		intervention = new Intervention(mechanic, workOrder, 60);
		
		sparePart = new SparePart("R1001", "junta la trocla", 100.0);
		new Substitution(sparePart, intervention, 2);
		
		workOrder.markAsFinished();

		invoice = new Invoice(0L, LocalDate.now() );
		invoice.addWorkOrder(workOrder);
		
		cash = new Cash( client );
		charge = new Charge(invoice, cash, 100.0);
	}
	
	@Test
	public void testClientChargeLinked() {
		assertTrue( client.getPaymentMeans().contains( cash ));
		assertTrue( cash.getClient() == client );
	}

	@Test
	public void testClientCahsUnlinked() {
		Associations.Pay.unlink(client, cash);
		
		assertTrue( ! client.getPaymentMeans().contains( cash ));
		assertTrue( client.getPaymentMeans().size() == 0 );
		assertTrue( cash.getClient() == null );
	}

	@Test
	public void testInvoiceChargeLinked() {
		assertTrue( cash.getCharges().contains( charge ));
		assertTrue( invoice.getCharges().contains( charge ));
		
		assertTrue( charge.getInvoice() == invoice );
		assertTrue( charge.getPaymentMean() == cash );
		
		assertTrue( cash.getAccumulated() == 100.0 );
	}

	@Test
	public void testInvoiceCashUnlinked() {
		Associations.ToCharge.unlink( charge );
		
		assertTrue( ! cash.getCharges().contains( charge ));
		assertTrue( cash.getCharges().size() == 0 );

		assertTrue( ! invoice.getCharges().contains( charge ));
		assertTrue( cash.getCharges().size() == 0 );
		
		assertTrue( charge.getInvoice() == null );
		assertTrue( charge.getPaymentMean() == null );
	}

}
