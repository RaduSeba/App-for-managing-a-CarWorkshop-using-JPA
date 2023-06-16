package uo.ri.cws.application.service.professionalgroup;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;

import uo.ri.cws.application.service.BusinessException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.ProfessionalGroupUtil;
import uo.ri.cws.application.service.util.sql.FindProfessionalGroupByIdSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindProfessionalGroupByNameSqlUnitOfWork;

public class UpdateSteps {
	private TestContext ctx;
	private ProfessionalGroupService service = Factory.service.forProfessionalGroupService();
	private ProfessionalGroupBLDto updated_dto = null, found_dto = null;
	private ProfessionalGroupBLDto before;

	public UpdateSteps(TestContext ctx) {
		this.ctx = ctx;
	}
	
	@When("I try to update a professional group with null arg")
	public void i_try_to_add_a_professional_group_with_null_argument() {
		tryUpdateAndKeepException(null);
	}

	@When("I try to update a professional group with null name")
	public void i_try_to_add_a_professional_group_with_null_id() {
		ProfessionalGroupBLDto dto = new ProfessionalGroupUtil().withName(null).get();
		tryUpdateAndKeepException(dto);

	}

	@When("I try to update a professional group with {string} name")
	public void i_try_to_add_a_professional_group_with(String arg)  {
		ProfessionalGroupBLDto dto = new ProfessionalGroupUtil().withName(arg).get();
		tryUpdateAndKeepException(dto);

	}

	@When("I try to update a professional group with negative triennium")
	public void i_try_to_update_a_professional_group_with_negative_triennium() {
		ProfessionalGroupBLDto dto = new ProfessionalGroupUtil().withTriennium(-10.0).get();
		tryUpdateAndKeepException(dto);

	}

	@When("I try to update a professional group with negative productivity plus")
	public void i_try_to_update_a_professional_group_with_negative_productivity_plus() throws BusinessException {
		ProfessionalGroupBLDto dto = new ProfessionalGroupUtil().withProductivity(-10.0).get();
		tryUpdateAndKeepException(dto);

	}
	

	@When("I try to update a non existent professional group")
	public void i_try_to_update_a_non_existent_professional_group() {
		ProfessionalGroupBLDto dto = new ProfessionalGroupUtil()
				.withName(UUID.randomUUID().toString()).get();
		tryUpdateAndKeepException(dto);
	}

	@When("I update a professional group")
	public void i_update_a_professional_group() throws BusinessException {

		FindProfessionalGroupByNameSqlUnitOfWork unit = 
				new FindProfessionalGroupByNameSqlUnitOfWork("I");
		unit.execute();
		before = unit.get();
		
		updated_dto = new ProfessionalGroupBLDto();
		updated_dto.id = before.id;
		updated_dto.version = before.version;
		updated_dto.name = before.name;
		updated_dto.trieniumSalary = 10.0;
		updated_dto.productivityRate = 1.0;
		
		service.updateProfessionalGroup(updated_dto);
		FindProfessionalGroupByIdSqlUnitOfWork unit2 = new FindProfessionalGroupByIdSqlUnitOfWork(updated_dto.id);
		unit2.execute();
		found_dto = unit2.get();

	}

	@Then("Version increases {int}")
	public void version_increases(Integer int1) {
		assertTrue(found_dto.version == before.version + 1);

	}
	@Then("Name does not change")
	public void name_does_not_change() {
		assertTrue(found_dto.name.compareTo("I") == 0);

	}

	@Then("Triennium and productivity are updated")
	public void triennium_and_productivity_are_updated() {
		assertTrue(found_dto.productivityRate==updated_dto.productivityRate);
		assertTrue(found_dto.trieniumSalary==updated_dto.trieniumSalary);

	}

	
	private void tryUpdateAndKeepException(ProfessionalGroupBLDto arg) {
		try {
			service.updateProfessionalGroup(arg);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}
	
}
