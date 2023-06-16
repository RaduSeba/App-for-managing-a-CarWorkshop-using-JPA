package uo.ri.cws.application.service.professionalgroup.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.domain.ProfessionalGroup;

public class ProfessionalGroupAssembler {


	public static Optional<ProfessionalGroupBLDto> toProfessionalGroupDto(Optional<ProfessionalGroup> arg) {
		ProfessionalGroupBLDto dto = null;
		
		if (arg.isPresent()) {
			dto = toDto(arg.get());
		}
		return Optional.ofNullable(dto);
	}

	public static List<ProfessionalGroupBLDto> toProfessionalGroupDtoList(List<ProfessionalGroup> list) {
		List<ProfessionalGroupBLDto> res = new ArrayList<>();
		for (ProfessionalGroup m : list) {
			res.add(toDto(m));
		}
		return res;
	}
	
	public static ProfessionalGroupBLDto toDto(ProfessionalGroup m) {
		ProfessionalGroupBLDto 	dto = new ProfessionalGroupBLDto();
		dto.id = m.getId();
		dto.version = m.getVersion();

		dto.name = m.getName();
		dto.productivityRate = m.getProductivityBonusPercentage();
		dto.trieniumSalary = m.getTrienniumPayment();
		return dto;
	}
}
