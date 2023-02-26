package uo.ri.cws.application.service.client;


import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.BusinessException;


public interface ClientCrudService {


    /**
     * Add a new client to the system with the data specified in the dto.
     * The id value will be ignored as it is generated here.
     *
     * @param client A dto containing info to be added
     * @param
     * @return dto with the id value set to the UUID generated
     * @throws IllegalArgumentException when argument is null or dni is null or empty string
     * @throws BusinessException        if there already exists another client with the same dni
     */
    ClientDto addClient(ClientDto client) throws BusinessException;

    /**
     * @param idClient the id of the client to be deleted
     * @throws BusinessException if the client does not exist or if there are vehicles
     *                           registered for this client
     * @throws IllegalArgumentException when argument is null or empty string
     */
    void deleteClient(String idClient) throws BusinessException;

    /**
     * Updates values for the client specified by the id field,
     * except id and dni
     *
     * @param client A dto identifying the client to update by the field id,
     *               and new data in the other fields
     * @throws BusinessException        if the client does not exist
     * @throws IllegalArgumentException when the argument is null or id is null or empty
     */
    void updateClient(ClientDto client) throws BusinessException;

    /**
     * @return the list of all clients registered in the system. It might be an empty list if there is no client
     * <p>
     * DOES NOT @throws BusinessException
     */
    List<ClientDto> findAllClients() throws BusinessException;

    /**
     * @param idClient The id of the client to find
     * @return the dto for the client or null if there is none with the id
     * @throws IllegalArgumentException when argument is null or empty string
     *                                  DOES NOT throw BusinessException
     */
    Optional<ClientDto> findClientById(String idClient) throws BusinessException;


    public static class ClientDto {
	public String id;
	public long version;

	public String dni;
	public String name;
	public String surname;
	public String phone;
	public String email;

	public String addressStreet;
	public String addressCity;
	public String addressZipcode;
    }

}