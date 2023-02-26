package uo.ri.ui.foreman.vehicle;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class VehiclesMenu extends BaseMenu {

	public VehiclesMenu() {
		menuOptions = new Object[][] { 
			{ "Foreman > Vehicle management", null },

			{ "Register new vehicle", 	NotYetImplementedAction.class }, 
			{ "Update vehicle", 		NotYetImplementedAction.class }, 
			{ "Disable vehicle", 		NotYetImplementedAction.class }, 
			{ "List all vehicles", 		NotYetImplementedAction.class }, 
		};
	}

}
