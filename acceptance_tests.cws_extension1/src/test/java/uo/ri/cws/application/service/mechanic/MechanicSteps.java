package uo.ri.cws.application.service.mechanic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.common.LocalDateConverter;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.common.TestContext.Key;
import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.ContractTypeUtil;
import uo.ri.cws.application.service.util.ContractUtil;
import uo.ri.cws.application.service.util.MechanicUtil;
import uo.ri.cws.application.service.util.ProfessionalGroupUtil;
import uo.ri.cws.domain.Contract.ContractState;

public class MechanicSteps {

	private TestContext ctx;

	private MechanicCrudService service = Factory.service.forMechanicCrudService();
	private MechanicDto mechanic;
	private List<MechanicDto> mechanics = new ArrayList<>();
	private Optional<MechanicDto> optional = Optional.empty();

	public MechanicSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Given("the following mechanic")
	public void theFollowingMechanic(DataTable dataTable) {
		Map<String, String> row = dataTable.asMaps().get(0);
		MechanicDto m = new MechanicUtil()
				.withDni(row.get("dni"))
				.withName(row.get("name"))
				.withSurname(row.get("surname"))
				.register()
				.get();
		ctx.put(Key.MECHANIC, m);
	}

	@Given("the following relation of mechanics")
	public void theFollowingRelationOfMechanics(DataTable dataTable) {
		MechanicDto m = null;
		List<Map<String, String>> table = dataTable.asMaps();
		for (Map<String, String> row : table) {
			m = new MechanicUtil()
					.withDni(row.get("dni"))
					.withName(row.get("name"))
					.withSurname(row.get("surname"))
					.register()
					.get();
			
			mechanics.add(m);
		}
		ctx.put(Key.MECHANICS, mechanics);
	}

	@Given("the following relation of mechanics with a contract")
	public void the_following_mechanics_with_a_contract_in_force2(DataTable dataTable) {
		List<Map<String, String>> table = dataTable.asMaps();
		for (Map<String, String> row : table) {
			processRow(row);
		}
	}

	@Given("the following relation of mechanics with a contract in force")
	public void the_following_mechanics_with_a_contract_in_force(DataTable dataTable) {
		List<Map<String, String>> table = dataTable.asMaps();
		for (Map<String, String> row : table) {
			processRow(row);
		}
	}

	@Given("the following relation of mechanics with a contract terminated")
	public void the_following_mechanics_with_a_contract_terminated(DataTable dataTable) {
		List<Map<String, String>> table = dataTable.asMaps();
		for (Map<String, String> row : table) {
			processRowTerminated(row);
		}
	}

	private void processRow(Map<String, String> row) {
		MechanicDto mDto = registerMechanic(
				row.get("dni"),
				row.get("name"),
				row.get("surname")
			);
		ctx.put(Key.MECHANIC, mDto);
		
		ContractDto cDto = registerContractForMechanic(
				mDto, 
				row.get("type"), 
				row.get("group"), 
				row.get("startDate"), 
				row.get("endDate"), 
				row.get("state"), 
				row.get("pay")
			);
		
		if (cDto.state.equals(ContractState.IN_FORCE)) {
			ctx.put(Key.INFORCE, cDto);
		} else {
			ctx.put(Key.TERMINATED, cDto);
		}
	}

	private void processRowTerminated(Map<String, String> row) {
		MechanicDto mDto = registerMechanic(
				row.get("dni"),
				row.get("name"),
				row.get("surname")
			);
		ctx.put(Key.MECHANIC, mDto);
		
		ContractDto c = registerContractForMechanic(
					mDto, 
					row.get("type"), 
					row.get("group"), 
					row.get("startDate"), 
					row.get("endDate"), 
					"TERMINATED", 
					row.get("pay")
				);
		ctx.put(Key.TERMINATED, c);
	}

	private MechanicDto registerMechanic(String dni, String name, String surname) {
		return new MechanicUtil()
				.withDni( dni )
				.withName( name )
				.withSurname( surname )
				.register()
				.get();
	}

	@Given("the following relation of contracts for this mechanic")
	public void contracts_for_mechanic(DataTable dataTable) throws BusinessException {
		MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);
		insertContractsForMechanic(m, dataTable);
	}

	private void insertContractsForMechanic(MechanicDto m, DataTable dataTable) throws BusinessException {
		List<Map<String, String>> table = dataTable.asMaps();
		List<ContractDto> cs = new ArrayList<>();
		ContractDto dto = null;
		for (Map<String, String> row : table) {
			String type = row.get("type");
			String group = row.get("group");
			String state = row.get("state");
			String pay = row.get("annual_money");
			String startDate = row.get("startDate");
			String endDate = row.get("endDate");

			dto = registerContractForMechanic(m, type, group, startDate, endDate, state, pay);
			cs.add(dto);
		}
		ctx.put(Key.CONTRACTS, cs);
	}

	@Given("the following mechanic with a contract in force")
	public void mechanicWithContractInForce(DataTable dataTable) throws BusinessException {
		Map<String, String> row = dataTable.asMaps().get(0);
		MechanicDto mDto = registerMechanic(
				row.get("dni"),
				row.get("name"),
				row.get("surname")
			);
		ctx.put(Key.MECHANIC, mDto);
		
		LocalDate sixMonthsAgo = LocalDate.now()
				.minusMonths(6)
				.with(TemporalAdjusters.firstDayOfMonth());
		
		ContractDto c = registerContractForMechanic(
					mDto, 
					row.get("type"), 
					row.get("group"), 
					LocalDateConverter.asString( sixMonthsAgo ), 
					row.get("endDate"), // no end date if null 
					"IN_FORCE", 
					row.get("pay")
				);
		ctx.put(Key.INFORCE, c);
	}

	@Given("the following mechanic with a contract in force shorter than a year")
	public void mechanicWithContractInForceShorterThanOneYear(DataTable dataTable) throws BusinessException {
		Map<String, String> row = dataTable.asMaps().get(0);
		MechanicDto mDto = registerMechanic(
				row.get("dni"),
				row.get("name"),
				row.get("surname")
			);
		ctx.put(Key.MECHANIC, mDto);
		
		LocalDate sixMonthsAgo = LocalDate.now()
				.minusMonths(6)
				.with(TemporalAdjusters.firstDayOfMonth());
		
		ContractDto c = registerContractForMechanic(
					mDto, 
					row.get("type"), 
					row.get("group"), 
					LocalDateConverter.asString( sixMonthsAgo ), 
					null, // no end date 
					"IN_FORCE", 
					row.get("pay")
				);
		ctx.put(Key.INFORCE, c);
	}

	@Given("the following mechanic with a contract in force for one year")
	public void mechanicWithAContractInForceForOneYear(DataTable dataTable) throws BusinessException {
		Map<String, String> row = dataTable.asMaps().get(0);
		MechanicDto mDto = registerMechanic(
				row.get("dni"),
				row.get("name"),
				row.get("surname")
			);
		ctx.put(Key.MECHANIC, mDto);
		
		LocalDate oneYearMonthAgo = LocalDate.now()
				.minusMonths(1)
				.minusYears(1)
				.with(TemporalAdjusters.firstDayOfMonth());
		
		ContractDto c = registerContractForMechanic(
					mDto, 
					row.get("type"), 
					row.get("group"), 
					LocalDateConverter.asString( oneYearMonthAgo ), 
					null, // no end date 
					"IN_FORCE", 
					row.get("pay")
				);
		ctx.put(Key.INFORCE, c);
	}

	@Given("the following mechanic with a contract terminated")
	public void mechanic_with_a_contract_terminated(DataTable dataTable) throws BusinessException {
		processRowTerminated(dataTable.asMaps().get(0));
	}

	private ContractDto registerContractForMechanic(MechanicDto m, 
			String type, String group, 
			String startDate, String endDate,
			String state, 
			String payment) {
		
		ContractTypeDto ct = new ContractTypeUtil().findContractType(type).get();
		ProfessionalGroupBLDto pg = new ProfessionalGroupUtil().findProfessionalGroup(group).get();
		double pay = (payment == null || payment.isBlank()) 
				? 1000.0 
				: Double.parseDouble(payment);

		LocalDate start = (startDate == null) 
				? LocalDate.now().plusMonths(1).with(TemporalAdjusters.firstDayOfMonth())
				: LocalDateConverter.convert(startDate);
		
		LocalDate end = (ct.name.equals("FIXED_TERM")) 
				? LocalDateConverter.convert(endDate) 
				: null;

		return new ContractUtil()
				.unique()
				.forMechanic(m)
				.withType(ct)
				.withGroup(pg)
				.withState(state)
				.withStartDate(start)
				.withEndDate(end)
				.withAnnualWage(pay)
				.register()
				.get();
	}

//	private ContractDto addTerminatedContractForMechanic(MechanicDto m, String type, String group, String startDate,
//			String endDate, String payment) throws BusinessException {
//		ContractTypeDto ct = new ContractTypeUtil().findContractType(type).get();
//		ProfessionalGroupBLDto pg = new ProfessionalGroupUtil().findProfessionalGroup(group).get();
//		
//		double pay = (payment == null || payment.isBlank()) 
//				? 1000.0 
//				: Double.parseDouble(payment);
//
//		LocalDate start = (startDate == null) 
//				? LocalDate.now().plusMonths(1).with(TemporalAdjusters.firstDayOfMonth())
//				: LocalDateConverter.convert(startDate);
//		
//		LocalDate end = (ct.name.equals("FIXED_TERM")) 
//				? LocalDateConverter.convert(endDate) 
//				: null;
//
//		ContractDto cDto = new ContractUtil()
//				.unique()
//				.forMechanic(m)
//				.withType(ct)
//				.withGroup(pg)
//				.withState("TERMINATED")
//				.withStartDate(start)
//				.withEndDate(end)
//				.withAnnualWage(pay)
//				.register()
//				.get();
//		ctx.put(Key.TERMINATED, cDto );
//		
//		return cDto;
//	}

	@When("we read all mechanics")
	public void weReadAllMechanics() throws BusinessException {
		mechanics = service.findAllMechanics();
		ctx.put(Key.MECHANICS, mechanics);

	}

	@When("we read all mechanics with contract in force")
	public void we_read_all_mechanics_with_contract_in_force() throws BusinessException {
		mechanics = service.findMechanicsInForce();
		ctx.put(Key.MECHANICS, mechanics);
	}

	private String randomDni() {
		return RandomStringUtils.randomAlphabetic(9);
	}

	private String randomSurname() {
		return RandomStringUtils.randomAlphabetic(4) + "-surname";
	}

	private String randomName() {
		return RandomStringUtils.randomAlphabetic(4) + "-name";

	}

	@When("I add a new non existing mechanic")
	public void iAddANewMechanic() throws BusinessException {
		mechanic = new MechanicUtil().withDni(randomDni()).get();
		mechanic.id = service.addMechanic(mechanic).id;
		ctx.put(Key.MECHANIC, mechanic);
	}

	@When("I try to add a new mechanic with null argument")
	public void iTryToAddANewMechanicWithNullArgument() {

		MechanicDto dto = null;

		tryAddAndKeepException(dto);
	}

	@When("I try to add a new mechanic with null dni")
	public void iTryToAddANewMechanicWithNullDni() {

		MechanicDto dto = new MechanicUtil().withDni(null).withName(randomName()).withSurname(randomSurname()).get();

		tryAddAndKeepException(dto);
	}

	@When("I try to add a new mechanic with {string}, {string}, {string}")
	public void iTryToAddANewMechanicWith(String dni, String name, String surname) {
		MechanicDto dto = new MechanicUtil().withDni(dni).withSurname(surname).withName(name).get();

		tryAddAndKeepException(dto);
	}

	@When("I try to add a new mechanic with same dni")
	public void iTryToAddANewMechanicWithSameDni() throws BusinessException {

		MechanicDto dto = new MechanicUtil().withDni(((MechanicDto) (ctx.get(Key.MECHANIC))).dni).get();

		tryAddAndKeepException(dto);
	}

	@When("I try to update a non existing mechanic")
	public void iTryToUpdateNonExistingMechanic() throws BusinessException {
		MechanicDto dto = new MechanicUtil().withId("mechanic-does-not-exist").get();

		tryUpdateAndKeepException(dto);
	}

	@When("I try to update a mechanic with null name")
	public void iTryToUpdateAMechanicWithNullName() {
		MechanicDto dto = new MechanicUtil().withName(null).get();

		tryUpdateAndKeepException(dto);

	}

	@When("I try to update a mechanic with null surname")
	public void iTryToUpdateAMechanicWithNullSurname() {
		MechanicDto dto = new MechanicUtil().withSurname(null).get();

		tryUpdateAndKeepException(dto);

	}

	@When("I try to update a mechanic with null dni")
	public void iTryToUpdateAMechanicWithNullDni() {
		MechanicDto dto = new MechanicUtil().withDni(null).get();

		tryUpdateAndKeepException(dto);

	}

	@When("I try to update the mechanic to {string}, {string} and {string}")
	public void iTryToUpdateTheMechanicToDniNameAndSurname(String name, String surname, String dni)
			throws BusinessException {
		MechanicDto dto = new MechanicUtil().loadById(((MechanicDto) (ctx.get(Key.MECHANIC))).id).get();

		dto.name = name;
		dto.surname = surname;
		dto.dni = dni;

		tryUpdateAndKeepException(dto);
	}

	@When("I update the mechanic")
	public void iUpdateTheMechanic() throws BusinessException {
		MechanicDto dto = new MechanicUtil().loadById(((MechanicDto) (ctx.get(Key.MECHANIC))).id).get();

		dto.name = dto.name += "-updated";
		dto.surname = dto.surname += "-updated";

		service.updateMechanic(dto);
	}

	@When("I try to find a mechanic with null argument")
	public void iTryToFindAMechanicWithNullArgument() {
		tryFindAndKeepException(null);
	}

//    @When("I try to find a mechanic with null dni")
//    public void iTryToFindAMechanicWithNullDni ( ) {
//	tryFindByDniAndKeepException(null);
//
//    }

	@When("I try to find a non existent mechanic")
	public void iTryToFindAMechanicByTheId() throws BusinessException {
		service.findMechanicById("does-not-exist-id");
	}

//	@When("Search a mechanic by a non existent dni")
//	public void iTryToFindAMechanicByANonExistentDni() throws BusinessException {
//		this.optional  = service.findMechanicByDni("does-not-exist-dni");
//
//	}

	@When("I look for a mechanic by wrong id {string}")
	public void iLookForAMechanicById(String id) throws BusinessException {
		tryFindAndKeepException(id);
	}

//    @When("I look for a mechanic by wrong dni {string}")
//    public void iLookForAMechanicByWrongDni ( String string ) {
//	tryFindByDniAndKeepException(string);
//
//    }

	@When("I look for mechanic by id")
	public void iLookForMechanic() throws BusinessException {
		optional = service.findMechanicById(((MechanicDto) (ctx.get(Key.MECHANIC))).id);
	}

//    @When("i search mechanic with the following dni")
//    public void iSearchForMechanicByDni ( DataTable table )
//	    throws BusinessException {
//	Map<String, String> row = table.asMaps().get(0);
//	optional = service.findMechanicByDni(row.get("dni"));
//    }

	@Then("one mechanic is found")
	public void oneMechanicFound() throws BusinessException {
		assertTrue(optional.isPresent());
	}

	@Then("the mechanic results updated for fields name and surname")
	public void theMechanicResultsUpdatedForNameAndSurnameOnly() throws BusinessException {
		MechanicDto loaded = new MechanicUtil().loadById(((MechanicDto) (ctx.get(Key.MECHANIC))).id).get();
		this.mechanic = (MechanicDto) ctx.get(Key.MECHANIC);
		assertEquals(mechanic.dni, loaded.dni);
		assertEquals(mechanic.name + "-updated", loaded.name);
		assertEquals(mechanic.surname + "-updated", loaded.surname);
	}

	@Then("the mechanic results added to the system")
	public void theMechanicResultsAddedToTheSystem() throws BusinessException {
		Optional<MechanicDto> op = service.findMechanicById(mechanic.id);

		MechanicDto added = op.get();
		this.mechanic = (MechanicDto) ctx.get(Key.MECHANIC);

		assertEquals(mechanic.dni, added.dni);
		assertEquals(mechanic.name, added.name);
		assertEquals(mechanic.surname, added.surname);
		assertFalse(added.id.isBlank());
		assertTrue(added.version == 1L);
	}

	@Then("mechanic not found")
	public void weGetNoMechanic() throws BusinessException {
		assertTrue(this.optional.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Then("we get the following list of mechanics")
	public void iGetTheFollowingRelationOfMechanics(DataTable data) {
		List<String> expectedMechanics = extractMechanics(data);
		mechanics = ((List<MechanicDto>) ctx.get(Key.MECHANICS));
		assertEquals(expectedMechanics.size(), mechanics.size());
		for (MechanicDto mechanic : mechanics) {
			String mString = new StringBuilder().append(mechanic.dni).append(",").append(mechanic.name).append(",")
					.append(mechanic.surname).toString();
			assertTrue(expectedMechanics.contains(mString));
		}
	}

	@Then("I get the following mechanic")
	public void iGetMechanic(DataTable table) {
		Map<String, String> row = table.asMaps().get(0);
		String mechStr = row.get("mechanic");

		MechanicDto dto = optional.get();
		String mString = new StringBuilder().append(dto.dni).append(",").append(dto.name).append(",")
				.append(dto.surname).toString();

		assertEquals(mechStr, mString);
	}

	@Then("List of mechanics is empty")
	public void list_of_mechanics_is_empty() {
		assertTrue(mechanics.isEmpty());

	}

	private List<String> extractMechanics(DataTable data) {
		return data.asMaps().stream().map(r -> r.get("mechanic")).collect(Collectors.toList());
	}

	private void tryAddAndKeepException(MechanicDto dto) {
		try {
			service.addMechanic(dto);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}

	}

	private void tryUpdateAndKeepException(MechanicDto dto) {
		try {
			service.updateMechanic(dto);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}

	}

	private void tryFindAndKeepException(String id) {
		try {
			service.findMechanicById(id);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

//    private void tryFindByDniAndKeepException ( String dni ) {
//	try {
//	    service.findMechanicByDni(dni);
//	    fail();
//	} catch (BusinessException ex) {
//	    ctx.setException(ex);
//	} catch (IllegalArgumentException ex) {
//	    ctx.setException(ex);
//	}
//    }

}
