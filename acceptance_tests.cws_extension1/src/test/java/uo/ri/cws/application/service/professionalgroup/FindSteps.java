package uo.ri.cws.application.service.professionalgroup;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.cws.application.service.BusinessException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.ProfessionalGroupUtil;

public class FindSteps {

	private ProfessionalGroupService service = Factory.service.forProfessionalGroupService();
	private List<ProfessionalGroupBLDto> list = new ArrayList<>();
	private Optional<ProfessionalGroupBLDto> optionalGroup = Optional.empty();
	private ProfessionalGroupBLDto newOne = null;
	private TestContext ctx;

	public FindSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	
	@When("I try to find a professional group with null id")
	public void i_try_to_find_group_with_null_argument() {
		tryFindByIdAndKeepException(null);

	}

	@When("I try to find a professional group with arg {string}")
	public void i_try_to_find_group_with_argument(String id) {
		tryFindByIdAndKeepException(id);

	}

	@When("I search a non existing professional group")
	public void i_search_a_non_existent_id() throws BusinessException {
		optionalGroup = service.findProfessionalGroupByName(UUID.randomUUID().toString());
	}
	
	@Then("professional group is not found")
	public void returns_empty() {
		assertTrue(optionalGroup.isEmpty());
	}

	@When("I search professional group I")
	public void i_find_a_payroll() throws BusinessException {
		optionalGroup = service.findProfessionalGroupByName("I");

	}

	@Then("professional group I is found")
	public void this_payroll_is_returned() {

		assertTrue(ProfessionalGroupUtil.matchGroupI(optionalGroup.get()));
	}

	@When("I search all professional groups")
	public void i_search_all() throws BusinessException {
		list = service.findAllProfessionalGroups();
		ProfessionalGroupUtil.sortBLDtoByName(list);
		
	}

	@Then("Default ones are found")
	public void default_are_returned() {
		ProfessionalGroupUtil.sortBLDtoByName(list);

		assertTrue(ProfessionalGroupUtil.matchGroupI(list.get(0)));
		assertTrue(ProfessionalGroupUtil.matchGroupII(list.get(1)));
		assertTrue(ProfessionalGroupUtil.matchGroupIII(list.get(2)));
		assertTrue(ProfessionalGroupUtil.matchGroupIV(list.get(3)));
		assertTrue(ProfessionalGroupUtil.matchGroupV(list.get(4)));
		assertTrue(ProfessionalGroupUtil.matchGroupVI(list.get(5)));
		assertTrue(ProfessionalGroupUtil.matchGroupVII(list.get(6)));
	}

	@Given("A new registered professional group")
	public void a_new_registered_group() {
		ProfessionalGroupUtil util = new ProfessionalGroupUtil()
				.withId("VIII")
				.withName("VIII")
				.withProductivity(5.50)
				.withTriennium(12.0)
				.register();
		newOne = util.get();

	}

	@Then("New one is found")
	public void new_one_is_found() {
		ProfessionalGroupBLDto eight = list.get(list.size()-1);
		
		assertTrue(ProfessionalGroupUtil.match(eight, newOne));
	}

	private void tryFindByIdAndKeepException(String id) {
		try {
			service.findProfessionalGroupByName(id);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

}
