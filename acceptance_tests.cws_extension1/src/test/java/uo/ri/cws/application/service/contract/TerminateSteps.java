package uo.ri.cws.application.service.contract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.common.TestContext.Key;
import uo.ri.cws.application.service.contract.ContractService.ContractDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.util.ContractUtil;
import uo.ri.cws.application.service.util.sql.FindContractByIdSqlUnitOfWork;
import uo.ri.cws.domain.Contract.ContractState;

public class TerminateSteps {

	private ContractService service = Factory.service.forContractService();
	private TestContext ctx;
	private ContractDto theContract = null;

	public TerminateSteps(TestContext ctx) {
		this.ctx = ctx;
	}

	@When("I try to terminate a contract with null id")
	public void i_try_to_terminate_a_contract_with_null_id() {
		tryTerminateAndKeepException(null);
	}

	@When("I try to terminate a contract with wrong {string}")
	public void i_try_to_terminate_a_contract_with_wrong(String arg) {
		tryTerminateAndKeepException(arg);

	}

	@When("I try to terminate a non existent contract")
	public void i_try_to_terminate_a_non_existent_contract() {
		tryTerminateAndKeepException(UUID.randomUUID().toString());

	}

	@When("I try to terminate a terminated contract")
	public void i_try_to_terminate_a_terminated_contract() {
		this.theContract = (ContractDto) ctx.get(Key.TERMINATED);
		tryTerminateAndKeepException(this.theContract.id);
	}

	@Then("There is no contract in force for the mechanic")
	public void no_contract_in_force() throws BusinessException {
		MechanicDto m = (MechanicDto) ctx.get(Key.MECHANIC);

		ContractUtil cUtil = new ContractUtil()
				.findContractInForceForMechanic(m.id);
		ContractDto c = cUtil.get();
		assertNull(c);
	}

	@When("I terminate the contract")
	public void i_terminate_the_contract() throws BusinessException {
		theContract = (ContractDto) ctx.get(Key.INFORCE);
		theContract.state = ContractState.TERMINATED;
		theContract.endDate = LocalDate.now()
				.with(TemporalAdjusters.lastDayOfMonth());
		
		ctx.put(Key.TERMINATED, theContract);

		service.terminateContract( theContract.id );
	}

	@Then("previous contract is terminated with this settlement")
	public void previous_contract_is_terminated(DataTable table)
			throws BusinessException {
		ContractDto terminated = (ContractDto) ctx.get(Key.TERMINATED);
		String valueAsString = table.asMaps().get(0).get("settlement");
		double expectedSettlement = Double.valueOf( valueAsString );

		terminated = new ContractUtil().findContractById(terminated.id).get();

		assertEquals( ContractState.TERMINATED, terminated.state );
		assertEquals( expectedSettlement, terminated.settlement, 0.01);
	}

	@Then("Contract is terminated")
	public void contract_is_terminated() {
		FindContractByIdSqlUnitOfWork unit = new FindContractByIdSqlUnitOfWork(
				theContract.id);
		unit.execute();
		ContractDto contractFound = unit.get().get();
		contractFound.dni = ((MechanicDto) ctx.get(Key.MECHANIC)).dni;
		
		assertTrue( ContractUtil.match(contractFound, theContract) );
	}

	private void tryTerminateAndKeepException(String arg) {
		try {
			service.terminateContract(arg);
			fail();
		} catch (BusinessException ex) {
			ctx.setException(ex);
		} catch (IllegalArgumentException ex) {
			ctx.setException(ex);
		}
	}
}
