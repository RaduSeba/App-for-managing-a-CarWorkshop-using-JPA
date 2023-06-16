package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;

public class AddProfessionalGroupSqlUnitOfWork {

	private ProfessionalGroupBLDto dto;
	private ConnectionData connectionData;
	private PreparedStatement insertIntoWorkOrders;
	private static final String INSERT_INTO_TPROFESSIONALGROUPS =
			"INSERT INTO TPROFESSIONALGROUPS"
			+ " ( ID, VERSION, NAME, TRIENNIUMPAYMENT, PRODUCTIVITYBONUSPERCENTAGE )"
			+ " VALUES ( ?, ?, ?, ?, ?)";

	public AddProfessionalGroupSqlUnitOfWork(ProfessionalGroupBLDto dto) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.dto = dto;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertWorkOrder();
		});
	}

	private void insertWorkOrder() throws SQLException {
		PreparedStatement st = insertIntoWorkOrders;
		int i = 1;
		st.setString(i++, dto.id);
		st.setLong(i++, dto.version);
		st.setString(i++, dto.name);
		st.setDouble(i++, dto.trieniumSalary);
		st.setDouble(i++, dto.productivityRate);

		st.executeUpdate();
	}

	private void prepareStatements(Connection con) throws SQLException {
		insertIntoWorkOrders = con.prepareStatement(INSERT_INTO_TPROFESSIONALGROUPS);
	}

}
