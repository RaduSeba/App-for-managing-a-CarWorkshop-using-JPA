package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;

public class AddClientSqlUnitOfWork {

    private ClientDto dto;
    private ConnectionData connectionData;
    private PreparedStatement insertIntoClients;

    public AddClientSqlUnitOfWork ( ClientDto dto ) {
	this.connectionData = PersistenceXmlScanner.scan();
	this.dto = dto;
    }

    public void execute ( ) {
	JdbcTransaction trx = new JdbcTransaction(connectionData);
	trx.execute( ( con ) -> {
	    prepareStatements(con);
	    insertClient();
	});
    }

    private static final String INSERT_INTO_TCLIENTS = "INSERT INTO TCLIENTS"
	    + " ( ID, DNI, NAME, SURNAME, EMAIL, PHONE, CITY, STREET, ZIPCODE, VERSION )"
	    + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private void insertClient ( ) throws SQLException {
	PreparedStatement st = insertIntoClients;
	int i = 1;
	st.setString(i++, dto.id);
	st.setString(i++, dto.dni);
	st.setString(i++, dto.name);
	st.setString(i++, dto.surname);
	st.setString(i++, dto.email);
	st.setString(i++, dto.phone);
	st.setString(i++, dto.addressCity);
	st.setString(i++, dto.addressStreet);
	st.setString(i++, dto.addressZipcode);
	st.setLong(i++, dto.version);

	st.executeUpdate();
    }

    private void prepareStatements ( Connection con ) throws SQLException {
	insertIntoClients = con.prepareStatement(INSERT_INTO_TCLIENTS);
    }

}
