package uo.ri.ui.manager.mechanic;

import uo.ri.ui.manager.mechanic.action.AddMechanicAction;
import uo.ri.ui.manager.mechanic.action.DeleteMechanicAction;
import uo.ri.ui.manager.mechanic.action.ListMechanicsAction;
import uo.ri.ui.manager.mechanic.action.UpdateMechanicAction;
import uo.ri.util.menu.BaseMenu;

public class MechanicsMenu extends BaseMenu {

	public MechanicsMenu() {
		menuOptions = new Object[][] { 
			{"Manager > Mechanics management", null},
			
			{ "Add mechanic", 		AddMechanicAction.class }, 
			{ "Update mechanic", 	UpdateMechanicAction.class }, 
			{ "Disable mechanic", 	DeleteMechanicAction.class }, 
			{ "List mechanics", 	ListMechanicsAction.class },
		};
	}

}
