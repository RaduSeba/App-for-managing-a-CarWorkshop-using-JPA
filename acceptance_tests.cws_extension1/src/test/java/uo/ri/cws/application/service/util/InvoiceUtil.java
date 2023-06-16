package uo.ri.cws.application.service.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.util.sql.AddInvoiceSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindInvoiceByNumberSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindInvoiceSqlUnitOfWork;
import uo.ri.cws.domain.Invoice.InvoiceState;

public class InvoiceUtil {

    private InvoiceDto result = createDefaultInvoice();

    public InvoiceDto get ( ) {
	return result;
    }

    private LocalDate randomDate ( ) {
	LocalDate dateBefore = LocalDate.parse("2020-01-01");
	LocalDate dateAfter = LocalDate.now();
	long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
	LocalDate randomDate = dateBefore.plusDays(
		ThreadLocalRandom.current().nextLong(noOfDaysBetween + 1));
	return randomDate;
    }

    private InvoiceDto createDefaultInvoice ( ) {
	InvoiceDto res = new InvoiceDto();
	res.number = new Random().nextLong();
	res.date = randomDate();
	double q = new Random().nextDouble();
	double vat = 0.21 * q;
	res.vat = vat;
	res.total = vat + q;

	return res;
    }

    public InvoiceUtil withState ( InvoiceState state ) {
	result.state = state.toString();
	return this;
    }

    public InvoiceUtil withAmount ( double arg ) {
	result.total = arg + result.vat;
	return this;
    }

    public InvoiceUtil withDate ( String date ) {
	result.date = LocalDate.parse(date);
	return this;
    }

    public InvoiceUtil withVat ( double arg ) {
	result.vat = arg;
	return this;
    }

    public InvoiceUtil withNumber ( long arg ) {
	result.number = arg;
	return this;
    }

    public InvoiceUtil register ( ) {
	new AddInvoiceSqlUnitOfWork(result).execute();
	return this;
    }

    public InvoiceUtil find ( String id ) {
	FindInvoiceSqlUnitOfWork find = new FindInvoiceSqlUnitOfWork(id);
	find.execute();
	result = find.get();
	return this;
    }

    public InvoiceUtil findByNumber ( String number ) {
	FindInvoiceByNumberSqlUnitOfWork find = new FindInvoiceByNumberSqlUnitOfWork(
		number);
	find.execute();
	result = find.get();
	return this;
    }
}
