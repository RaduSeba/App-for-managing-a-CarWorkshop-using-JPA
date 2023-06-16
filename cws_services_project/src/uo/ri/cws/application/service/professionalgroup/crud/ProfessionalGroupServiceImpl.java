package uo.ri.cws.application.service.professionalgroup.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factory;
import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService;
import uo.ri.cws.application.service.professionalgroup.crud.command.AddProfessionalGroup;
import uo.ri.cws.application.service.professionalgroup.crud.command.DeleteProfessionalGroup;
import uo.ri.cws.application.service.professionalgroup.crud.command.FindAllProfessionalGroups;
import uo.ri.cws.application.service.professionalgroup.crud.command.FindProfessionalGroup;
import uo.ri.cws.application.service.professionalgroup.crud.command.UpdateProfessionalGroup;
import uo.ri.cws.application.util.command.CommandExecutor;

public class ProfessionalGroupServiceImpl implements ProfessionalGroupService {
	
	private CommandExecutor executor = Factory.executor.forExecutor();


	public ProfessionalGroupServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ProfessionalGroupBLDto addProfessionalGroup(
			ProfessionalGroupBLDto dto) throws BusinessException {
		// TODO Auto-generated method stub
		return executor.execute(new AddProfessionalGroup(dto));
	}

	@Override
	public void deleteProfessionalGroup(String name) throws BusinessException {
		
		executor.execute(new DeleteProfessionalGroup(name));
		
	}

	@Override
	public void updateProfessionalGroup(ProfessionalGroupBLDto dto)
			throws BusinessException {
		// TODO Auto-generated method stub
		executor.execute(new UpdateProfessionalGroup(dto));
	}

	@Override
	public Optional<ProfessionalGroupBLDto> findProfessionalGroupByName(
			String id) throws BusinessException {
		// TODO Auto-generated method stub
		return executor.execute(new FindProfessionalGroup(id));
	}

	@Override
	public List<ProfessionalGroupBLDto> findAllProfessionalGroups()
			throws BusinessException {
		
		return executor.execute(new FindAllProfessionalGroups());
	}

}
