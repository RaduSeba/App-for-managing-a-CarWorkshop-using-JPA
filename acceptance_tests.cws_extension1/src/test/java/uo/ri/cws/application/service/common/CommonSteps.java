package uo.ri.cws.application.service.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.BusinessException;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessFactory;
import uo.ri.cws.application.service.util.DbUtil;
import uo.ri.cws.infrastructure.persistence.jpa.executor.JpaExecutorFactory;
import uo.ri.cws.infrastructure.persistence.jpa.repository.JpaRepositoryFactory;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class CommonSteps {

	private TestContext ctx;

	public CommonSteps(TestContext ctx) {
		this.ctx = ctx;
	}


	@Before
	public void setUp() {
		Factory.service = new BusinessFactory();
		Factory.repository = new JpaRepositoryFactory();
		Factory.executor = new JpaExecutorFactory();

		new DbUtil().clearTables();
	}

	@After
	public void tearDown() {
		Jpa.close();
	}

	@Then("an error happens with an explaining message")
	public void anErrorHappensWithAnExplainingMessage() {
		Exception ex = ctx.getException();

		assertTrue(ex instanceof BusinessException);
		assertNotNull( ex );
		assertNotNull( ex.getMessage() );
		assertFalse( ex.getMessage().isEmpty() );
	}
	
	@Then("argument is rejected with an explaining message")
	public void invalidArgumentWithAnExplainingMessage() {
		Exception ex = ctx.getException();

		assertTrue(ex instanceof IllegalArgumentException);
		assertNotNull( ex );
		assertNotNull( ex.getMessage() );
		assertFalse( ex.getMessage().isEmpty() );
		
		
	}
	
	
	@Then("empty is returned")
	public void emptyIsReturned() {
		Optional<?> optional = ctx.getUniqueResult();

		assertTrue( optional.isEmpty() );
	}

	@Then("an empty list is returned")
	public void anEmptyIsReturned() {
		List<?> list = ctx.getResultList();

		assertTrue( list.isEmpty() );
	}

}