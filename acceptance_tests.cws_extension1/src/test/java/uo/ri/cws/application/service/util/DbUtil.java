package uo.ri.cws.application.service.util;

import uo.ri.cws.application.service.util.sql.HsqlDbClearAllTablesUnitOfWork;

public class DbUtil {

	public DbUtil clearTables() {
		new HsqlDbClearAllTablesUnitOfWork().execute();
		return this;
	}

}
