package uo.ri.cws.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import uo.ri.cws.domain.base.BaseEntity;

@Entity
@Table(name="TCHARGES", uniqueConstraints = {
		
		@UniqueConstraint(columnNames= {
				"INVOICE_ID","PAYMENTMEAN_ID"
		})	
})
public class Charge extends BaseEntity {
	// natural attributes
	private double amount = 0.0;

	// accidental attributes
	@ManyToOne private Invoice invoice;
	@ManyToOne private PaymentMean paymentMean;

	Charge(){}
	
	public Charge(Invoice invoice, PaymentMean paymentMean, double amount) {
		this.amount = amount;
		this.paymentMean=paymentMean;
		this.invoice=invoice;
		paymentMean.pay(amount);
		Associations.ToCharge.link(paymentMean, this, invoice);
		
	}

	/**
	 * Unlinks this charge and restores the accumulated to the payment mean
	 * @throws IllegalStateException if the invoice is already settled
	 */
	public void rewind() {
		// asserts the invoice is not in PAID status
		// decrements the payment mean accumulated ( paymentMean.pay( -amount) )
		// unlinks invoice, this and paymentMean
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void _setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public PaymentMean getPaymentMean() {
		return this.paymentMean;
	}

	public void _setPaymentMean(PaymentMean paymentMean) {
		this.paymentMean = paymentMean;
	}

	public void setPaymentMean(PaymentMean paymentMean) {
		this.paymentMean = paymentMean;
	}
	
	

}
