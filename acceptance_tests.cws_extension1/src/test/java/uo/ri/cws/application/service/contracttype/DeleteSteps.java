package uo.ri.cws.application.service.contracttype;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.common.TestContext.Key;
import uo.ri.cws.application.service.contracttype.ContractTypeService.ContractTypeDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService.ProfessionalGroupBLDto;
import uo.ri.cws.application.service.util.ContractTypeUtil;
import uo.ri.cws.application.service.util.ContractUtil;
import uo.ri.cws.application.service.util.sql.FindContractTypeByIdSqlUnitOfWork;
import uo.ri.cws.application.service.util.sql.FindContractTypeByNameSqlUnitOfWork;

public class DeleteSteps {

    private TestContext ctx;
    private ContractTypeService service = Factory.service
	    .forContractTypeService();
    private String name2Del = null;
    private ContractTypeDto typeDto;
    private ProfessionalGroupBLDto profGroupDto;

    public DeleteSteps ( TestContext ctx ) {
	this.ctx = ctx;
	typeDto = ((ContractTypeDto) ctx.get(Key.CONTRACTTYPE));
	profGroupDto = ((ProfessionalGroupBLDto) ctx
		.get(Key.PROFESSIONALGROUP));
    }

    @When("I try to del a contract type with null argument")
    public void i_try_to_del_a_contract_type_with_null_argument ( ) {
	tryAddAndKeepException(null);
    }

    @When("I try to del a contract type with {string}")
    public void i_try_to_del_a_contract_type_with ( String arg ) {
	tryAddAndKeepException(arg);
    }

    @When("I try to del a non existent contract type")
    public void i_try_to_del_a_non_existent_contract_type ( ) {
	tryAddAndKeepException(UUID.randomUUID().toString());

    }

    @Given("A contract in force for the mechanic in contract type PERMANENT")
    public void a_contract_in_force_for_the_mechanic_in_contract_type_permanent ( )
	    throws BusinessException {
	MechanicDto m = ((MechanicDto) ctx.get(Key.MECHANIC));
	new ContractUtil().unique().forMechanic(m).withType(typeDto)
		.withGroup(profGroupDto).register().get();

    }

    @Given("a contract type with no contracts")
    public void a_contract_type_with_no_contracts ( ) {
	ContractTypeUtil util = new ContractTypeUtil();
	util.register();
	ContractTypeDto x = util.get();
	this.name2Del = x.name;

    }

    @Then("This contract type is deleted")
    public void this_contract_type_is_deleted ( ) {
	FindContractTypeByIdSqlUnitOfWork unit = new FindContractTypeByIdSqlUnitOfWork(
		this.name2Del);
	unit.execute();
	assertTrue(unit.get().isEmpty());
    }

    @When("I try to del contract type PERMANENT")
    public void i_try_to_del_contract_type_i ( ) {
	String id = null;
	FindContractTypeByNameSqlUnitOfWork unit = new FindContractTypeByNameSqlUnitOfWork(
		"PERMANENT");
	unit.execute();
	id = unit.get().get().id;
	tryAddAndKeepException(id);

    }

    @When("I del this contract type")
    public void i_del_this_contract_type ( ) throws BusinessException {
	service.deleteContractType(name2Del);
    }

    private void tryAddAndKeepException ( String arg ) {
	try {
	    service.deleteContractType(arg);
	    fail();
	} catch (BusinessException ex) {
	    ctx.setException(ex);
	} catch (IllegalArgumentException ex) {
	    ctx.setException(ex);
	}
    }

}
