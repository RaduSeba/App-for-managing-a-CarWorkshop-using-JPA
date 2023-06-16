package uo.ri.cws.application.service.common;

import java.time.LocalDate;

public class LocalDateConverter {

	public static LocalDate convert(String arg) {
		String[] split = arg.split("-");
		int day = Integer.parseInt(split[0]);
		int month = Integer.parseInt(split[1]);
		int year = Integer.parseInt(split[2]);
		return LocalDate.of(year, month, day);
	}

	public static String asString(LocalDate d) {
		return d.getDayOfMonth() + "-" + d.getMonth().getValue() + "-" + d.getYear();
	}
}