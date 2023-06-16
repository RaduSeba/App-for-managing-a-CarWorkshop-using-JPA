package uo.ri.cws.application.util.command;

import uo.ri.cws.application.service.BusinessException;

public interface Command<T> {

	T execute() throws BusinessException; 
}
