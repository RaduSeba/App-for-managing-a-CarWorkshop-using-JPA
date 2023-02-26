package uo.ri.cws.application.repository;

import java.util.List;

import uo.ri.cws.domain.PaymentMean;

public interface PaymentMeanRepository extends Repository<PaymentMean> {
	
	/**
	 * @param id of the client
	 * @return a list with all the payment means owned by the client
	 */
	List<PaymentMean> findPaymentMeansByClientId(Long id);
}
