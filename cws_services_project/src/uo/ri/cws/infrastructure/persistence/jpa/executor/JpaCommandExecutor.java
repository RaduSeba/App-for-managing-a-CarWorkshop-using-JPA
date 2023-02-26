package uo.ri.cws.infrastructure.persistence.jpa.executor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class JpaCommandExecutor implements CommandExecutor {

	@Override
	public <T> T execute(Command<T> cmd) throws BusinessException {
		EntityManager mapper = Jpa.createEntityManager();
		try {
			EntityTransaction trx = mapper.getTransaction();
			trx.begin();
			
			try {
				T res = cmd.execute();
				trx.commit();
				
				return res;
				
			} catch (BusinessException | RuntimeException ex) {
				if ( trx.isActive() ) {
					trx.rollback();
				}
				throw ex;
			}
		} finally {
			if ( mapper.isOpen() ) {
				mapper.close();
			}
		}
	}
}
