package uo.ri.cws.application.service.util.db;

public class PersistenceXmlScanner {

	public static ConnectionData scan() {
		ConnectionData res = new ConnectionData();
		res.driver = "org.hsqldb.jdbcDriver";
		res.pass = "";
		res.url = "jdbc:hsqldb:hsql://localhost";
		res.user = "sa";
		return res;

	}

}
