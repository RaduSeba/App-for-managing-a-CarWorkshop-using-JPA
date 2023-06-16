package uo.ri.util.menu;

/**
 * Representa cada acción invocada por el usuario. 
 * 
 * 	Cada acción se encargará de la interacción con el usuario:
 * pantalla, teclado, listados y validaciones; e invocará a 
 * la capa de servicios.
 * 
 * @author alb
 */
public interface Action {
	void execute() throws Exception;
}
