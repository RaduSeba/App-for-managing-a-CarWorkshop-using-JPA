package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;
import uo.ri.cws.domain.Contract.ContractState;


public class FindContractsInForceSqlUnitOfWork {

	private ConnectionData connectionData;
	private PreparedStatement find;
	private static final String FIND_CONTRACTS_INFORCE =
			"SELECT * FROM TCONTRACTS"
			+ " WHERE STATE = 'IN_FORCE'" 
			;
	private String mId, ctId, pgId;
	private List<ContractDto> resultList = new ArrayList<>();

	
	public FindContractsInForceSqlUnitOfWork() {
		this.connectionData = PersistenceXmlScanner.scan();
		
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findContracts();
			fillContracts();
		});
	}
	
	private void fillContract(ContractDto c) {
		
		FindMechanicByIdSqlUnitOfWork munit = new FindMechanicByIdSqlUnitOfWork(mId);
		munit.execute();
		
		c.dni = munit.get().dni;
		
		FindContractTypeByIdSqlUnitOfWork ctunit = new FindContractTypeByIdSqlUnitOfWork(ctId);
		ctunit.execute();
		c.contractTypeName = ctunit.get().get().name;
		
		FindProfessionalGroupByIdSqlUnitOfWork pgunit = new FindProfessionalGroupByIdSqlUnitOfWork(pgId);
		pgunit.execute();
		c.professionalGroupName = pgunit.get().name;
	}
	
	private void fillContracts() {
		ContractDto c ;
		for (int index = 0; index<this.resultList.size(); index++) {
			c = this.resultList.get(index);
			fillContract(c);
		}
	}

	public List<ContractDto> get() {
		return resultList;
	}

	private void findContracts() throws SQLException {
		ContractDto result;
		PreparedStatement st = find;

		ResultSet rs = st.executeQuery();
		
		while ( rs.next() ) {
			result = new ContractDto();
			result.id = rs.getString("id");
			result.version = rs.getLong("version");
			result.annualBaseWage = rs.getDouble("annualBaseWage");
			
			ctId = rs.getString("contractType_id");
			result.startDate = rs.getDate("startDate").toLocalDate();
			pgId = rs.getString("professionalGroup_id");
			result.state = ContractState.valueOf( rs.getString("state") );
			mId = rs.getString("mechanic_id");
		
			// Optional fields
			result.settlement = rs.getDouble("settlement");
			Date d = rs.getDate("endDate");
			if (rs.wasNull())
				result.endDate = null;
			else
				result.endDate = d.toLocalDate();
			this.resultList.add(result);
		}
	}

	private void prepareStatements(Connection con) throws SQLException {
		find = con.prepareStatement(FIND_CONTRACTS_INFORCE);
	}

}
