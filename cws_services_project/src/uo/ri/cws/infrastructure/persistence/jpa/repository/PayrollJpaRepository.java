package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.domain.ContractType;
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
		YearMonth currentYearMonth = YearMonth.now();

		// Set the day of the month to 1 to get the start date of the current month
		LocalDate startDateOfCurrentMonth = currentYearMonth.atDay(1);
		
		// Get the next month by adding 1 to the current month
		YearMonth nextYearMonth = currentYearMonth.plusMonths(1);

		// Set the day of the month to 1 to get the start date of the next month
		LocalDate startDateOfNextMonth = nextYearMonth.atDay(1);
		return Jpa.getManager().createNamedQuery("Payroll.findCurrentMonth",Payroll.class).setParameter(1,  startDateOfCurrentMonth).setParameter(2, startDateOfNextMonth).getResultList();
	}

	@Override
	public Optional<Payroll> findCurrentMonthByContractId(String contractId) {
		
		YearMonth currentYearMonth = YearMonth.now();

		// Set the day of the month to 1 to get the start date of the current month
		LocalDate startDateOfCurrentMonth = currentYearMonth.atDay(1);
		
		// Get the next month by adding 1 to the current month
		YearMonth nextYearMonth = currentYearMonth.plusMonths(1);

		// Set the day of the month to 1 to get the start date of the next month
		LocalDate startDateOfNextMonth = nextYearMonth.atDay(1);
		
		return Jpa.getManager().createNamedQuery("Payroll.findCurrentMonthByContractId",Payroll.class).setParameter(1, contractId).setParameter(2,  startDateOfCurrentMonth).setParameter(3, startDateOfNextMonth)
				.getResultList().stream().findFirst();
	}

}
