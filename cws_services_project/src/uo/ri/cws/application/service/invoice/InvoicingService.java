package uo.ri.cws.application.service.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.cws.application.service.BusinessException;

/**
 * This service is intended to be used by the Cashier It follows the ISP
 * principle (@see SOLID principles from RC Martin)
 */
public interface InvoicingService {

	InvoiceDto createInvoiceFor(List<String> workOrderIds)
			throws BusinessException;

	List<InvoicingWorkOrderDto> findWorkOrdersByClientDni(String dni)
			throws BusinessException;

	Optional<InvoiceDto> findInvoice(Long number) throws BusinessException;

	List<PaymentMeanDto> findPayMeansByClientDni(String dni)
			throws BusinessException;

	void settleInvoice(String invoiceId, Map<Long, Double> charges)
			throws BusinessException;

	public static class InvoiceDto {

		public String id;		// the surrogate id (UUID)
		public long version;

		public double total;	// total amount (money) vat included
		public double vat;		// amount of vat (money)
		public long number;		// the invoice identity, a sequential number
		public LocalDate date;	// of the invoice
		public String state;	// the state as in InvoiceState

	}

	public static class InvoicingWorkOrderDto {
		public String id;
		public String description;
		public LocalDate date;
		public String state;
		public double total;
	}

	public static abstract class PaymentMeanDto {
		public String id;
		public long version;

		public String clientId;
		public Double accumulated;
	}

	public static class CashDto extends PaymentMeanDto {

	}

	public static class CardDto extends PaymentMeanDto {
		public String cardNumber;
		public LocalDate cardExpiration;
		public String cardType;

	}

	public static class VoucherDto extends PaymentMeanDto {

		public String code;
		public String description;
		public Double available;

	}
}
