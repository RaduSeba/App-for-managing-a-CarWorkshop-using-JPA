package uo.ri.cws.application.service.util;

import java.time.LocalDateTime;
import java.util.UUID;

import uo.ri.cws.application.service.intervention.InterventionService.InterventionDto;
import uo.ri.cws.application.service.util.sql.AddInterventionSqlUnitOfWork;

public class InterventionUtil {

	private InterventionDto dto = createDefaultDto();

	private InterventionDto createDefaultDto() {
		InterventionDto dto = new InterventionDto();
		dto.id = UUID.randomUUID().toString();
		dto.version = 1L;

		return dto;
	}

	public InterventionUtil withMechanic(String arg) {
		this.dto.mechanicId = arg;
		return this;
	}
	
	public InterventionUtil withWorkorder(String arg) {
		this.dto.workorderId = arg;
		return this;
	}
	
	public InterventionUtil withMinutes(int arg) {
		this.dto.minutes= arg;
		return this;
	}
	
	public InterventionUtil withDate(LocalDateTime arg) {
		this.dto.date = arg;
		return this;
	}
	
	public InterventionUtil register() {
		new AddInterventionSqlUnitOfWork(dto).execute();
		return this;
		
	}

	public InterventionDto get() {
		return dto;
	}

}
