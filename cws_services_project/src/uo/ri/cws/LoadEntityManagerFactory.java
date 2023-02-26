package uo.ri.cws;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Fuerza a que se cargen los parámetros de configuración,
 * se analizen todos los mapeos y, si procede, se crea la BDD
 */
public class LoadEntityManagerFactory {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("carworkshop");
		
		emf.createEntityManager().close();
		emf.close();
		
		System.out.println("--> Si no hay excepciones todo va bien");
		System.out.println("\n\t (O no hay ninguna clase mapeada)");
	}

}
