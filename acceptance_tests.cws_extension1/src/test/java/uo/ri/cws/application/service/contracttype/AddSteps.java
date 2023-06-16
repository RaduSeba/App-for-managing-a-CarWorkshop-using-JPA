package uo.ri.cws.application.service.contracttype;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.Optional;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.service.util.ContractTypeUtil;
import uo.ri.cws.application.service.util.sql.FindContractTypeByIdSqlUnitOfWork;

public class AddSteps {

	private TestContext ctx;
	private ContractTypeService service = Factory.service.forContractTypeService();
	private ContractTypeDto ct_dto = null;

	public AddSteps(TestContext ctx) {
		this.ctx = ctx;
	}
	
	@When("I try to add a contract type with null argument")
	public void i_try_to_add_a_professional_group_with_null_argument() {
		tryAddAndKeepException(null);
	}

	@When("I try to add a contract type with null name")
	public void i_try_to_add_a_professional_group_with_null_name() {
		ContractTypeDto dto = new ContractTypeUtil().withName(null).get();
		
		tryAddAndKeepException(dto);

	}

	@When("I try to add a contract type with {string}")
	public void i_try_to_add_a_professional_group_with(String arg)  {
		ContractTypeDto dto = new ContractTypeUtil().withName(arg).get();
		
		tryAddAndKeepException(dto);

	}

	@When("I try to add a contract type with negative compensation days")
	public void i_try_to_add_a_professional_group_with_negative_days() {
		ContractTypeDto dto = new ContractTypeUtil().withDays(-10).get();
		
		tryAddAndKeepException(dto);

	}

	@When("I try to add a contract type with name PERMANENT")
	public void i_try_to_add_a_professional_group_with_repeated_name() {
		
		ContractTypeDto newCt = new ContractTypeDto();
		newCt.name = "PERMANENT";
		newCt.compensationDays = 1;
		tryAddAndKeepException(newCt);

	}

	@When("I register a new contract type")
	public void i_register_a_new_professional_group(DataTable table) throws BusinessException {
		Map<String, String> row = table.asMaps().get(0);
		ContractTypeDto ct = new ContractTypeUtil()
				.withName( row.get("name"))
				.withDays( Double.parseDouble(row.get("days") ))
				.get();

		this.ct_dto = service.addContractType(ct);
	}
	
	@Then("The contract type is added")
	public void the_professional_group_is_added(DataTable table) {
		
		Map<String, String> row = table.asMaps().get(0);
		String string = row.get("type");
		
		
		FindContractTypeByIdSqlUnitOfWork finder = new FindContractTypeByIdSqlUnitOfWork(this.ct_dto.id);
		finder.execute();
		Optional<ContractTypeDto> found = finder.get();
		assertTrue(found.isPresent());
		assertTrue(found.get().version == 1L);
		String string2 = new StringBuilder()
				.append(found.get().name)
				.append(",")
				.append(Double.toString(found.get().compensationDays))
				.toString();
		assertTrue(string.equals(string2));

	}

	private void tryAddAndKeepException(ContractTypeDto arg) {
		try {
			service.addContractType(arg);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

}
