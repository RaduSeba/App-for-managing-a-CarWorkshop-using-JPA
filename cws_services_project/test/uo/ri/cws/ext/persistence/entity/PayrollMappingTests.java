package uo.ri.cws.ext.persistence.entity;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Payroll;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.cws.persistence.util.UnitOfWork;

public class PayrollMappingTests {

    private UnitOfWork unitOfWork;
    private EntityManagerFactory factory;
    private Mechanic mechanic;
    private Contract contract;
    private ContractType type;
    private ProfessionalGroup group;
    private Payroll payroll;

    @Before
    public void setUp() {
	factory = Persistence
		.createEntityManagerFactory("carworkshop");
	unitOfWork = UnitOfWork
		.over(factory);

	mechanic = new Mechanic("mechanic-dni");

	type = new ContractType("contract-type-name", 2.0);
	group = new ProfessionalGroup("professional-group-name", 300.0, 10.0);
	contract = new Contract(mechanic, type, group, 3000.0);
	payroll = new Payroll(contract);

	unitOfWork
		.persist(payroll, contract, group, type, mechanic);
    }

    @After
    public void tearDown() {
	unitOfWork
		.remove(payroll, contract, group, type, mechanic);
	factory
		.close();
    }

    /**
     * All fields of payroll are persisted properly
     */
    @Test
    public void testAllFieldsPersisted() {
	Payroll restored = unitOfWork
		.findById(Payroll.class, payroll
			.getId());

	/*
	 * 
	 * @Basic(optional = false) private LocalDate date;
	 * 
	 * @ManyToOne(optional = false) private Contract contract;
	 * 
	 * private double monthlyWage; private double bonus; private double
	 * productivityBonus; private double trienniumPayment; private double
	 * incomeTax; private double nic;
	 */
	assertEquals(payroll
		.getId(), restored
			.getId());
	assertEquals(payroll
		.getDate(), restored
			.getDate());
	assertEquals(payroll
		.getContract(), restored
			.getContract());
	assertEquals(payroll
		.getMonthlyWage(), restored
			.getMonthlyWage(), 0.001);
	assertEquals(payroll
		.getBonus(), restored
			.getBonus(), 0.001);
	assertEquals(payroll
		.getProductivityBonus(), restored
			.getProductivityBonus(), 0.001);
	assertEquals(payroll
		.getTrienniumPayment(), restored
			.getTrienniumPayment(), 0.001);
	assertEquals(payroll
		.getIncomeTax(), restored
			.getIncomeTax(), 0.001);

	assertEquals(payroll
		.getNIC(), restored
			.getNIC(), 0.001);
    }

    /**
     * When two payrolls with the same contract and startDate, the second cannot
     * be persisted
     */
    @Test(expected = PersistenceException.class)
    public void testRepeated() {
	unitOfWork
		.persist(payroll);
	Payroll repeatedPayroll = new Payroll(contract, payroll
		.getDate());

	unitOfWork
		.persist(repeatedPayroll);
    }

}
