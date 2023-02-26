package uo.ri.ui;

import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessFactory;
import uo.ri.cws.infrastructure.persistence.jpa.executor.JpaExecutorFactory;
import uo.ri.cws.infrastructure.persistence.jpa.repository.JpaRepositoryFactory;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;
import uo.ri.ui.cashier.action.FindNotInvoicedWorkOrders;
import uo.ri.ui.cashier.action.InvoiceWorkorderAction;
import uo.ri.util.console.DefaultPrinter;
import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class CashierMain {

	private static class MainMenu extends BaseMenu {
		MainMenu() {
			menuOptions = new Object[][] { 
				{ "Cash", null },
				{ "Find not invoiced work orders", 	FindNotInvoicedWorkOrders.class }, 
				{ "Find work order by plate", 		NotYetImplementedAction.class }, 
				{ "Inovice work orders", 			InvoiceWorkorderAction.class },
				{ "Liquidate invoice", 				NotYetImplementedAction.class },
			};
		}
	}

	public static void main(String[] args) {
		new CashierMain()
				.config()
				.run()
				.close();
	}

	private CashierMain config() {
		Factory.service = new BusinessFactory();
		Factory.repository = new JpaRepositoryFactory();
		Factory.executor = new JpaExecutorFactory();

		return this;
	}
	
	public CashierMain run() {
		try {
			new MainMenu().execute();

		} catch (RuntimeException rte) {
			DefaultPrinter.printRuntimeException(rte);
		}
		return this;
	}

	private void close() {
		Jpa.close();
	}

}
