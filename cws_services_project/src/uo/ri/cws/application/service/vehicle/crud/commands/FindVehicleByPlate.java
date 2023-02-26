package uo.ri.cws.application.service.vehicle.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.VehicleRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;
import uo.ri.cws.application.util.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Vehicle;
import uo.ri.util.assertion.ArgumentChecks;

public class FindVehicleByPlate implements Command<Optional<VehicleDto>> {

	private String plate;
	private VehicleRepository repo = Factory.repository.forVehicle();
	
	public FindVehicleByPlate(String plate) {
		ArgumentChecks.isNotEmpty( plate );
		this.plate = plate;
	}

	@Override
	public Optional<VehicleDto> execute() throws BusinessException {
		Optional<Vehicle> m = repo.findByPlate( plate );
		return m.isPresent()
				? Optional.of( DtoAssembler.toDto( m.get() ))
				: Optional.empty();
	}

}
