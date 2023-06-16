package uo.ri.cws.application.service.util.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.util.db.ConnectionData;
import uo.ri.cws.application.service.util.db.JdbcTransaction;
import uo.ri.cws.application.service.util.db.PersistenceXmlScanner;

public class AddPayrollSqlUnitOfWork {

	private PayrollBLDto dto = null;

	private ConnectionData connectionData;
	private PreparedStatement insertIntoTPayrolls;
	private static final String INSERT_INTO_TPAYROLLS =
			"INSERT INTO TPAYROLLS"
			+ " ( ID, VERSION, CONTRACT_ID, DATE, MONTHLYWAGE, BONUS,"
			+ " PRODUCTIVITYBONUS, TRIENNIUMPAYMENT, INCOMETAX, NIC )"
			+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


	public AddPayrollSqlUnitOfWork(PayrollBLDto arg) {
		this.connectionData = PersistenceXmlScanner.scan();
		this.dto = arg;
	}

	public void execute() {
		JdbcTransaction trx = new JdbcTransaction( connectionData );
		trx.execute((con) -> {
			prepareStatements( con );
			insertPayroll();
		});
	}

	private void insertPayroll() throws SQLException {
		PreparedStatement st = insertIntoTPayrolls;
		int i = 1;
		st.setString(i++, dto.id);
		st.setLong(i++, dto.version);
		st.setString(i++, dto.contractId);
		st.setDate(i++, Date.valueOf(dto.date));
		st.setDouble(i++, dto.monthlyWage);
		st.setDouble(i++, dto.bonus);
		st.setDouble(i++, dto.productivityBonus);
		st.setDouble(i++, dto.trienniumPayment);
		st.setDouble(i++, dto.incomeTax);
		st.setDouble(i++, dto.nic);

		st.executeUpdate();
	}

	private void prepareStatements(Connection con) throws SQLException {
		insertIntoTPayrolls = con.prepareStatement(INSERT_INTO_TPAYROLLS);
	}

}
