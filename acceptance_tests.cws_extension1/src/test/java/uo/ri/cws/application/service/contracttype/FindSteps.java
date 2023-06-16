package uo.ri.cws.application.service.contracttype;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.cws.application.service.BusinessException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.service.util.ContractTypeUtil;
import uo.ri.cws.application.service.util.sql.FindContractTypeByNameSqlUnitOfWork;

public class FindSteps {

	private ContractTypeService service = Factory.service.forContractTypeService();

	private TestContext ctx;
	private Optional<ContractTypeDto> optionalFound;
	private ContractTypeDto dto = null;
	private List<ContractTypeDto> allFound = null;

	public FindSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@When("I try to find a contract type with name {string}")
	public void i_try_to_find_a_contract_type_with_arg(String arg) {
		tryFindAndKeepException(arg);
	}
	
	@Then("contract type PERMANENT is found")
	public void contract_type_permanent_is_found() {
		FindContractTypeByNameSqlUnitOfWork finder = 
				new FindContractTypeByNameSqlUnitOfWork("PERMANENT");
		finder.execute();
		ContractTypeDto dto = finder.get().get();
		
		assertTrue(optionalFound.isPresent());
		assertTrue(ContractTypeUtil.match(optionalFound.get(), dto));
	}

	@Given("A new registered contract type")
	public void a_new_registered_contract_type() {
		ContractTypeUtil util = new ContractTypeUtil().withId("ZZZZZZ").withName("ZZZZZZ");
		util.register();
		dto = util.get();
	}

	@When("I search all contract types")
	public void i_search_all_contract_types() throws BusinessException {
		allFound  = service.findAllContractTypes();
	}

	@Then("{string} contract types are found")
	public void contract_types_are_found(String arg) {
		int number = Integer.parseInt(arg);
		assertTrue(allFound.size() == number);
	}
	
	@Then("Default contract types are found")
	public void default_contract_types_are_found() {
		
		ContractTypeUtil.sortBLDtoByName(allFound);
		assertTrue(ContractTypeUtil.matchFIXEDTERM(allFound.get(0)));
		assertTrue(ContractTypeUtil.matchPERMANENT(allFound.get(1)));
		assertTrue(ContractTypeUtil.matchTEMPORARY(allFound.get(2)));
	}

	@Then("New contract type is found")
	public void new_contract_type_is_found() {
		assertTrue(ContractTypeUtil.match(this.dto, allFound.get(3)));

	}

	@When("I search a non existing contract type")
	public void i_search_a_non_existing_contract_type() throws BusinessException {
		optionalFound = service.findContractTypeByName(UUID.randomUUID().toString());

	}

	@Then("contract type is not found")
	public void contract_type_is_not_found() {
		assertTrue(optionalFound.isEmpty());

	}

	@When("I search contract type PERMANENT")
	public void i_search_contract_type_permanent() throws BusinessException {
	    optionalFound = service.findContractTypeByName("PERMANENT");
	}


	@Then("Only PERMANENT mechanics are found")
	public void this_payroll_is_returned() {

	}

	private void tryFindAndKeepException(String id) {
		try {
			service.findContractTypeByName(id);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

}
