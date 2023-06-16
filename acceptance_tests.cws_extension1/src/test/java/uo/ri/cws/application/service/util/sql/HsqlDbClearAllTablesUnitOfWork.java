package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;

public class HsqlDbClearAllTablesUnitOfWork {
	private ConnectionData connectionData = PersistenceXmlScanner.scan();
	private PreparedStatement clearAllTables, insertDefaultContractTypes, insertDefaultProfessionalGroups;

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			clearAllTables();
			insertDefaultProfessionalGroups();
			insertDefaultContractTypes();
		});
	}


	private void insertDefaultProfessionalGroups() throws SQLException {
		PreparedStatement st = insertDefaultProfessionalGroups;
		st.setString(1, UUID.randomUUID().toString());
		st.setString(2, UUID.randomUUID().toString());
		st.setString(3, UUID.randomUUID().toString());
		st.setString(4, UUID.randomUUID().toString());
		st.setString(5, UUID.randomUUID().toString());
		st.setString(6, UUID.randomUUID().toString());			
		st.setString(7, UUID.randomUUID().toString());		
		st.executeUpdate();	
	}

	private void insertDefaultContractTypes() throws SQLException {
		PreparedStatement st = insertDefaultContractTypes;
		st.setString(1, UUID.randomUUID().toString());
		st.setString(2, UUID.randomUUID().toString());
		st.setString(3, UUID.randomUUID().toString());
	
		st.executeUpdate();		
	}

	private static final String CLEAR_ALL_TABLES =
			"TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK";
	
	private static final String INSERT_DEFAULT_CONTRACT_TYPES = "INSERT INTO TCONTRACTTYPES"
			+ " (ID, VERSION, NAME, COMPENSATIONDAYS)"
		    + " VALUES"
			+ " (?,1,'FIXED_TERM',4.2E0),"
			+ " (?,1,'PERMANENT',1.35E0),"	
			+ " (?,1,'TEMPORARY',3.25E0)"
			;
	private static final String INSERT_DEFAULT_PROFESSIONAL_GROUPS = "INSERT INTO TPROFESSIONALGROUPS"
			+ "(ID, VERSION, NAME, TRIENNIUMPAYMENT, PRODUCTIVITYBONUSPERCENTAGE)"
			+ " VALUES"
			+ " (?,1,'I',46.74E0,5.0E0),"
			+ " (?,1,'II',38.12E0,4.5E0),"
			+ " (?,1,'III',33.44E0,3.0E0),"
			+ " (?,1,'IV',28.85E0,3.5E0),"
			+ " (?,1,'V',19.64E0,2.5E0),"
			+ " (?,1,'VI',14.78E0,2.0E0),"
			+ " (?,1,'VII',11.25E0,1.5E0)";

	private void clearAllTables() throws SQLException {
		PreparedStatement st = clearAllTables;
		st.executeUpdate();
	}

	private void prepareStatements(Connection con) throws SQLException {
		clearAllTables = con.prepareStatement( CLEAR_ALL_TABLES );
		insertDefaultContractTypes = con.prepareStatement( INSERT_DEFAULT_CONTRACT_TYPES );
		insertDefaultProfessionalGroups = con.prepareStatement( INSERT_DEFAULT_PROFESSIONAL_GROUPS );
	}

}
