package uo.ri.cws.application.service.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

public class NIFUtil {

	private static final char[] LETRAS_NIF = { 'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J',
			'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E' };

	public static String generateRandomNIF() {
		LocalDateTime localDateTime = LocalDateTime.now();
		ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
		Random seed = new Random(zdt.toInstant().toEpochMilli());
		int numDNI = seed.nextInt(100000000);
		return Integer.toString(numDNI) + LETRAS_NIF[numDNI % 23];
	}

}