package rugbyniela.service;

import org.antlr.v4.parse.ANTLRParser.throwsSpec_return;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.collaborator.CollaboratorRequestDTO;
import rugbyniela.entity.dto.collaborator.CollaboratorResponseDTO;
import rugbyniela.entity.pojo.Collaborator;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.Team;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.CollaboratorMapper;
import rugbyniela.repository.CollaboratorRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollaboratorServiceImpl implements ICollaboratorService{
	
	
	private final CollaboratorRepository collaboratorRepository;

	private final CollaboratorMapper collaboratorMapper;
	
	@Override
	public CollaboratorResponseDTO createCollaborator(CollaboratorRequestDTO dto) {
		if(collaboratorRepository.existsByName(dto.name())) {
			throw new RugbyException("Este colaborador ya existe", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		Collaborator collaborator = collaboratorMapper.toEntity(dto);
		if(collaborator.getIsActive()==null) {
			collaborator.setIsActive(true);
		}
		collaboratorRepository.save(collaborator);
		log.info("Se ha creado el colaborador {} correctamente", 
				collaborator.getId());
		return collaboratorMapper.toDTO(collaborator);
	}

	@Override
	public CollaboratorResponseDTO updateCollaborator(Long id, CollaboratorRequestDTO dto) {
		Collaborator collaborator = checkCollaborator(id);
		if(Boolean.FALSE.equals(collaborator.getIsActive())) {
			throw new RugbyException("No puedes actualizar un colaborador eliminado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		if(dto.name()!=null) {
			collaborator.setName(dto.name());
		}
		if(dto.url()!=null) {
			collaborator.setUrl(dto.url());
		}
		collaboratorRepository.save(collaborator);
		log.info("Se ha actualizado el equipo {} correctamente", 
				collaborator.getId());
		return collaboratorMapper.toDTO(collaborator);
	}

	@Override
	public void deleteCollaborator(Long id) {
		Collaborator collaborator = checkCollaborator(id);
		if(Boolean.FALSE.equals(collaborator.getIsActive())) {
			throw new RugbyException("No puedes borrar un colaborador ya eliminado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		collaborator.setIsActive(false);
		String name = deletedName(collaborator.getName());
		collaborator.setName(name);
		collaboratorRepository.save(collaborator);
		log.info("Se ha eliminado el colaborador {}", 
				collaborator.getId());
		
	}

	public Collaborator checkCollaborator(Long id) {
		Collaborator collaborator = collaboratorRepository.findById(id)
				.orElseThrow(()
				-> new RugbyException("Colaborador no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN)); 
		return collaborator;
	}
	
	private String deletedName(String name) {
	    return "DEL_" + name;
	}
}
