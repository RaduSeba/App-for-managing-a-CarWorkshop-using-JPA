package uo.ri.cws.application.service.paymentmean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.BusinessException;

public interface PaymentMeanCrudService {

    /**
     * @formatter:off
     * 
     * Registers a new Card in the system with the information received
     * 
     * @param card
     * @return The dto with the id field updated
     * 
     * @throws BusinessException in case of: 
     * 		- Client does not exist. 
     * 		- There is another card registered with the same card number 
     * 		- The card to be inserted is outdated (expiration date before today)
     * 
     * @throws IllegalArgumentException if 
     * 		- The dto is null. 
     * 		- Any field of the following fields is null: id, cardExpiration, 
     * 				cardNumber, cardType, clientId 
     * 		- Any field of the following fields is empty: id, cardNumber, 
     * 				cardType, clientId
     * 
     * @formatter:on
     */
    CardDto addCard(CardDto card) throws BusinessException;

    /**
     * @formatter:off
     * 
     * Registers a new Voucher in the system with the information received
     * 
     * @param voucher
     * @return The dto with the id field updated
	 *
     * @throws BusinessException in case of: 
     * 		- Client does not exist. 
     * 		- There is another voucher registered with the same code
     * @throws IllegalArgumentException if 
     * 		- The dto is null. 
     * 		- Any field of the following is null: code, description or client id 
     * 		- Any field of the following is empty: code, description or client id 
     * 		- Field balance is negative
     * @formatter:on
     */
    VoucherDto addVoucher(VoucherDto voucher) throws BusinessException;

    /**
     * @formatter:off
     * 
     * Removes the payment mean from the system
     * 
     * @param id
     * @throws BusinessException in case of: 
     * 		- There is no payment mean with such id. 
     * 		- Cash can not be removed.
     *      - The payment mean has charges.
     *      
     * @throws IllegalArgumentException if id is null or blank
     * @formatter:on
     */
    void deletePaymentMean(String id) throws BusinessException;

    /**
     * Find a payment mean registered by id
     * 
     * @return A payment mean
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the argument is null or blank
     */
    Optional<PaymentMeanDto> findById(String id) throws BusinessException;

    /**
     * Returns all the payment means for the client identified by its id
     * 
     * @param id
     * @return A list with payment means or empty if there is no payment mean
     *         for the client
     * @throws BusinessException        DOES NOT
     * @throws IllegalArgumentException if the argument is null or blank
     */
    List<PaymentMeanDto> findPaymentMeansByClientId(String id)
	    throws BusinessException;
    
    public static abstract class PaymentMeanDto {
        public String id;
        public Long version;

        public String clientId;
        public Double accumulated;
    }

    public static class CardDto extends PaymentMeanDto {
        public String cardNumber;
        public LocalDate cardExpiration;
        public String cardType;
    }
    
    public static class CashDto extends PaymentMeanDto {}
    
    public static class VoucherDto extends PaymentMeanDto {
        public String code;
        public String description;
        public Double balance;
    }
    
}
