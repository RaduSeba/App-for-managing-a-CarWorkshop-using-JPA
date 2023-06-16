package uo.ri.cws.application.service.util;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryBLDto;
import uo.ri.cws.application.service.util.sql.AddPayrollSqlUnitOfWork;


public class PayrollUtil {

	private PayrollBLDto dto = null;

	public PayrollUtil unique() {
		dto = new PayrollBLDto();
		dto.id = UUID.randomUUID().toString();
		dto.version = 1L;
		dto.bonus = 0.0;
		dto.date = LocalDate.now();
		dto.incomeTax = randomDouble();
		dto.monthlyWage = randomDouble();
		dto.nic = randomDouble();
		dto.productivityBonus = randomDouble();
		dto.trienniumPayment = randomDouble();

		return this;
	}
	
	private double randomDouble() {
		Random random = new Random();  // Only one instance needed.
		return random.nextInt(100) / 100.0;
	}
	
	public PayrollUtil forDate(LocalDate d) {
		this.dto.date = d;
		return this;
	}
	
	public PayrollUtil forContract (String cid ) {
		this.dto.contractId = cid;
		return this;
	}
	
	public PayrollUtil forMonthlyWage (double arg ) {
		this.dto.monthlyWage = arg;
		return this;
	}
	
	
	public PayrollUtil forBonus (double arg ) {
		this.dto.bonus = arg;
		return this;
	}
	
	public PayrollUtil forProductivityBonus (double arg ) {
		this.dto.productivityBonus = arg;
		return this;
	}
	
	public PayrollUtil forTrienniumPayment (double arg ) {
		this.dto.trienniumPayment = arg;
		return this;
	}
	
	public PayrollUtil forIncomeTax (double arg ) {
		this.dto.incomeTax = arg;
		return this;
	}
	
	public PayrollUtil forNIC (double arg ) {
		this.dto.nic = arg;
		return this;
	}
	
	public PayrollUtil register() {
		new AddPayrollSqlUnitOfWork(dto).execute();
		return this;
	}
	
	public PayrollBLDto get() {
		return this.dto;
	}
	
	public static boolean match(List<PayrollBLDto> list1, List<PayrollBLDto> list2) {
		PayrollBLDto dto1 = null;
		PayrollBLDto dto2 = null;

		if (list1.size() != list2.size())
			return false;
		PayrollUtil.sortBLDtoById(list2);
		PayrollUtil.sortBLDtoById(list1);

		for (int index = 0; index < list1.size(); index++) {
			dto1 = list1.get(index);
			dto2 = list2.get(index);
			if (!PayrollUtil.match(dto1, dto2))
				return false;
		}
		return true;
	}
	
	public static boolean match(PayrollBLDto pr1, PayrollBLDto pr2) {
		 if (pr1 == pr2)
		        return true;
		    // null check
		    if (pr1 == null)
		        return (pr2 == null);

		    if ( (match(pr1.id, pr2.id))
					&& (match(pr1.contractId, pr2.contractId))
					&& (match(pr1.date, pr2.date))
					&& Math.abs(pr1.monthlyWage - pr2.monthlyWage) < Double.MIN_NORMAL
					&& Math.abs(pr1.bonus - pr2.bonus) < Double.MIN_NORMAL
					&& Math.abs(pr1.trienniumPayment - pr2.trienniumPayment) < Double.MIN_NORMAL
					&& Math.abs(pr1.productivityBonus - pr2.productivityBonus) < Double.MIN_NORMAL
					&& Math.abs(pr1.incomeTax - pr2.incomeTax) < Double.MIN_NORMAL
					&& Math.abs(pr1.nic - pr2.nic) < Double.MIN_NORMAL
					)
		    	return true;
		    else
		    	return false;
	}
	
	public static boolean matchDates(LocalDate d1, LocalDate d2) {
		if ((d1 == null && d2 == null) || (d1 != null && d2 != null && d1.compareTo(d2) == 0))
			return true;
		return false;
	}
	
	private static boolean match(String id1, String id2) {
		return (id1.compareTo(id2) == 0);
	}

	
	private static boolean match(Object o1, Object o2) {
		return (o1.equals(o2));
	}
	
	public static void sortBLDtoById(List<PayrollBLDto> arg) {
		Collections.sort(arg, new Comparator<PayrollBLDto>() {
			  @Override
			  public int compare(PayrollBLDto p1, PayrollBLDto p2) {
			    return p1.id.compareTo(p2.id);
			  }
			});
	}
	
	public static void sortPayrollSummaryById(List<PayrollSummaryBLDto> arg) {
		Collections.sort(arg, new Comparator<PayrollSummaryBLDto>() {
			  @Override
			  public int compare(PayrollSummaryBLDto p1, PayrollSummaryBLDto p2) {
			    return p1.id.compareTo(p2.id);
			  }
			});
	}
	
	public static void sortDALDtoById(List<PayrollBLDto> arg) {
		Collections.sort(arg, new Comparator<PayrollBLDto>() {
			  @Override
			  public int compare(PayrollBLDto p1, PayrollBLDto p2) {
			    return p1.id.compareTo(p2.id);
			  }
			});
	}

	public static boolean matchPayrollSummaries(List<PayrollSummaryBLDto> list1, List<PayrollSummaryBLDto> list2) {
		PayrollSummaryBLDto s1 = null, s2 = null;
		
		sortPayrollSummaryById(list1);
		sortPayrollSummaryById(list2);
		
		for (int index = 0; index < list1.size(); index++) {
			s1 = list1.get(index);
			s2 = list2.get(index);
			if (!s1.id.equals(s2.id) 
					|| (s1.version != s2.version) 
					|| !(s1.date.equals(s2.date))
					|| !(Double.compare(s1.netWage, s2.netWage) < 0.001) )
				return false;
		}
		return true;
	}

	public static boolean matchPayrollSummaries(PayrollSummaryBLDto s1, PayrollSummaryBLDto s2) {
		if (!s1.id.equals(s2.id) 
				|| (s1.version != s2.version) 
				|| !(s1.date.equals(s2.date))
				|| !(Double.compare(s1.netWage, s2.netWage) < 0.001) )
			return false;
		else
			return true;
	}

}
