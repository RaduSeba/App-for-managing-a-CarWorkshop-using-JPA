package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;

public class AddContract  implements Command<ContractDto> {
	
	private ContractDto cdto;
	private ContractRepository repo=  Factory.repository.forContract();
	private ContractTypeRepository ctrepo= Factory.repository.forContractType();
	private ProfessionalGroupRepository prepo= Factory.repository.forProfessionalGroup();
	private MechanicRepository mrepo= Factory.repository.forMechanic();

	public AddContract(ContractDto dto) {
		ArgumentChecks.isNotNull(dto, "THe contract is null");
		ArgumentChecks.isNotEmpty(dto.dni, "The dni cannot be null");
		ArgumentChecks.isNotNull(dto.contractTypeName, "The contract type cannot be null");
		ArgumentChecks.isNotNull(dto.professionalGroupName, "The contract type cannot be null");
	
		this.cdto=dto;
	}

	@Override
	public ContractDto execute() throws BusinessException {
		
		Optional<Mechanic> om = mrepo.findByDni(cdto.dni);
		Optional<ContractType> ct=ctrepo.findByName(cdto.contractTypeName);
		Optional<ProfessionalGroup> pg=prepo.findByName(cdto.professionalGroupName);
		
		BusinessChecks.isFalse(om.isEmpty(),"The mechanic doesn t  exist");
		BusinessChecks.isFalse(ct.isEmpty(),"The contractType doesn t  exist");
		BusinessChecks.isFalse(pg.isEmpty(),"The professionalgroup doesn t  exist");
		
		Contract c=new Contract(om.get(),ct.get(),pg.get(),cdto.endDate,cdto.annualBaseWage	);
		
		repo.add(c);
		
		return DtoAssembler.toDto(c);
		
	}

}
