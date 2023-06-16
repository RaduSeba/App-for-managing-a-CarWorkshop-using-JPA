package uo.ri.cws.application.service.util;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.util.sql.AddClientSqlUnitOfWork;

public class ClientUtil {

    private ClientDto dto = unique();

    public ClientUtil randomDni ( ) {
	dto.dni = RandomStringUtils.randomAlphanumeric(9);
	return this;
    }

    public ClientUtil register ( ) {
	new AddClientSqlUnitOfWork(dto).execute();
	return this;
    }

    public ClientDto get ( ) {
	return dto;
    }

    private ClientDto unique ( ) {
	ClientDto res = new ClientDto();
	res.id = UUID.randomUUID().toString();
	res.version = 1L;
	res.dni = RandomStringUtils.randomAlphanumeric(9);
	res.name = RandomStringUtils.randomAlphabetic(4) + " name";
	res.surname = RandomStringUtils.randomAlphabetic(6) + " surname";
	res.email = RandomStringUtils.randomAlphabetic(6) + " email";
	res.phone = RandomStringUtils.randomAlphabetic(6) + " phone";
	res.addressCity = RandomStringUtils.randomAlphabetic(6) + " city";
	res.addressStreet = RandomStringUtils.randomAlphabetic(6) + " street";
	res.addressZipcode = RandomStringUtils.randomAlphabetic(6)
		+ " ZIP CODE";
	return res;
    }

    public ClientUtil withName ( String string ) {
	dto.name = string;
	return this;
    }

    public ClientUtil withSurname ( String string ) {
	dto.surname = string;
	return this;
    }

    public ClientUtil withDni ( String string ) {
	dto.dni = string;
	return this;

    }

}
