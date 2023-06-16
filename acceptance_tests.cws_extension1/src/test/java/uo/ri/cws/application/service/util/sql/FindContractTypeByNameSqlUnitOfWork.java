package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;


public class FindContractTypeByNameSqlUnitOfWork {

	private String name;
	private ConnectionData connectionData;
	private PreparedStatement find;
	private static final String FIND_CONTRACTTYPE_BY_NAME =
			"SELECT * FROM TCONTRACTTYPES" 
			+ " WHERE NAME = ?";

	private Optional<ContractTypeDto> result = Optional.empty();
	
	public FindContractTypeByNameSqlUnitOfWork(String name) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.name = name;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findContractType();
		});
	}

	private void findContractType() throws SQLException {
		PreparedStatement st = find;
		ContractTypeDto value = null;
		int i = 1;
		st.setString(i++, name);

		ResultSet rs = st.executeQuery();
		
		if ( rs.next() ) {
			value = new ContractTypeDto();
			value.id = rs.getString("id");
			value.version = rs.getLong("version");
			value.name = rs.getString("name");
			value.compensationDays = rs.getDouble("compensationdays");
		}
		this.result = Optional.ofNullable(value);
	}

	public Optional<ContractTypeDto> get() {
		return this.result;
	}

	
	private void prepareStatements(Connection con) throws SQLException {
		find = con.prepareStatement(FIND_CONTRACTTYPE_BY_NAME);
	}

}
