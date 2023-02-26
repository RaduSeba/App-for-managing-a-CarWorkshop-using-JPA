package uo.ri.cws.infrastructure.persistence.jpa.util;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Jpa {

	private static volatile EntityManagerFactory emf = null;
	private static ThreadLocal<EntityManager> emThread = 
		new ThreadLocal<EntityManager>();
	
	public static EntityManager createEntityManager() {
		EntityManager entityManager = getEmf().createEntityManager();
		emThread.set(entityManager);
		return entityManager;
	}

	public static EntityManager getManager() {
		return emThread.get();
	}

	public synchronized static void close() {
		if (emf == null) return;
		if (emf.isOpen()) {
			emf.close();
		}
		emf = null;
	}

	private static EntityManagerFactory getEmf() {
		if (emf == null){
			// Avoids the remote possibility of multiple initialization in
			// a concurrent environment
			// emf field must be volatile for this to work
			// Broken under Java 1.4
			synchronized(Jpa.class) {
				if (emf == null) {  // double-check pattern 
					String name = loadPersistentUnitName();
					emf = Persistence.createEntityManagerFactory(name);
				}
			}
		}
		return emf;
	}

	private static String loadPersistentUnitName() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(Jpa.class.getResourceAsStream("/META-INF/persistence.xml"));

			doc.getDocumentElement().normalize();
			NodeList nl = doc.getElementsByTagName("persistence-unit");
			
			return ((Element)nl.item(0)).getAttribute("name");

		} catch (ParserConfigurationException e1) {
			throw new RuntimeException(e1);
		} catch (SAXException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
	}

}
