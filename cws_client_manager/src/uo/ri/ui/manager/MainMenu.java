package uo.ri.ui.manager;

import uo.ri.ui.manager.mechanic.MechanicsMenu;
import uo.ri.ui.manager.sparepart.SparepartsMenu;
import uo.ri.ui.manager.vehicletype.VehicleTypesMenu;
import uo.ri.util.menu.BaseMenu;

public class MainMenu extends BaseMenu {{
		menuOptions = new Object[][] {
			{ "Manager", null },

			{ "Mechanics management", 		MechanicsMenu.class },
			{ "Spareparts management", 		SparepartsMenu.class },
			{ "Vehicle types management", 	VehicleTypesMenu.class },
		};
}}