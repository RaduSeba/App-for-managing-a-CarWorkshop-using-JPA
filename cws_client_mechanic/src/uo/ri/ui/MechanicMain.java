package uo.ri.ui;

import uo.ri.util.console.DefaultPrinter;
import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class MechanicMain {

	private static class MainMenu extends BaseMenu {
		MainMenu() {
			menuOptions = new Object[][] { 
				{ "Mechanic", null },
				{ "List assigned work orders", 		NotYetImplementedAction.class }, 
				{ "Add parts to work order", 		NotYetImplementedAction.class },
				{ "Remove parts from work order", 	NotYetImplementedAction.class },
				{ "Close a work order", 			NotYetImplementedAction.class },
			};
		}
	}

	private MainMenu menu = new MainMenu();
	
	public static void main(String[] args) {
		new MechanicMain()
			.config()
			.run()
			.close();
	}

	private MechanicMain config() {
		return this;
	}

	public MechanicMain run() {
		try {
			menu.execute();

		} catch (RuntimeException rte) {
			DefaultPrinter.printRuntimeException(rte);
		}
		return this;
	}

	private void close() {
	}

}
