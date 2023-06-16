package uo.ri.cws.application.service.professionalgroup.crud.command;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.util.BusinessChecks;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;

public class DeleteProfessionalGroup implements Command<Void>{
	
	private String n;
	private ProfessionalGroupRepository prepo = Factory.repository.forProfessionalGroup();
	private ContractRepository repo=  Factory.repository.forContract();

	
	public DeleteProfessionalGroup(String name) {
		ArgumentChecks.isNotEmpty(name,"The name cannot be empty");
		ArgumentChecks.isNotNull(name, "The name cannot be null");
		ArgumentChecks.isNotBlank(name,"The name cannot be blank");
		this.n=name;
	}

	@Override
	public Void execute() throws BusinessException {

		
		Optional<ProfessionalGroup> o = prepo.findByName(n);
		BusinessChecks.isFalse(o.isEmpty(),"The professionalGroup doesn t exist");
	
		
		
		
		ProfessionalGroup p= o.get();
		
		List<Contract> l =repo.findByProfessionalGroupId(p.getId());
		
		BusinessChecks.isTrue(l.isEmpty(),"The professionalGroup  exist in other contracts");
		prepo.remove(p);
		
		
		
		return null;
	}

}
