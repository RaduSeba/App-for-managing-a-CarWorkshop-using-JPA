package uo.ri.cws.application.service.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTransaction {

	public interface Command {
		void execute(Connection con) throws SQLException;
	}

	private ConnectionData connectionData;

	public JdbcTransaction(ConnectionData connectionData) {
		this.connectionData = connectionData;
	}

	public void execute(Command cmd) {
		Connection con = createConnection(connectionData);
		try {
			try {

				cmd.execute(con);
				con.commit();

			} catch (Exception e) {
				con.rollback();
				throw e;
			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Connection createConnection(ConnectionData connectionData) {
		try {
			Connection con = DriverManager.getConnection(
					connectionData.url,
					connectionData.user,
					connectionData.pass
				);
			con.setAutoCommit( false );
			return con;
		} catch (SQLException e) {
			throw new RuntimeException("Database connection trouble", e);
		}
	}

}
