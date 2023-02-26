package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.util.assertion.ArgumentChecks;

public class CreateInvoiceFor implements Command<InvoiceDto>{

	private List<String> workOrderIds;
	private WorkOrderRepository wrkrsRepo = Factory.repository.forWorkOrder();
	private InvoiceRepository invsRepo = Factory.repository.forInvoice();

	public CreateInvoiceFor(List<String> workOrderIds) {
		ArgumentChecks.isNotNull( workOrderIds );
		for(int i=0;i<workOrderIds.size();i++)
		{
			ArgumentChecks.isNotEmpty(workOrderIds.get(i),"one id is empty" );
			ArgumentChecks.isNotNull(workOrderIds.get(i), "one id is null");
			ArgumentChecks.isNotBlank(workOrderIds.get(i), null);
		}
		
		
		if(workOrderIds.isEmpty()==true)
		{
			throw new IllegalArgumentException( "List cannot be empty" );
		}
		
		
		this.workOrderIds = workOrderIds;
	}

	@Override
	public InvoiceDto execute() throws BusinessException {
	
		Long number= invsRepo.getNextInvoiceNumber();
		List<WorkOrder> workOrders=wrkrsRepo.findByIds(workOrderIds);
		
		BusinessChecks.isTrue(workOrders.size()==workOrderIds.size(), "Some workorders do not exist");
		
		
		checkAllAreFinished(workOrders);
		
		Invoice i= new Invoice(number,workOrders);
		invsRepo.add(i);
		
		
		
		
		return DtoAssembler.toDto(i);
	}
	
	public void checkAllAreFinished(List<WorkOrder>workOrders) throws BusinessException
	{
		for(WorkOrder w: workOrders)
		{
			BusinessChecks.isTrue(w.isFinished()==true, "There is an unfinished workorder");
		}
	}

}
