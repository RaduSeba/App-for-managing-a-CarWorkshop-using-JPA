package uo.ri.cws.application.service.professionalgroup;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import uo.ri.cws.application.service.BusinessException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.common.TestContext.Key;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.ProfessionalGroupUtil;
import uo.ri.cws.application.service.util.sql.FindProfessionalGroupByIdSqlUnitOfWork;

public class AddSteps {

	private TestContext ctx;
	private ProfessionalGroupService service = Factory.service.forProfessionalGroupService();
	private ProfessionalGroupBLDto pg_dto = null;

	public AddSteps(TestContext ctx) {
		this.ctx = ctx;
	}
	
	@When("I try to add a professional group with null argument")
	public void i_try_to_add_a_professional_group_with_null_argument() {
		tryAddAndKeepException(null);
	}

	@When("I try to add a professional group with null name")
	public void i_try_to_add_a_professional_group_with_null_name() {
		ProfessionalGroupBLDto dto = new ProfessionalGroupUtil().withName(null).get();
		
		tryAddAndKeepException(dto);

	}

	@When("I try to add a professional group with {string}")
	public void i_try_to_add_a_professional_group_with(String arg)  {
		ProfessionalGroupBLDto dto = new ProfessionalGroupUtil().withName(arg).get();
		
		tryAddAndKeepException(dto);

	}

	@When("I try to add a professional group with negative triennium")
	public void i_try_to_add_a_professional_group_with_negative_triennium() {
		ProfessionalGroupBLDto dto = new ProfessionalGroupUtil().withTriennium(-10.0).get();
		
		tryAddAndKeepException(dto);

	}

	@When("I try to add a professional group with negative productivity plus")
	public void i_try_to_add_a_professional_group_with_negative_productivity_plus() throws BusinessException {
		ProfessionalGroupBLDto dto = new ProfessionalGroupUtil().withProductivity(-10.0).get();
		
		tryAddAndKeepException(dto);

	}

	@Given("a freshly new professional group")
	public void a_professional_group() {
		ProfessionalGroupUtil util = new ProfessionalGroupUtil().register();
		pg_dto = util.get();
		ctx.put(Key.PROFESSIONALGROUP, pg_dto);
		
	}

	@When("I try to add a repeated professional group")
	public void i_try_to_add_a_professional_group_with_the_same_name() {
		ProfessionalGroupUtil util = new ProfessionalGroupUtil().withName("I");
		ProfessionalGroupBLDto newPg = util.get();
		tryAddAndKeepException(newPg);

	}
	@When("I register a new professional group with the following data")
	public void i_register_a_new_professional_group(DataTable table) 
			throws BusinessException {
		Map<String, String> row = table.asMaps().get(0);
		ProfessionalGroupBLDto	pg = new ProfessionalGroupUtil()
				.withName( row.get("name") )
				.withProductivity( Double.parseDouble(row.get("productivityRate") ) )
				.withTriennium( Double.parseDouble(row.get("trienniumPay") ) )
				.get();
		
		this.pg_dto = service.addProfessionalGroup(pg);
	}
	

	@Then("The professional group is added")
	public void the_professional_group_is_added(DataTable table) {
		Map<String, String> row = table.asMaps().get(0);
		String pgStr = row.get("professionalGroup") ;
		
		FindProfessionalGroupByIdSqlUnitOfWork finder = 
				new FindProfessionalGroupByIdSqlUnitOfWork(this.pg_dto.id);
		finder.execute();
		ProfessionalGroupBLDto found = finder.get();
		String foundStr = new StringBuilder()
				.append(found.name)
				.append(",")
				.append(found.trieniumSalary)
				.append(",")
				.append(found.productivityRate)
				.toString()
				;
		assertTrue(found.version == 1L);
		assertTrue(pgStr.equals(foundStr));

	}

	private void tryAddAndKeepException(ProfessionalGroupBLDto arg) {
		try {
			service.addProfessionalGroup(arg);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

}
