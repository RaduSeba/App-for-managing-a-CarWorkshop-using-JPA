package uo.ri.cws.application.service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.util.sql.AddWorkOrderSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindWorkOrderSqlUnitOfWork;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;
import uo.ri.cws.domain.WorkOrder;

public class WorkOrderUtil {
	private WorkOrderDto dto = createDefaultWorkOrderDto();

	public WorkOrderUtil register() {
		new AddWorkOrderSqlUnitOfWork(dto).execute();
		return this;
	}

	public WorkOrderDto get() {
		return dto;
	}

	private LocalDateTime randomDate() {
//		It is as easy as, assume d1 and d2 being LocalDate, with d1 < d2 (pseudo-code):

		String str = "2020-04-08 12:30";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		LocalDateTime dateBefore = LocalDateTime.parse(str, formatter);
		LocalDateTime dateAfter = LocalDateTime.now();
		long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
		LocalDateTime randomDate = dateBefore.plusDays(ThreadLocalRandom.current().nextLong(noOfDaysBetween + 1));
		return randomDate;
	}

	private WorkOrderDto createDefaultWorkOrderDto() {
		WorkOrderDto res = new WorkOrderDto();

		res.id = UUID.randomUUID().toString();
		res.version = 1L;
		res.date = randomDate();
		res.description = RandomStringUtils.randomAlphabetic(10);
		res.state = WorkOrder.WorkOrderState.OPEN.toString();
		res.total = 0.0;

		return res;
	}

	public WorkOrderUtil forMechanic(String mId) {
		dto.mechanicId = mId;
		return this;
	}

	public WorkOrderUtil withState(String status) {
		dto.state = status;
		return this;
	}

	public WorkOrderUtil withAmount(double amount) {
		dto.total = amount;
		return this;
	}

	public WorkOrderUtil withDate(String date) {
		dto.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return this;
	}

	public WorkOrderUtil loadById(String id) throws BusinessException {
		FindWorkOrderSqlUnitOfWork find = new FindWorkOrderSqlUnitOfWork(id);
		find.execute();
		this.dto = find.get();
		return this;
	}

	public WorkOrderUtil withInvoice(String invoiceid) {
		dto.invoiceId = invoiceid;
		return this;
	}

	public WorkOrderUtil forVehicle(String vId) {
		dto.vehicleId = vId;
		return this;
	}

	public WorkOrderUtil withDate(LocalDateTime ldt) {
		dto.date = ldt;
		return this;
	}
}
