package uo.ri.cws.application.service.contract;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.service.contract.ContractService.ContractSummaryDto;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Contract.ContractState;

public class ContractAssembler {

	public static ContractDto toDto(Contract arg) {
		ContractDto result = toContractDto(arg);
		return result;
	}

	public static ContractSummaryDto toContractSummaryBLDto(Contract arg) {
		ContractSummaryDto result = null;
		result = toContractSummaryDto(arg);
		return result;

	}

	private static ContractSummaryDto toContractSummaryDto(Contract arg) {
		ContractSummaryDto result = new ContractSummaryDto();
		result.id = arg.getId();
		result.version = arg.getVersion();
		result.state = arg.getState();
		result.dni = (ContractState.IN_FORCE.equals(result.state))?
				arg.getMechanic().get().getDni():
					arg.getFiredMechanic().get().getDni();

		if (ContractState.TERMINATED.equals(result.state))
			result.settlement = arg.getSettlement();
		return result;
	}


	private static ContractDto toContractDto(Contract arg) {
		ContractDto result = new ContractDto();
		result.id = arg.getId();
		result.version = arg.getVersion();
		result.contractTypeName = arg.getContractType().getName();
		result.professionalGroupName = arg.getProfessionalGroup().getName();
		result.annualBaseWage = arg.getAnnualBaseWage();
		result.startDate = arg.getStartDate();
		if (arg.getEndDate().isPresent()) {
			result.endDate = arg.getEndDate().get();
		}
		result.state = arg.getState();
		result.settlement = arg.getSettlement();
		result.dni = ContractState.IN_FORCE.equals(result.state)
				? arg.getMechanic().get().getDni()
						: arg.getFiredMechanic().get().getDni();

		return result;
	}

	public static List<ContractSummaryDto> toDtoList(List<Contract> arg) {
		return arg.stream()
				.map(c -> toContractSummaryDto(c) )
				.collect( Collectors.toList() );
	}

}
