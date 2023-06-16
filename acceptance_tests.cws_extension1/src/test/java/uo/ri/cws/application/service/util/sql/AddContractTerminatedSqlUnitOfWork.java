package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;

public class AddContractTerminatedSqlUnitOfWork {

	private ContractDto dto;
	private String mechanic, type, group;

	private ConnectionData connectionData;
	private PreparedStatement insertIntoTContracts;
	private static final String INSERT_INTO_TCONTRACTS =
			"INSERT INTO TCONTRACTS"
			+ " ( ID, VERSION, FIREDMECHANIC_ID, STARTDATE, ENDDATE, CONTRACTTYPE_ID,"
			+ " PROFESSIONALGROUP_ID, ANNUALBASEWAGE, STATE, SETTLEMENT )"
			+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public AddContractTerminatedSqlUnitOfWork(ContractDto dto) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.dto = dto;
	}

	public AddContractTerminatedSqlUnitOfWork(ContractDto dto, String mechanicId, String contractTypeId, String professionalGroupId) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.dto = dto;
		this.mechanic = mechanicId;
		this.group = professionalGroupId;
		this.type = contractTypeId;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertContract();
		});
	}

	private void insertContract() throws SQLException {
		PreparedStatement st = insertIntoTContracts;
		int i = 1;
		st.setString(i++, dto.id);
		st.setLong(i++, dto.version);
		st.setString(i++, mechanic);
		st.setDate(i++, Date.valueOf(dto.startDate));
		if (dto.endDate!=null)
			st.setDate(i++, Date.valueOf(dto.endDate));
		else
			st.setNull(i++, java.sql.Types.DATE);
		st.setString(i++, type);
		st.setString(i++, group);
		st.setDouble(i++, dto.annualBaseWage);
		st.setString(i++, "TERMINATED");
		st.setDouble(i++, dto.settlement);

		st.executeUpdate();
	}

	private void prepareStatements(Connection con) throws SQLException {
		insertIntoTContracts = con.prepareStatement(INSERT_INTO_TCONTRACTS);
	}

}
