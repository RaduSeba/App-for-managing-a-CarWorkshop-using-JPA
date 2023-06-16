package uo.ri.util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Jdbc {

	private static String URL = "jdbc:hsqldb:hsql://localhost";
	private static String USER = "sa";
	private static String PASS = "";
	
//	static {
//		try {
//			Class.forName( DRIVER );
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Driver not found in classpath", e);
//		}
//	}
	
//	public static Connection getConnection() throws SQLException {
//		return DriverManager.getConnection(URL, USER, PASS);
//	}

//	public static void close(ResultSet rs, Statement st, Connection c) {
//		close(rs);
//		close(st);
//		close(c);
//	}
//
//	public static void close(ResultSet rs, Statement st) {
//		close(rs);
//		close(st);
//	}
//
//	protected static void close(ResultSet rs) {
//		if (rs != null) try { rs.close(); } catch(SQLException e) {/* ignore */}
//	}
//
//	public static void close(Statement st) {
//		if (st != null ) try { st.close(); } catch(SQLException e) {/* ignore */}
//	}
//
//	public static void close(Connection c) {
//		if (c != null) try { c.close(); } catch(SQLException e) {/* ignore */}
//	}

	public static Connection createThreadConnection() throws SQLException {
		Connection con = DriverManager.getConnection(URL, USER, PASS);
		threadConnection.set(con);
		return con;
	}

	private static ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();

	public static Connection getCurrentConnection() {
		return threadConnection.get();
	}

}
