package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;

public class FindPayrollsThisMonthSqlUnitOfWork {

	private int idMonth, idYear;
	private ConnectionData connectionData;
	private PreparedStatement find;
	private static final String FIND_PAYROLL_THIS_MONTH =
			"SELECT * FROM TPAYROLLS"
					+ " WHERE YEAR (date) = ?"
					+ " AND MONTH(date) = ?"; 
			
	private List<PayrollBLDto> result = new ArrayList<>();

	
	public FindPayrollsThisMonthSqlUnitOfWork(LocalDate d ) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.idMonth = d.getMonthValue();
		this.idYear = d.getYear();
	}


	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			findPayrolls();
		});
	}
	
	public List<PayrollBLDto> get() {
		return result;
	}

	private void findPayrolls() throws SQLException {
		PreparedStatement st = find;
		PayrollBLDto payroll = null;
		
		int i = 1;
		st.setInt(i++, idYear);
		st.setInt(i, idMonth);

		ResultSet rs = st.executeQuery();
		
		while ( rs.next() ) {
			payroll = new PayrollBLDto();
			payroll.id = rs.getString("id");
			payroll.version = rs.getLong("version");
			payroll.contractId = rs.getString("contract_id");
			payroll.date = rs.getDate("date").toLocalDate();
			payroll.monthlyWage = rs.getDouble("monthlyWage");
			payroll.bonus = rs.getDouble("bonus");
			payroll.productivityBonus = rs.getDouble("productivityBonus");
			payroll.trienniumPayment = rs.getDouble("trienniumPayment");
			payroll.incomeTax = rs.getDouble("incomeTax");
			payroll.nic = rs.getDouble("nic");

			result.add(payroll);
			
		}
	}

	private void prepareStatements(Connection con) throws SQLException {
		find = con.prepareStatement(FIND_PAYROLL_THIS_MONTH);
	}

}
