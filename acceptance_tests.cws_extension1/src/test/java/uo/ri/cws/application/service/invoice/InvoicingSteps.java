package uo.ri.cws.application.service.invoice;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.util.InvoiceUtil;
import uo.ri.cws.application.service.util.VehicleUtil;
import uo.ri.cws.application.service.util.WorkOrderUtil;
import uo.ri.cws.application.service.util.sql.FindWorkOrderSqlUnitOfWork;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;
import uo.ri.cws.domain.Invoice.InvoiceState;
import uo.ri.cws.domain.WorkOrder.WorkOrderState;
import uo.ri.util.math.Round;

public class InvoicingSteps {

	private TestContext ctx;

	private InvoicingService service = Factory.service.forCreateInvoiceService();
	private WorkOrderDto workorder;
	private List<WorkOrderDto> allWorkorders = new ArrayList<>();
	private List<WorkOrderDto> onlyFinishedWorkorders = new ArrayList<>();
	private List<String> workordersIds = new ArrayList<>();

	private List<InvoicingWorkOrderDto> invoicingWorkOrders = new ArrayList<InvoicingWorkOrderDto>();

	private InvoiceDto invoice;
	private ClientDto client;

	private VehicleDto vehicle;

	public InvoicingSteps(TestContext ctx) {
		this.ctx = ctx;
		client = (ClientDto) ctx.get(TestContext.Key.ACLIENT);
	}

	@Given("a vehicle")
	public void aVehicle() {
		vehicle = new VehicleUtil().withOwner(client.id).register().get();
	}

	@Given("one finished workorder")
	public void aListOfOneFinishedWorkorderId() {
		createWorkOrderWithState("FINISHED");
		onlyFinishedWorkorders.add(workorder);
	}

	private void createWorkOrderWithState(String state) {
		double leftLimit = 1D;
		double rightLimit = 100D;

		workorder = new WorkOrderUtil().forVehicle(vehicle.id).withState(state)
				.withAmount(randomAmount(leftLimit, rightLimit)).register().get();
		workordersIds.add(workorder.id);
		allWorkorders.add(workorder);
	}

	@Given("a list of several finished workorder ids")
	public void aListOfSeveralFinishedWorkorderIds() {
		int min = 2;
		int max = 5;
		int num = new Random().nextInt(max - min + 1) + min;

		for (int x = 0; x < num; x++) {
			createWorkOrderWithState("FINISHED");
			onlyFinishedWorkorders.add(workorder);

		}
		this.ctx.put(TestContext.Key.WORKORDERS, onlyFinishedWorkorders);

	}

	@Given("one non existent workorder")
	public void oneNonExistentWorkorder() {
		workordersIds.add("non-existing-workorderID");
	}

	@Given("one ASSIGNED workorder")
	public void oneASSIGNEDWorkorder() {
		createWorkOrderWithState("ASSIGNED");

	}

	@Given("one OPEN workorder")
	public void oneOPENWorkorder() {
		createWorkOrderWithState("OPEN");

	}

	@Given("one INVOICED workorder")
	public void oneINVOICEDWorkorder() {
		createWorkOrderWithState("INVOICED");

	}

	@Given("a null id")
	public void aNullId() {
		workordersIds.add(null);
	}

	@Given("an empty id")
	public void anEmtpyId() {
		workordersIds.add(" ");
	}

	@When("I try to create an invoice")
	public void iTryToCreateAnInvoice() {
		tryCreateAndKeepException();

	}

	@When("I try to create an invoice for a null list")
	public void iTryToCreateAnInvoiceForANullList() {
		this.workordersIds = null;
		tryCreateAndKeepException();

	}

	@When("I try to create an invoice for an empty list")
	public void iTryToCreateAnInvoiceForAnEmptyList() {
		tryCreateAndKeepException();
	}

	@When("I try to create an invoice for a wrong argument")
	public void iTryToCreateAnInvoiceForAWrongArgument() {
		tryCreateAndKeepException();

	}

	@When("I try to create an invoice for a list and one of the strings is empty")
	public void iTryToCreateAnInvoiceForAListAndOneOfTheStringsIsEmpty() {
		int min = 2;
		int max = 5;
		int num = new Random().nextInt(max - min + 1) + min;

		for (int x = 0; x < num; x++) {
			createWorkOrderWithState("FINISHED");
		}
		workordersIds.add("");
	}

//    @When("I try to find workorders for a non existent dni")
//    public void iTryToFindWorkordersForANonExistentDni ( ) {
//	tryFindAndKeepException("non-existent-client");
//    }
//
//    @When("I try to find workorders with null dni")
//    public void iTryToFindWorkordersWithNullDni ( ) {
//	tryFindAndKeepException(null);
//
//    }
//
//    @When("I look for a workorder by empty dni")
//    public void iLookForAWorkorderByEmptyDni ( ) {
//	tryFindAndKeepException(" ");
//
//    }

//    private void tryFindAndKeepException ( String arg ) {
//	try {
//	    service.findNotInvoicedWorkOrdersByClientDni(arg);
//	    fail();
//	} catch (BusinessException ex) {
//	    ctx.setException(ex);
//	} catch (IllegalArgumentException ex) {
//	    ctx.setException(ex);
//	}
//
//    }

	private void tryCreateAndKeepException() {
		try {
			service.createInvoiceFor(workordersIds);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}

	}

	private double randomAmount(double leftLimit, double rightLimit) {

		double generatedDouble = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
		return generatedDouble;
	}

	@SuppressWarnings("unchecked")
	@When("I create an invoice for the workorders")
	public void iCreateAnInvoiceForTheWorkorders() throws BusinessException {
		if (workordersIds.isEmpty()) {
			workordersIds = ((List<WorkOrderDto>) this.ctx.get(TestContext.Key.WORKORDERS)).stream()
					.filter(wo -> wo.state.equals("FINISHED")).map(wo -> wo.id).collect(Collectors.toList());
		}
		invoice = Factory.service.forCreateInvoiceService().createInvoiceFor(workordersIds);
	}

//    @When("I search not invoiced workorders by dni")
//    public void iSearchNotInvoicedWorkordersByDni ( ) throws BusinessException {
//
//	this.invoicingWorkOrders = Factory.service.forCreateInvoiceService()
//		.findNotInvoicedWorkOrdersByClientDni(this.client.dni);
//
//    }

	@Then("I get only finished workorders")
	public void iGetOnlyFinishedWorkorders() {
		assertTrue(this.invoicingWorkOrders.size() == this.onlyFinishedWorkorders.size());
		/* ids match */
		Map<String, WorkOrderDto> map = this.onlyFinishedWorkorders.stream()
				.collect(Collectors.toMap(dto -> dto.id, dto -> dto));
		List<InvoicingWorkOrderDto> hasDiffId = invoicingWorkOrders.stream().filter(s -> !map.get(s.id).id.equals(s.id))
				.collect(Collectors.toList());
		assertTrue(hasDiffId.isEmpty());
	}

	@Then("I get an empty list")
	public void iGetAnEmptyList() {
		assertTrue(this.invoicingWorkOrders.isEmpty());
	}

	@Then("an invoice is created")
	public void anInvoiceIsCreated() {
		/* invoice is created */
		InvoiceDto found = new InvoiceUtil().find(invoice.id).get();
		assertTrue(found != null);

		InvoiceDto expectedInvoice = createExpectedInvoice(this.allWorkorders);
		/* with proper number */
		/* with proper amount */
		assertTrue(found.total == expectedInvoice.total);

		/* with proper state */
		assertTrue(found.state.equals(expectedInvoice.state));

		/* workorders are updated */
		List<WorkOrderDto> updated = new ArrayList<>();
		for (String id : workordersIds) {
			FindWorkOrderSqlUnitOfWork find = new FindWorkOrderSqlUnitOfWork(id);
			find.execute();
			updated.add(find.get());
		}

		/* to proper state */
		assertTrue(updated.stream().allMatch(wo -> wo.state.equals(WorkOrderState.INVOICED.toString())));

		/* with proper invoice_id */
		assertTrue(updated.stream().allMatch(wo -> wo.invoiceId.equals(this.invoice.id)));

	}

	private InvoiceDto createExpectedInvoice(List<WorkOrderDto> workorders) {
		InvoiceDto result = new InvoiceDto();

		double total = calculateTotal(workorders);

		result.total = Round.twoCents( total );
		result.state = InvoiceState.NOT_YET_PAID.toString();
		return result;
	}

	private double calculateTotal(List<WorkOrderDto> lst) {
		double VAT = 0.21;
		double totalAmount = lst.stream().map(wo -> wo.total).collect(Collectors.summingDouble(Double::doubleValue));
		double total = totalAmount * (1 + VAT); // vat included
		return total;
	}

}
