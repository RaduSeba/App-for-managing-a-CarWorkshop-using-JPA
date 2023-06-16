package uo.ri.cws.application.service.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.sql.AddProfessionalGroupSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindProfessionalGroupByNameSqlUnitOfWork;
import uo.ri.util.math.Round;

public class ProfessionalGroupUtil {
	private static final int rangeMin = 1;
	private static final int rangeMax = 100;
	private ProfessionalGroupBLDto dto = createRandomProfessionalGroupDto();;

	public ProfessionalGroupUtil register() {
		new AddProfessionalGroupSqlUnitOfWork(dto).execute();
		return this;
	}

	public static void sortBLDtoByName(List<ProfessionalGroupBLDto> arg) {
		Collections.sort(arg, new Comparator<ProfessionalGroupBLDto>() {
			@Override
			public int compare(ProfessionalGroupBLDto p1,
					ProfessionalGroupBLDto p2) {
				return p1.name.compareTo(p2.name);
			}
		});
	}

	public ProfessionalGroupBLDto get() {
		return dto;
	}

	private ProfessionalGroupBLDto createRandomProfessionalGroupDto() {
		ProfessionalGroupBLDto res = new ProfessionalGroupBLDto();

		res.id = UUID.randomUUID().toString();
		res.version = 1L;
		res.name = "dummy-professional-group-name" + new Random().nextInt();
		Random r = new Random();
		double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		res.trieniumSalary = Round.twoCents(randomValue);
		randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		res.productivityRate = Round.twoCents(randomValue);
		return res;
	}

	public ProfessionalGroupUtil withName(String name) {
		dto.name = name;
		return this;
	}

	public ProfessionalGroupUtil withId(String id) {
		dto.id = id;
		return this;
	}

	public ProfessionalGroupUtil withTriennium(double amount) {
		dto.trieniumSalary = amount;
		return this;
	}

	public ProfessionalGroupUtil withProductivity(double amount) {
		dto.productivityRate = amount;
		return this;
	}

	public static boolean match(ProfessionalGroupBLDto g1,
			ProfessionalGroupBLDto g2) {
		if (g1 == g2) return true;
		// null check
		if (g1 == null) return (g2 == null);

		if ((match(g1.id, g2.id)) && (g1.version == g2.version)
				&& (match(g1.name, g2.name))
				&& Math.abs(g1.productivityRate
						- g2.productivityRate) < Double.MIN_NORMAL
				&& Math.abs(g1.trieniumSalary
						- g2.trieniumSalary) < Double.MIN_NORMAL)
			return true;
		else
			return false;

	}

	private static boolean match(String id1, String id2) {
		return (id1.compareTo(id2) == 0);
	}

	public static boolean matchGroupI(ProfessionalGroupBLDto pg) {
		if ((match(pg.name, "I"))
				&& Math.abs(pg.productivityRate - 5.00) < Double.MIN_NORMAL
				&& Math.abs(pg.trieniumSalary - 46.74) < Double.MIN_NORMAL)
			return true;
		else
			return false;
	}

	public static boolean matchGroupII(ProfessionalGroupBLDto pg) {
		if ((match(pg.name, "II"))
				&& Math.abs(pg.productivityRate - 4.50) < Double.MIN_NORMAL
				&& Math.abs(pg.trieniumSalary - 38.12) < Double.MIN_NORMAL)
			return true;
		else
			return false;
	}

	public static boolean matchGroupIII(ProfessionalGroupBLDto pg) {
		if ((match(pg.name, "III"))
				&& Math.abs(pg.productivityRate - 3.00) < Double.MIN_NORMAL
				&& Math.abs(pg.trieniumSalary - 33.44) < Double.MIN_NORMAL)
			return true;
		else
			return false;
	}

	public static boolean matchGroupIV(ProfessionalGroupBLDto pg) {
		if ((match(pg.name, "IV"))
				&& Math.abs(pg.productivityRate - 3.50) < Double.MIN_NORMAL
				&& Math.abs(pg.trieniumSalary - 28.85) < Double.MIN_NORMAL)
			return true;
		else
			return false;
	}

	public static boolean matchGroupV(ProfessionalGroupBLDto pg) {
		if ((match(pg.name, "V"))
				&& Math.abs(pg.productivityRate - 2.50) < Double.MIN_NORMAL
				&& Math.abs(pg.trieniumSalary - 19.64) < Double.MIN_NORMAL)
			return true;
		else
			return false;
	}

	public static boolean matchGroupVI(ProfessionalGroupBLDto pg) {
		if ((match(pg.name, "VI"))
				&& Math.abs(pg.productivityRate - 2.00) < Double.MIN_NORMAL
				&& Math.abs(pg.trieniumSalary - 14.78) < Double.MIN_NORMAL)
			return true;
		else
			return false;
	}

	public static boolean matchGroupVII(ProfessionalGroupBLDto pg) {
		if ((match(pg.name, "VII"))
				&& Math.abs(pg.productivityRate - 1.50) < Double.MIN_NORMAL
				&& Math.abs(pg.trieniumSalary - 11.25) < Double.MIN_NORMAL)
			return true;
		else
			return false;
	}

	public ProfessionalGroupUtil findProfessionalGroup(String name) {
		FindProfessionalGroupByNameSqlUnitOfWork unit = new FindProfessionalGroupByNameSqlUnitOfWork(
				name);
		unit.execute();
		this.dto = unit.get();
		return this;
	}

	public ProfessionalGroupBLDto unique() {
		this.dto = new ProfessionalGroupBLDto();

		this.dto.id = UUID.randomUUID().toString();
		this.dto.version = 1L;
		this.dto.name = RandomStringUtils.randomAlphabetic(4) + "-name";
		this.dto.productivityRate = new Random().nextDouble();
		this.dto.trieniumSalary = new Random().nextDouble() * 100;

		return this.dto;

	}
}
