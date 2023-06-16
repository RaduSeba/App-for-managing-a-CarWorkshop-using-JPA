package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;

public class FindMechanicByIdSqlUnitOfWork {

    private String id;
    private ConnectionData connectionData;
    private PreparedStatement find;
    private static final String FIND_MECHANIC_BY_ID = "SELECT * FROM TMECHANICS"
	    + " WHERE ID = ?";

    private MechanicDto result = null;

    public FindMechanicByIdSqlUnitOfWork ( String id ) {
	this.connectionData = PersistenceXmlScanner.scan();
	this.id = id;
    }

    public void execute ( ) {
	JdbcTransaction trx = new JdbcTransaction(connectionData);
	trx.execute( ( con ) -> {
	    prepareStatements(con);
	    findMechanic();
	});
    }

    private void findMechanic ( ) throws SQLException {
	PreparedStatement st = find;

	int i = 1;
	st.setString(i++, id);

	ResultSet rs = st.executeQuery();

	if ( rs.next() ) {
	    result = new MechanicDto();
	    result.id = rs.getString("id");
	    result.version = rs.getLong("version");
	    result.dni = rs.getString("dni");
	    result.name = rs.getString("name");
	    result.surname = rs.getString("surname");

	}
    }

    private void prepareStatements ( Connection con ) throws SQLException {
	find = con.prepareStatement(FIND_MECHANIC_BY_ID);
    }

    public MechanicDto get ( ) {
	return this.result;
    }
}
