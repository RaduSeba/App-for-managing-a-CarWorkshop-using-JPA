package uo.ri.cws.application.service.mechanic;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.common.NIFUtil;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.common.TestContext.Key;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.MechanicUtil;
import uo.ri.cws.application.service.util.WorkOrderUtil;

public class DeleteSteps {
	private MechanicDto mechanic;
	private MechanicCrudService service = Factory.service.forMechanicCrudService();

	private TestContext ctx;

	public DeleteSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@Given("a mechanic")
	public void a_mechanic() throws BusinessException {
		mechanic = new MechanicUtil()
				.withDni(randomDni())
				.withName(randomName())
				.withSurname(randomSurname())
				.register()
				.get();
		ctx.put(Key.MECHANIC, mechanic);
	}

	@When("I remove the mechanic")
	public void i_remove_the_mechanic() throws BusinessException {
		service.deleteMechanic(mechanic.id);
	}

	@Then("the mechanic no longer exists")
	public void the_mechanic_no_longer_exists() throws BusinessException {
		MechanicDto dto = new MechanicUtil().loadById(mechanic.id).get();
		assertNull(dto);
	}

	@When("I try to remove a non existent mechanic")
	public void i_try_to_remove_a_non_existent_mechanic() {
		tryDeleteAndKeepException("does-not-exist-mechanic");

	}

	@Given("a mechanic with work orders registered")
	public void a_mechanic_with_work_orders_registered() throws BusinessException {
		String dni = randomDni();
		mechanic = new MechanicUtil().withDni(dni).register().get();
		new WorkOrderUtil().forMechanic(mechanic.id).register();

	}

	@When("I try to remove the mechanic")
	public void i_try_to_remove_the_mechanic() {
		if (mechanic == null)
			mechanic = (MechanicDto) ctx.get(Key.MECHANIC);
		tryDeleteAndKeepException(mechanic.dni);

	}

	@When("I try to remove a mechanic with null argument")
	public void i_try_to_remove_a_mechanic_with_null_argument() throws BusinessException {
		tryDeleteAndKeepException(null);

	}

	@When("I try to delete a mechanic with {string}")
	public void i_try_to_delete_a_mechanic_with(String dni) {
		tryDeleteAndKeepException(dni);

	}

	private String randomDni() {
		return NIFUtil.generateRandomNIF();
	}

	private String randomSurname() {
		return "surname";
	}

	private String randomName() {
		return "name";

	}

	private void tryDeleteAndKeepException(String dni) {
		try {
			service.deleteMechanic(dni);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}

}
