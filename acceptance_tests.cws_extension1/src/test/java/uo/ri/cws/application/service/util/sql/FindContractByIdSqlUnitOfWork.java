package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;
import uo.ri.cws.domain.Contract.ContractState;


public class FindContractByIdSqlUnitOfWork {

	private String id;
	private ConnectionData connectionData;
	private PreparedStatement find;
	private static final String FIND_CONTRACT_BY_ID =
			"SELECT * FROM TCONTRACTS WHERE ID = ?";
	
	private String mId, firedmId, ctId, pgId;
	private Optional<ContractDto> result = Optional.empty();

	public FindContractByIdSqlUnitOfWork(String id) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.id = id;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findContract();
			fillContract();
		});
	}
	
	private void fillContract() {
		ContractDto c = this.result.get();
		String mech = null;
		if (c.state.equals(ContractState.IN_FORCE))
			mech =mId;
		else
			mech = firedmId;

		FindMechanicByIdSqlUnitOfWork munit = new FindMechanicByIdSqlUnitOfWork(mech);
		munit.execute();
		
		c.dni = munit.get().dni;
		
		FindContractTypeByIdSqlUnitOfWork ctunit = new FindContractTypeByIdSqlUnitOfWork(ctId);
		ctunit.execute();
		c.contractTypeName = ctunit.get().get().name;
		
		FindProfessionalGroupByIdSqlUnitOfWork pgunit = new FindProfessionalGroupByIdSqlUnitOfWork(pgId);
		pgunit.execute();
		c.professionalGroupName = pgunit.get().name;
		this.result = Optional.of(c);
	}

	public Optional<ContractDto> get() {
		return result;
	}

	private void findContract() throws SQLException {
		ContractDto result = new ContractDto();
		PreparedStatement st = find;

		int i = 1;
		st.setString(i++, id);

		ResultSet rs = st.executeQuery();
		
		if ( rs.next() ) {
			result.id = rs.getString("id");
			result.version = rs.getLong("version");
			result.annualBaseWage = rs.getDouble("annualBaseWage");
			
			ctId = rs.getString("contractType_id");
			result.startDate = rs.getDate("startDate").toLocalDate();
			pgId = rs.getString("professionalGroup_id");
			result.state = ContractState.valueOf( rs.getString("state") );
			if (result.state.equals(ContractState.TERMINATED))
				firedmId = rs.getString("firedmechanic_id");
			else
				mId = rs.getString("mechanic_Id");
		
			// Optional fields
			result.settlement = rs.getDouble("settlement");
			Date d = rs.getDate("endDate");
			if (rs.wasNull())
				result.endDate = null;
			else
				result.endDate = d.toLocalDate();
			this.result = Optional.ofNullable(result);
		}
	}

	private void prepareStatements(Connection con) throws SQLException {
		find = con.prepareStatement(FIND_CONTRACT_BY_ID);
	}

}
