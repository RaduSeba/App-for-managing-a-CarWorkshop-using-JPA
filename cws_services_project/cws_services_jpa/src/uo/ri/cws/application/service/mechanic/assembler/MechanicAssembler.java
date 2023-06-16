package uo.ri.cws.application.service.mechanic.assembler;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.domain.Mechanic;

public class MechanicAssembler {

    public static MechanicDto toDto(Mechanic m) {
	MechanicDto dto = new MechanicDto();
	dto.id = m.getId();
	dto.version = m.getVersion();

	dto.dni = m.getDni();
	dto.name = m.getName();
	dto.surname = m.getSurname();
	return dto;
    }

    public static List<MechanicDto> toMechanicDtoList(List<Mechanic> list) {
	List<MechanicDto> res = new ArrayList<>();
	for (Mechanic m : list) {
	    res.add(toDto(m));
	}
	return res;
    }
}
