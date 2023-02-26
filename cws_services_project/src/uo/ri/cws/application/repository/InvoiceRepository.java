package uo.ri.cws.application.repository;

import java.util.Optional;

import uo.ri.cws.domain.Invoice;

public interface InvoiceRepository extends Repository<Invoice> {

	/**
	 * @param numero de la factura que se busca
	 * @return la factura identificada o null si no existe
	 */
	Optional<Invoice> findByNumber(Long numero);
	
	/**
	 * @return el siguiente número de factura a usar, es decir,
	 * 	el mayor número existente registrado + 1.
	 * 
	 * En un despliegue real esta forma de obtener el número 
	 * puede dar problemas en concurrencia, ya que dos hilos 
	 * simultáneos podrían llegar a obtener el mismo número.
	 * El código que use este método debería tener esto en cuenta. 
	 */
	Long getNextInvoiceNumber();
}
