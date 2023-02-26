package uo.ri.cws.application.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.service.contract.ContractService.ContractSummaryDto;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.service.invoice.InvoicingService.CardDto;
import uo.ri.cws.application.service.invoice.InvoicingService.CashDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.cws.application.service.invoice.InvoicingService.VoucherDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService.VehicleTypeDto;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.WorkOrder;

public class DtoAssembler {

	public static ClientDto toDto(Client c) {
		 ClientDto dto = new ClientDto();

		 dto.id = c.getId();
		 dto.version = c.getVersion();

		 dto.dni = c.getDni();
		 dto.name = c.getName();
		 dto.surname = c.getSurname();

		 return dto;
	}
	
	public static ContractDto toDto(Contract c) {
		 ContractDto dto = new ContractDto();

		 dto.id = c.getId();
		 dto.version = c.getVersion();

		 dto.dni = c.getMechanic().get().getId();
		dto.annualBaseWage=c.calculateannualwage();
		dto.contractTypeName=c.getContractType().getName();
		dto.endDate=c.getEndDate().get();
		dto.startDate=c.getStartDate();
		dto.professionalGroupName=c.getProfessionalGroup().getName();
		dto.settlement=c.getSettlement();
		dto.state=c.getState();
		

		 return dto;
	}
	
	public static ContractTypeDto toDto(ContractType c) {
		 ContractTypeDto dto = new ContractTypeDto();

		 dto.id = c.getId();
		 dto.version = c.getVersion();

		 dto.name=c.getName();
		 dto.compensationDays=c.getCompensationDays();
		
		

		 return dto;
	}
	
	public static ContractSummaryDto toDto2(Contract c) {
		 ContractSummaryDto dto = new ContractSummaryDto();

		 dto.id = c.getId();
		 dto.version = c.getVersion();

		 dto.dni = c.getMechanic().get().getId();
		
		dto.settlement=c.getSettlement();
		dto.state=c.getState();
		
		dto.numPayrolls=c.getPayrolls().size();
		

		 return dto;
	}


	public static List<ClientDto> toClientDtoList(List<Client> clientes) {
		List<ClientDto> res = new ArrayList<>();
		for(Client c: clientes) {
			res.add( DtoAssembler.toDto( c ) );
		}
		return res;
	}
	
	public static List<ContractDto> toContractDtoList(List<Contract> contracts) {
		List<ContractDto> res = new ArrayList<>();
		for(Contract c: contracts) {
			res.add( DtoAssembler.toDto( c ) );
		}
		return res;
	}
	
	
	
	public static List<ContractTypeDto> toContractTypeDtoList(List<ContractType> contracts) {
		List<ContractTypeDto> res = new ArrayList<>();
		for(ContractType c: contracts) {
			res.add( DtoAssembler.toDto( c ) );
		}
		return res;
	}
	
	
	public static List<ContractSummaryDto> toContractSummaryDtoList(List<Contract> contracts) {
		List<ContractSummaryDto> res = new ArrayList<>();
		for(Contract c: contracts) {
			res.add( DtoAssembler.toDto2( c ) );
		}
		return res;
	}

	public static MechanicDto toDto(Mechanic m) {
		MechanicDto dto = new MechanicDto();
		dto.id = m.getId();
		dto.version = m.getVersion();

		dto.dni = m.getDni();
		dto.name = m.getName();
		dto.surname = m.getSurname();
		return dto;
	}

	public static List<MechanicDto> toMechanicDtoList(List<Mechanic> list) {
		List<MechanicDto> res = new ArrayList<>();
		for(Mechanic m: list) {
			res.add( toDto( m ) );
		}
		return res;
	}

	public static List<VoucherDto> toVoucherDtoList(List<Voucher> list) {
		List<VoucherDto> res = new ArrayList<>();
		for(Voucher b: list) {
			res.add( toDto( b ) );
		}
		return res;
	}

	public static VoucherDto toDto(Voucher v) {
		VoucherDto dto = new VoucherDto();
		dto.id = v.getId();
		dto.version = v.getVersion();

		dto.clientId = v.getClient().getId();
		dto.accumulated = v.getAccumulated();
		dto.code = v.getCode();
		dto.description = v.getDescription();
		dto.available = v.getAvailable();
		return dto;
	}

	public static CardDto toDto(CreditCard cc) {
		CardDto dto = new CardDto();
		dto.id = cc.getId();
		dto.version = cc.getVersion();

		dto.clientId = cc.getClient().getId();
		dto.accumulated = cc.getAccumulated();
		dto.cardNumber = cc.getNumber();
		dto.cardExpiration = cc.getValidThru();
		dto.cardType = cc.getType();
		return dto;
	}

	public static CashDto toDto(Cash m) {
		CashDto dto = new CashDto();
		dto.id = m.getId();
		dto.version = m.getVersion();

		dto.clientId = m.getClient().getId();
		dto.accumulated = m.getAccumulated();
		return dto;
	}

	public static InvoiceDto toDto(Invoice invoice) {
		InvoiceDto dto = new InvoiceDto();
		dto.id = invoice.getId();
		dto.version = invoice.getVersion();

		dto.number = invoice.getNumber();
		dto.date = invoice.getDate();
		dto.total = invoice.getAmount();
		dto.vat = invoice.getVat();
		dto.state = invoice.getState().toString();
		return dto;
	}

	public static List<PaymentMeanDto> toPaymentMeanDtoList(List<PaymentMean> list) {
		return list.stream()
				.map( mp -> toDto( mp ) )
				.collect( Collectors.toList() );
	}

	private static PaymentMeanDto toDto(PaymentMean mp) {
		if (mp instanceof Voucher) {
			return toDto( (Voucher) mp );
		}
		else if (mp instanceof CreditCard) {
			return toDto( (CreditCard) mp );
		}
		else if (mp instanceof Cash) {
			return toDto( (Cash) mp);
		}
		else {
			throw new RuntimeException("Unexpected type of payment mean");
		}
	}

	public static WorkOrderDto toDto(WorkOrder a) {
		WorkOrderDto dto = new WorkOrderDto();
		dto.id = a.getId();
		dto.version = a.getVersion();

		dto.vehicleId = a.getVehicle().getId();
		dto.description = a.getDescription();
		dto.date = a.getDate();
		dto.total = a.getAmount();
		dto.state = a.getState().toString();

		dto.invoiceId = a.getInvoice() == null ? null : a.getInvoice().getId();

		return dto;
	}

	public static VehicleDto toDto(Vehicle v) {
		VehicleDto dto = new VehicleDto();
		dto.id = v.getId();
		dto.version = v.getVersion();

		dto.plate = v.getPlateNumber();
		dto.clientId = v.getClient().getId();
		dto.make = v.getMake();
		dto.vehicleTypeId = v.getVehicleType().getId();
		dto.model = v.getModel();

		return dto;
	}

	public static List<WorkOrderDto> toWorkOrderDtoList(List<WorkOrder> list) {
		return list.stream()
				.map( a -> toDto( a ) )
				.collect( Collectors.toList() );
	}

	public static VehicleTypeDto toDto(VehicleType vt) {
		VehicleTypeDto dto = new VehicleTypeDto();

		dto.id = vt.getId();
		dto.version = vt.getVersion();

		dto.name = vt.getName();
		dto.pricePerHour = vt.getPricePerHour();

		return dto;
	}

	public static List<VehicleTypeDto> toVehicleTypeDtoList(
			List<VehicleType> list) {
		return list.stream()
				.map( a -> toDto( a ) )
				.collect( Collectors.toList() );
	}

}
