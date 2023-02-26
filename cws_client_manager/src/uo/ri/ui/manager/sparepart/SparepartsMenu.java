package uo.ri.ui.manager.sparepart;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class SparepartsMenu extends BaseMenu {

	public SparepartsMenu() {
		menuOptions = new Object[][] { 
			{"Manager > Parts management", null},
			
			{ "Add", 		NotYetImplementedAction.class }, 
			{ "Update", 	NotYetImplementedAction.class }, 
			{ "Remove", 	NotYetImplementedAction.class }, 
			{ "List all", 	NotYetImplementedAction.class },
		};
	}

}
