package uo.ri.cws.infrastructure.persistence.jpa.executor;

import uo.ri.cws.application.util.command.ComandExecutorFactory;
import uo.ri.cws.application.util.command.CommandExecutor;

public class JpaExecutorFactory implements ComandExecutorFactory {

	@Override
	public CommandExecutor forExecutor() {
		return new JpaCommandExecutor();
	}

}
