package uo.ri.cws.application.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CashDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.application.service.vehicleType.VehicleTypeCrudService.VehicleTypeDto;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.Invoice;
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
	dto.email = c.getEmail();
	dto.phone = c.getPhone();
	dto.addressCity = c.getAddress().getCity();
	dto.addressStreet = c.getAddress().getStreet();
	dto.addressZipcode = c.getAddress().getZipCode();

	return dto;
    }

    public static List<ClientDto> toClientDtoList(List<Client> clientes) {
	List<ClientDto> res = new ArrayList<>();
	for (Client c : clientes) {
	    res.add(DtoAssembler.toDto(c));
	}
	return res;
    }



    public static List<VoucherDto> toVoucherDtoList(List<Voucher> list) {
	List<VoucherDto> res = new ArrayList<>();
	for (Voucher b : list) {
	    res.add(toDto(b));
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
	dto.balance = v.getAvailable();
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
	dto.state = invoice.getStatus().toString();
	return dto;
    }

    public static List<PaymentMeanDto> toPaymentMeanDtoList(List<PaymentMean> list) {
	return list.stream().map(mp -> toDto(mp)).collect(Collectors.toList());
    }

    public static PaymentMeanDto toDto(PaymentMean mp) {
	if (mp instanceof Voucher) {
	    return toDto((Voucher) mp);
	} else if (mp instanceof CreditCard) {
	    return toDto((CreditCard) mp);
	} else if (mp instanceof Cash) {
	    return toDto((Cash) mp);
	} else {
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
	dto.state = a.getStatus().toString();

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
	return list.stream().map(a -> toDto(a)).collect(Collectors.toList());
    }

    public static VehicleTypeDto toDto(VehicleType vt) {
	VehicleTypeDto dto = new VehicleTypeDto();

	dto.id = vt.getId();
	dto.version = vt.getVersion();

	dto.name = vt.getName();
	dto.pricePerHour = vt.getPricePerHour();

	return dto;
    }

    public static List<VehicleTypeDto> toVehicleTypeDtoList(List<VehicleType> list) {
	return list.stream().map(a -> toDto(a)).collect(Collectors.toList());
    }

}
