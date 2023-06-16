package uo.ri.cws.application.service.client;

import io.cucumber.java.en.Given;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.common.TestContext;
import uo.ri.cws.application.service.util.ClientUtil;

public class ClientSteps {

    private TestContext ctx;

    private ClientDto client;

    public ClientSteps ( TestContext ctx ) {
	this.ctx = ctx;
    }

    @Given("a client")
    public void aClient ( ) {
	client = new ClientUtil().get();
    }

    @Given("a client registered")
    public void aClientRegistered ( ) {
	client = new ClientUtil().register().get();
	this.ctx.put(TestContext.Key.ACLIENT, client);

    }

}
