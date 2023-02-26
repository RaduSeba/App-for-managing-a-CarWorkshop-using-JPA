package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class PayrollJpaRepository 
		extends BaseJpaRepository<Payroll> 
		implements PayrollRepository  {

	public PayrollJpaRepository() {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public List<Payroll> findByContract(String contractId) {
		
		return Jpa.getManager().createNamedQuery("Payroll.findbyContract",Payroll.class).setParameter(1, contractId).getResultList();
	}

	@Override
	public List<Payroll> findCurrentMonthPayrolls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Payroll> findCurrentMonthByContractId(String contractId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

}
