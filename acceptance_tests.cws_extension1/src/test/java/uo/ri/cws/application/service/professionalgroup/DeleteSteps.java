package uo.ri.cws.application.service.professionalgroup;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;

import uo.ri.cws.application.service.BusinessException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.common.TestContext.Key;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.sql.FindProfessionalGroupByIdSqlUnitOfWork;

public class DeleteSteps {

	private TestContext ctx;
	private ProfessionalGroupService service = Factory.service.forProfessionalGroupService();
	private String id2Del = null;
	
	public DeleteSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@When("I try to del a professional group with null argument")
	public void i_try_to_del_a_professional_group_with_null_argument() {
		tryAddAndKeepException(null);
	}

	@When("I try to del a professional group with {string}")
	public void i_try_to_del_a_professional_group_with(String arg) {
		tryAddAndKeepException(arg);
	}

	@When("I try to del a non existent professional group")
	public void i_try_to_del_a_non_existent_professional_group() {
		tryAddAndKeepException(UUID.randomUUID().toString());

	}


	@When("I try to del professional group I")
	public void i_try_to_del_professional_group_i() {
//		String id = null;
//		FindProfessionalGroupByNameSqlUnitOfWork unit = 
//				new FindProfessionalGroupByNameSqlUnitOfWork("I");
//		unit.execute();
//		id = unit.get().id;
		tryAddAndKeepException("I");

	}

	@When("I del this professional group")
	public void i_del_this_professional_group() throws BusinessException {
		this.id2Del = ((ProfessionalGroupBLDto)ctx.get(Key.PROFESSIONALGROUP)).id;
		service.deleteProfessionalGroup(
				((ProfessionalGroupBLDto)ctx.get(Key.PROFESSIONALGROUP)).name);
	}

	@Then("The professional group is deleted")
	public void the_professional_group_is_deleted() {
		FindProfessionalGroupByIdSqlUnitOfWork unit = new FindProfessionalGroupByIdSqlUnitOfWork(this.id2Del);
		unit.execute();
		assertTrue(unit.get() == null);
	}



	private void tryAddAndKeepException(String arg) {
		try {
			service.deleteProfessionalGroup(arg);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

}
