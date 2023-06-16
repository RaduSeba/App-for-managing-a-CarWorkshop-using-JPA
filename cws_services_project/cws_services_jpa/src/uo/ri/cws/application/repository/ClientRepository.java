package uo.ri.cws.application.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Client;

public interface ClientRepository extends Repository<Client> {

    Optional<Client> findByDni(String dni);

    /**
     * @return a list with all mechanics (might be empty)
     */
    List<Client> findAll();

    List<Client> findSponsoredByClient(String id);

    List<Client> findRecomendedBy(String id);

    List<Client> findWithThreeUnusedWorkOrders();

    List<Client> findWithRecomendationsDone();
}
