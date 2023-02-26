package uo.ri.cws.application.repository;

import java.util.List;

import uo.ri.cws.domain.WorkOrder;

public interface WorkOrderRepository extends Repository<WorkOrder>{

	/**
	 * @param idsAveria, lista de los id de avería a recuperar
	 * @return lista con averias cuyo id aparece en idsAveria,
	 * 	o lista vacía si no hay ninguna
	 */
	List<WorkOrder> findByIds(List<String> workOrderIds);

	List<WorkOrder> findNotInvoicedWorkOrdersByClientDni(String dni);

	List<WorkOrder> findByMechanic(String mechanic_id);
}