package uo.ri.cws.application.service.util;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.common.NIFUtil;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.sql.AddMechanicSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindMechanicByDniSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindMechanicByIdSqlUnitOfWork;

public class MechanicUtil {

	private MechanicDto dto = createDefaultDto();

	public MechanicUtil unique() {
		this.dto.id = UUID.randomUUID().toString();
		this.dto.dni = NIFUtil.generateRandomNIF();
		this.dto.name = RandomStringUtils.randomAlphabetic(4) + "-name";
		this.dto.surname = RandomStringUtils.randomAlphabetic(4) + "-surname";
		return this;
	}

	public MechanicUtil withId(String arg) {
		this.dto.id = arg;
		return this;
	}

	public MechanicUtil withDni(String arg) {
		this.dto.dni = arg;
		return this;
	}

	public MechanicUtil withName(String name) {
		this.dto.name = name;
		return this;
	}

	public MechanicUtil withSurname(String arg) {
		this.dto.surname = arg;
		return this;
	}

	public MechanicUtil loadById(String id) {

		FindMechanicByIdSqlUnitOfWork find = new FindMechanicByIdSqlUnitOfWork(id);
		find.execute();
		this.dto = find.get();
		return this;

	}

	public MechanicUtil register() {
		new AddMechanicSqlUnitOfWork(dto).execute();
		return this;
	}

	public MechanicUtil registerIfNew() {
		FindMechanicByDniSqlUnitOfWork finder = new FindMechanicByDniSqlUnitOfWork(dto.dni);
		finder.execute();

		Optional<MechanicDto> op = finder.get();
		if (op.isEmpty()) {
			register();
		} else {
			dto.id = op.get().id;
		}
		return this;
	}

	public MechanicDto get() {
		return dto;
	}

	private MechanicDto createDefaultDto() {
		MechanicDto dto = new MechanicDto();
		dto.id = UUID.randomUUID().toString();
		dto.dni = NIFUtil.generateRandomNIF();
		dto.name = RandomStringUtils.randomAlphabetic(4) + "-name";
		dto.surname = RandomStringUtils.randomAlphabetic(4) + "-surname";

		return dto;
	}

}
