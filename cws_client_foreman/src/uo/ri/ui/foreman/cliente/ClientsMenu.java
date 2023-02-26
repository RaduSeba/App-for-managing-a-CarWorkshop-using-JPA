package uo.ri.ui.foreman.cliente;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class ClientsMenu extends BaseMenu {

	public ClientsMenu() {
		menuOptions = new Object[][] { 
			{ "Foreman > Client management", null },

			{ "Register a client", 	NotYetImplementedAction.class }, 
			{ "Update a client", 	NotYetImplementedAction.class }, 
			{ "Disable a client", 	NotYetImplementedAction.class }, 
			{ "List all clients", 	NotYetImplementedAction.class }, 
		};
	}

}
