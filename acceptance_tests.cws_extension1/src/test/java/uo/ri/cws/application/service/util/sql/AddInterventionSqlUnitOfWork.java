package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.intervention.InterventionService.InterventionDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;

public class AddInterventionSqlUnitOfWork {

	private InterventionDto dto;
	private ConnectionData connectionData;
	private PreparedStatement insertIntoInterventions;

	public AddInterventionSqlUnitOfWork(InterventionDto dto2) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.dto = dto2;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertIntervention();
		});
	}
	
	private static final String INSERT_INTO_TINTERVENTIONS =
			"INSERT INTO TINTERVENTIONS"
				+ " ( ID, MECHANIC_ID, WORKORDER_ID, DATE, MINUTES, VERSION )"
				+ " VALUES (?, ?, ?, ?, ?, ?)";

	private void insertIntervention() throws SQLException {
		PreparedStatement st = insertIntoInterventions;
		int i = 1;
		st.setString(i++, dto.id);
		st.setString(i++, dto.mechanicId);
		st.setString(i++, dto.workorderId);
		st.setDate(i++, Date.valueOf(dto.date.toLocalDate()) );
		st.setInt(i++, dto.minutes);
		st.setLong(i++, dto.version);

		st.executeUpdate();
	}

	private void prepareStatements(Connection con) throws SQLException {
		insertIntoInterventions = con.prepareStatement(INSERT_INTO_TINTERVENTIONS);
	}

}
