package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.time.LocalDateTime;
import java.util.List;

import uo.ri.cws.application.repository.InterventionRepository;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class InterventionJpaRepository
		extends BaseJpaRepository<Intervention>
		implements InterventionRepository {

	@Override
	public List<Intervention> findByMechanicIdBetweenDates(
			Long id, 
			LocalDateTime startDate, 
			LocalDateTime endDate) {
		
		return Jpa.getManager()
				.createNamedQuery(
					"Intervention.findByMechanicIdBetweenDates", 
					Intervention.class
				)
				.setParameter(1, id)
				.setParameter(2, startDate)
				.setParameter(3, endDate)
				.getResultList();
	}

}
