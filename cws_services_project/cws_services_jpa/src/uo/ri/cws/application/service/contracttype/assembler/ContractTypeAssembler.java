package uo.ri.cws.application.service.contracttype.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.domain.ContractType;

public class ContractTypeAssembler {
	
	public static Optional<ContractTypeDto> toContractTypeBLDto(Optional<ContractType> arg) {
		Optional<ContractTypeDto> result = arg.isEmpty() ? Optional.ofNullable(null)
				: Optional.ofNullable(toContractTypeDto(arg.get()));
		return result;		
	}

	private static ContractTypeDto toContractTypeDto(ContractType arg) {
		ContractTypeDto result = new ContractTypeDto();
		result.id = arg.getId();
		result.version = arg.getVersion();
		result.name = arg.getName();
		result.compensationDays = arg.getCompensationDays();
		return result;
		
	}

	public static List<ContractTypeDto> toContractTypeBLDtoList(List<ContractType> arg) {
		List<ContractTypeDto> result = new ArrayList<ContractTypeDto>();
		for (ContractType ct : arg)
			result.add(toContractTypeDto(ct));
		return result;
	}
	
}
