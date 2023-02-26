package uo.ri.cws.application.service.professionalgroup.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.BusinessException;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupService;

public class ProfessionalGroupServiceImpl implements ProfessionalGroupService {

	public ProfessionalGroupServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ProfessionalGroupBLDto addProfessionalGroup(
			ProfessionalGroupBLDto dto) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteProfessionalGroup(String name) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProfessionalGroup(ProfessionalGroupBLDto dto)
			throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<ProfessionalGroupBLDto> findProfessionalGroupByName(
			String id) throws BusinessException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<ProfessionalGroupBLDto> findAllProfessionalGroups()
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}
