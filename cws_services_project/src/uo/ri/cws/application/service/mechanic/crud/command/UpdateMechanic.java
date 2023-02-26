package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;

public class UpdateMechanic implements Command<Void>{

	private MechanicDto dto;
	private MechanicRepository repo = Factory.repository.forMechanic();

	public UpdateMechanic(MechanicDto dto) {
		
		ArgumentChecks.isNotEmpty(dto.id,"Id cannot be empty");
		ArgumentChecks.isNotEmpty(dto.name,"Name cannot be empty");
		ArgumentChecks.isNotEmpty(dto.surname);
		
		this.dto = dto;
	}

	@Override
	public Void execute() throws BusinessException {
		
		Optional<Mechanic> om=repo.findById(dto.id);
		
		BusinessChecks.exists(om);
		
		Mechanic m =om.get();
		
		BusinessChecks.isTrue(dto.version==m.getVersion(),"You are working on a stale data");
		m.setName(dto.name);
		m.setSurname(dto.surname);

		return null;
	}

}
