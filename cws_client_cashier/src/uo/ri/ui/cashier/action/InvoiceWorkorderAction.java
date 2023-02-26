package uo.ri.ui.cashier.action;

import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.menu.Action;

public class InvoiceWorkorderAction implements Action {

	@Override
	public void execute() throws BusinessException {
		List<String> workOrderIds = new ArrayList<>();

		// Ask the user the work order ids
		do {
			String id = Console.readString("Workorder id");
			workOrderIds.add(id);
		} while ( moreWorkOrders() );

		InvoicingService cs = Factory.service.forCreateInvoiceService();
		InvoiceDto invoice = cs.createInvoiceFor(workOrderIds);

		Printer.printInvoice( invoice );
	}

	private boolean moreWorkOrders() {
		return Console
				.readString("more work orders? (y/n) ")
				.equalsIgnoreCase("y");
	}

}
