package uo.ri.cws.application.service.contracttype;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import uo.ri.cws.application.service.util.sql.FindContractTypeByIdSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindContractTypeByNameSqlUnitOfWork;

public class UpdateSteps {
	private TestContext ctx;
	private ContractTypeService service = Factory.service.forContractTypeService();
	private ContractTypeDto updated_dto = null;
	Optional<ContractTypeDto> found_dto = null;
	private ContractTypeDto before = null;
	private Optional<ContractTypeDto> after = null;

	public UpdateSteps(TestContext ctx) {
		this.ctx = ctx;
	}
	
	@When("I try to update a contract type with null arg")
	public void i_try_to_add_a_contract_type_with_null_argument() {
		tryUpdateAndKeepException(null);
	}

	@When("I try to update a contract type with null name")
	public void i_try_to_add_a_contract_type_with_null_name() {
		ContractTypeDto dto = new ContractTypeUtil().withName(null).get();
		tryUpdateAndKeepException(dto);

	}

	@When("I try to update a contract type with {string} name")
	public void i_try_to_add_a_contract_type_with(String arg)  {
		ContractTypeDto dto = new ContractTypeUtil().withName(arg).get();
		tryUpdateAndKeepException(dto);

	}

	@When("I try to update a contract type with negative days")
	public void i_try_to_update_a_contract_type_with_negative_days() {
		ContractTypeDto dto = new ContractTypeUtil().withDays(-10).get();
		tryUpdateAndKeepException(dto);

	}

	@When("I try to update a non existent contract type")
	public void i_try_to_update_a_non_existent_contract_type() {
		ContractTypeDto dto = new ContractTypeUtil().withName(UUID.randomUUID().toString()).get();
		tryUpdateAndKeepException(dto);
	}
	
	@Given("An initial contract type PERMANENT")
	public void an_initial_contract_with_type_permanent() {

		FindContractTypeByNameSqlUnitOfWork unit = 
				new FindContractTypeByNameSqlUnitOfWork("PERMANENT");
		unit.execute();
		before  = unit.get().get();
		
	}


	@When("I update contract type PERMANENT")
	public void i_update_a_contract_type() throws BusinessException {
		updated_dto = new ContractTypeDto();
		updated_dto.name = "PERMANENT";
		updated_dto.compensationDays = 10;

		FindContractTypeByNameSqlUnitOfWork unit = 
				new FindContractTypeByNameSqlUnitOfWork(updated_dto.name);
		unit.execute();
		before  = unit.get().get();
		updated_dto.version = before.version;
		service.updateContractType(updated_dto);
		FindContractTypeByIdSqlUnitOfWork unit2 = new FindContractTypeByIdSqlUnitOfWork(before.id);
		unit2.execute();
		after = unit2.get();

	}
	
	@When("I update contract type PERMANENT repeatedly")
	public void i_update_a_contract_type_repeatedly() 
			throws BusinessException {
		updated_dto = new ContractTypeDto();
		updated_dto.name = "PERMANENT";
		updated_dto.compensationDays = 10;

		FindContractTypeByNameSqlUnitOfWork unit = 
				new FindContractTypeByNameSqlUnitOfWork(updated_dto.name);
		unit.execute();
		before  = unit.get().get();
		updated_dto.version = before.version;
		service.updateContractType(updated_dto);
		updated_dto.version++;
		updated_dto.compensationDays = 25;
		service.updateContractType(updated_dto);

		unit = new FindContractTypeByNameSqlUnitOfWork(updated_dto.name);
		unit.execute();
		after = unit.get();

	}
	
	@Then("Contract type Version increases {int}")
	public void version_increases(Integer arg) {

		assertTrue(before.version + arg == after.get().version);

	}

	@Then("Compensation days field is updated")
	public void triennium_and_productivity_are_updated() {
		assertTrue(after.get().compensationDays - updated_dto.compensationDays < Double.MIN_NORMAL);
	}
	

	@Then("Contract type name does not change")
	public void name_does_not_change() {
		assertTrue(after.get().name.compareTo("PERMANENT") == 0);

	}


	private void tryUpdateAndKeepException(ContractTypeDto arg) {
		try {
			service.updateContractType(arg);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}
	
}
