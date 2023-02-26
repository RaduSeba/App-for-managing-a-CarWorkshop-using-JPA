package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.Voucher;

public interface PaymentMeanRepository extends Repository<PaymentMean> {

    /**
     * @param id of the client
     * @return a list with all the payment means owned by the client
     */
    List<PaymentMean> findPaymentMeansByClientId(String id);

    List<PaymentMean> findPaymentMeansByInvoiceId(String idFactura);

    List<PaymentMean> findByClientId(String id);

    /**
     * Returns an Object[] with three values - Object[0] an integer with the
     * number of vouchers the client has - Object[1] a double with the total
     * amount available in all the client's vouchers - Object[2] a double with
     * the amount already consumed
     * 
     * @param id
     * @return
     */
    Object[] findAggregateVoucherDataByClientId(String id);

    Optional<CreditCard> findCreditCardByNumber(String pan);

    List<Voucher> findVouchersByClientId(String id);

    Optional<Voucher> findVoucherByCode(String code);
}
