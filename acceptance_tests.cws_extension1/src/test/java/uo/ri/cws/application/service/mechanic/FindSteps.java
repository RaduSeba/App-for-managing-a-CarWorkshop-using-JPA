package uo.ri.cws.application.service.mechanic;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.common.TestContext.Key;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

public class FindSteps {

    private MechanicCrudService service = Factory.service
	    .forMechanicCrudService();
    private List<MechanicDto> list = new ArrayList<>();

    private TestContext ctx;

    public FindSteps ( TestContext ctx ) {
	this.ctx = ctx;
    }

    @When("I try to find a mechanic with null contract type")
    public void i_try_to_find_null_argument ( ) {
	tryFindAndKeepException(null);

    }

    @When("I try to find mechanics with contract type {string}")
    public void i_try_to_find_group_with_argument ( String id ) {
	tryFindAndKeepException(id);

    }

    @When("I search mechanics in a non existing contract type")
    public void i_search_mech_in_a_non_existent_type ( )
	    throws BusinessException {
	list = service.findMechanicsWithContractInForceInContractType(
		UUID.randomUUID().toString());
    }

    @When("I search mechanics in contract type PERMANENT")
    public void i_find_a_payroll ( ) throws BusinessException {
	list = service
		.findMechanicsWithContractInForceInContractType("PERMANENT");
	ctx.put(Key.MECHANICS, list);

    }

    private void tryFindAndKeepException ( String id ) {
	try {
	    service.findMechanicsWithContractInForceInContractType(id);
	    fail();
	} catch (BusinessException ex) {
	    ctx.setException(ex);
	} catch (IllegalArgumentException ex) {
	    ctx.setException(ex);
	}
    }

}
