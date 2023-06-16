package uo.ri.cws.application.service.util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.util.sql.AddVehicleSqlUnitOfWork;
import uo.ri.cws.application.service.vehicle.VehicleCrudService.VehicleDto;

public class VehicleUtil {

    private VehicleDto dto = createDefaultVehicle();

    public VehicleUtil randomPlate ( ) {
	dto.plate = RandomStringUtils.randomAlphanumeric(7);
	return this;
    }

    public VehicleUtil register ( ) {
	new AddVehicleSqlUnitOfWork(dto).execute();
	return this;
    }

    public VehicleDto get ( ) {
	return dto;
    }

    private VehicleDto createDefaultVehicle ( ) {
	VehicleDto res = new VehicleDto();
	res.id = UUID.randomUUID().toString();
	res.version = 1L;
	res.plate = RandomStringUtils.randomAlphanumeric(9);
	res.make = RandomStringUtils.randomAlphabetic(4);
	res.model = RandomStringUtils.randomAlphabetic(4);
	res.clientId = RandomStringUtils.randomAlphabetic(4);

	return res;
    }

    public VehicleUtil withOwner ( String id ) {
	dto.clientId = id;
	return this;
    }

    public VehicleUtil withPlate ( String string ) {
	dto.plate = string;
	return this;
    }

}
