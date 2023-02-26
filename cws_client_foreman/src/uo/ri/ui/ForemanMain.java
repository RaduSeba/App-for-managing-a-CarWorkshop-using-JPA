package uo.ri.ui;

import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessFactory;
import uo.ri.cws.infrastructure.persistence.jpa.executor.JpaExecutorFactory;
import uo.ri.cws.infrastructure.persistence.jpa.repository.JpaRepositoryFactory;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;
import uo.ri.ui.foreman.cliente.ClientsMenu;
import uo.ri.ui.foreman.reception.ReceptionMenu;
import uo.ri.ui.foreman.vehicle.VehiclesMenu;
import uo.ri.util.console.DefaultPrinter;
import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class ForemanMain {

	private static class MainMenu extends BaseMenu {
		MainMenu() {
			menuOptions = new Object[][] { 
				{ "Foreman", null },
				{ "Vehicle reception", 		ReceptionMenu.class }, 
				{ "Client management", 		ClientsMenu.class },
				{ "Vehicle management", 	VehiclesMenu.class },
				{ "Review client history", 	NotYetImplementedAction.class }, 
			};
		}
	}

	public static void main(String[] args) {
		new ForemanMain()
			.config()
			.run()
			.close();
	}

	private ForemanMain config() {
		Factory.service = new BusinessFactory();
		Factory.repository = new JpaRepositoryFactory();
		Factory.executor = new JpaExecutorFactory();

		return this;
	}

	public ForemanMain run() {
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
