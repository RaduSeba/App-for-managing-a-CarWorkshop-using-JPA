package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;


public class FindProfessionalGroupByIdSqlUnitOfWork {

	private String id;
	private ConnectionData connectionData;
	private PreparedStatement find;
	private static final String FIND_PROFESSIONAL_GROUP_BY_ID =
			"SELECT * FROM TPROFESSIONALGROUPS"
			+ " WHERE ID = ?"
			;

	private ProfessionalGroupBLDto result = null;

	
	public FindProfessionalGroupByIdSqlUnitOfWork(String id) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.id = id;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findProfessionalGroup();
		});
	}
	
	public ProfessionalGroupBLDto get() {
		return result;
	}

	private void findProfessionalGroup() throws SQLException {
		PreparedStatement st = find;

		int i = 1;
		st.setString(i++, id);

		ResultSet rs = st.executeQuery();
		
		if ( rs.next() ) {
			result = new ProfessionalGroupBLDto();
			result.id = rs.getString("id");
			result.version = rs.getLong("version");
			result.productivityRate = rs.getDouble("productivityBonusPercentage");
			result.name = rs.getString("name");
			result.trieniumSalary = rs.getDouble("trienniumPayment");
		}
	}

	private void prepareStatements(Connection con) throws SQLException {
		find = con.prepareStatement(FIND_PROFESSIONAL_GROUP_BY_ID);
	}

}
