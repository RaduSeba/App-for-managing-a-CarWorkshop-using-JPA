package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;


public class FindAllPayrollsSqlUnitOfWork {
	private ConnectionData connectionData;
	private PreparedStatement find;
	private static final String FIND_ALL_PAYROLLS =
			"SELECT * FROM TPAYROLLS"; 
	
	private List<PayrollBLDto> results = new ArrayList<>();
	
	public FindAllPayrollsSqlUnitOfWork() {
		this.connectionData = PersistenceXmlScanner.scan();
		
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findAllPayrolls();
		});
	}

	private void findAllPayrolls() throws SQLException {
		PreparedStatement st = find;
		PayrollBLDto result = null;
		ResultSet rs = st.executeQuery();
		
		while ( rs.next() ) {
			result = new PayrollBLDto();
			result.id = rs.getString("id");
			result.version = rs.getLong("version");
			result.contractId = rs.getString("contract_id");
			result.date = rs.getDate("date").toLocalDate();
			result.monthlyWage = rs.getDouble("monthlyWage");
			result.bonus = rs.getDouble("bonus");
			result.productivityBonus = rs.getDouble("productivityBonus");
			result.trienniumPayment = rs.getDouble("trienniumPayment");
			result.incomeTax = rs.getDouble("incomeTax");
			result.nic = rs.getDouble("nic");
		
			results.add(result);
			
		}
	}

	public List<PayrollBLDto> get() {
		return this.results;
	}

	
	private void prepareStatements(Connection con) throws SQLException {
		find = con.prepareStatement(FIND_ALL_PAYROLLS);
	}

}
