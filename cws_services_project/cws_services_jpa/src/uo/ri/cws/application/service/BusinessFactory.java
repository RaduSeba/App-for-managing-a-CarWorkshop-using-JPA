package uo.ri.cws.application.service;

import uo.ri.cws.application.ServiceFactory;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientHistoryService;
import uo.ri.cws.application.service.client.crud.ClientServiceImpl;
import uo.ri.cws.application.service.contract.ContractService;
import uo.ri.cws.application.service.contract.crud.ContractServiceImpl;
import uo.ri.cws.application.service.contracttype.ContractTypeService;
import uo.ri.cws.application.service.contracttype.impl.ContractTypeServiceImpl;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.create.InvoicingServiceImpl;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.crud.MechanicCrudServiceImpl;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.impl.PayrollServiceImpl;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService;
import uo.ri.cws.application.service.professionalgroup.crud.ProfessionalGroupServiceImpl;
import uo.ri.cws.application.service.sparepart.SparePartCrudService;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicle.crud.VehicleServiceImpl;
import uo.ri.cws.application.service.vehicleType.VehicleTypeCrudService;
import uo.ri.cws.application.service.workorder.CloseWorkOrderService;
import uo.ri.cws.application.service.workorder.ViewAssignedWorkOrdersService;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService;

public class BusinessFactory implements ServiceFactory {

    @Override
    public MechanicCrudService forMechanicCrudService() {
	return new MechanicCrudServiceImpl();
    }

    @Override
    public InvoicingService forCreateInvoiceService() {
	return new InvoicingServiceImpl();
    }

    @Override
    public VehicleCrudService forVehicleCrudService() {
	return new VehicleServiceImpl();
    }

    @Override
    public CloseWorkOrderService forClosingBreakdown() {
	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public VehicleTypeCrudService forVehicleTypeCrudService() {
	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public SparePartCrudService forSparePartCrudService() {
	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public ClientCrudService forClienteCrudService() {
	return new ClientServiceImpl();
    }

    @Override
    public ClientHistoryService forClientHistoryService() {
	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public WorkOrderCrudService forWorkOrderCrudService() {
	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public ViewAssignedWorkOrdersService forViewAssignedWorkOrdersService() {
	throw new RuntimeException("Not yet implemented");
    }

    @Override
    public ContractService forContractService() {
	return new ContractServiceImpl();
    }

    @Override
    public ContractTypeService forContractTypeService() {
	return new ContractTypeServiceImpl();

    }

    @Override
    public PayrollService forPayrollService() {
	return new PayrollServiceImpl();
    }

    @Override
    public ProfessionalGroupService forProfessionalGroupService() {
	return new ProfessionalGroupServiceImpl();

    }

}
