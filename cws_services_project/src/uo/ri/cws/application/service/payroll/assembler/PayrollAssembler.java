package uo.ri.cws.application.service.payroll.assembler;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.payroll.PayrollService.PayrollBLDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryBLDto;
import uo.ri.cws.domain.Payroll;

public class PayrollAssembler {

	public static PayrollBLDto toDto(Payroll arg) {
		PayrollBLDto result = new PayrollBLDto();

		result.id = arg.getId();
		result.version = arg.getVersion();
		result.bonus = arg.getBonus();
		result.contractId = arg.getContract().getId();
		result.date = arg.getDate();
		result.incomeTax = arg.getIncomeTax();
		result.monthlyWage = arg.getMonthlyWage();
		result.nic = arg.getNIC();
		result.productivityBonus = arg.getProductivityBonus();
		result.trienniumPayment = arg.getTrienniumPayment();
		result.netWage = arg.getMonthlyWage() + arg.getBonus() + arg.getProductivityBonus()
		+ arg.getTrienniumPayment() - arg.getIncomeTax() - arg.getNIC();

		return result;

	}

	public static List<PayrollBLDto> toDtoList(List<Payroll> all) {
		List<PayrollBLDto> result = new ArrayList<PayrollBLDto>();
		for (Payroll record : all)
			result.add(toDto(record));
		return result;
	}


	public static PayrollSummaryBLDto toBlSummaryDto(Payroll arg) {
		PayrollSummaryBLDto result = new PayrollSummaryBLDto();
		result.id = arg.getId();
		result.version = arg.getVersion();
		result.date = arg.getDate();
		result.netWage = arg.getMonthlyWage() + arg.getBonus() + arg.getProductivityBonus()
				+ arg.getTrienniumPayment() - arg.getIncomeTax() - arg.getNIC();
		return result;
	}

}
