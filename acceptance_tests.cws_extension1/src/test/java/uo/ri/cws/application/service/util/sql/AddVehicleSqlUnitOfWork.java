package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;

public class AddVehicleSqlUnitOfWork {

    private VehicleDto dto;
    private ConnectionData connectionData;
    private PreparedStatement insertIntoVehicles;

    public AddVehicleSqlUnitOfWork ( VehicleDto dto ) {
	this.connectionData = PersistenceXmlScanner.scan();
	this.dto = dto;
    }

    public void execute ( ) {
	JdbcTransaction trx = new JdbcTransaction(connectionData);
	trx.execute( ( con ) -> {
	    prepareStatements(con);
	    insertVehicle();
	});
    }

    private static final String INSERT_INTO_TVEHICLES = "INSERT INTO TVEHICLES"
	    + " ( ID, PLATENUMBER, BRAND, MODEL, CLIENT_ID, VERSION )"
	    + " VALUES ( ?, ?, ?, ?, ?, ?)";

    private void insertVehicle ( ) throws SQLException {
	PreparedStatement st = insertIntoVehicles;
	int i = 1;
	st.setString(i++, dto.id);
	st.setString(i++, dto.plate);
	st.setString(i++, dto.make);
	st.setString(i++, dto.model);
	st.setString(i++, dto.clientId);
	st.setLong(i++, dto.version);

	st.executeUpdate();
    }

    private void prepareStatements ( Connection con ) throws SQLException {
	insertIntoVehicles = con.prepareStatement(INSERT_INTO_TVEHICLES);
    }

}
