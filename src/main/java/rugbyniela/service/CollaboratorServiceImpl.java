package rugbyniela.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.collaborator.CollaboratorRequestDTO;
import rugbyniela.entity.dto.collaborator.CollaboratorResponseDTO;
import rugbyniela.entity.pojo.Collaborator;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.CollaboratorMapper;
import rugbyniela.repository.CollaboratorRepository;
import rugbyniela.utils.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollaboratorServiceImpl implements ICollaboratorService{
	
	
	private final CollaboratorRepository collaboratorRepository;

	private final CollaboratorMapper collaboratorMapper;
	
	private final ISupabaseStorageService supabaseStorageService;
	
	
	@Override
	public Page<CollaboratorResponseDTO> fetchAllCollaborators(int page, Boolean isActive, String name) {
		checkNegativePage(page);
		String searchName = (name!=null && !name.isBlank())
				? "%" + name.trim().toLowerCase() + "%"
						: null;
		
		Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
		Page<Collaborator> collaborators = collaboratorRepository.findByFilters(searchName, isActive, pageable);
		
		return collaborators.map(collaboratorMapper::toDTO);
	}

	@Override
	public CollaboratorResponseDTO fetchCollaboratorById(Long id) {
		Collaborator collaborator =checkCollaborator(id);
		return collaboratorMapper.toDTO(collaborator);
	}
	
	@Override
	public CollaboratorResponseDTO createCollaborator(CollaboratorRequestDTO dto, MultipartFile logoFile) {
		if(collaboratorRepository.existsByName(dto.name())) {
			throw new RugbyException("Este colaborador ya existe", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		Collaborator collaborator = collaboratorMapper.toEntity(dto);
		if(collaborator.getIsActive()==null) {
			collaborator.setIsActive(true);
		}
		 if (logoFile != null && !logoFile.isEmpty()) {
		        try {
		            String safeName = StringUtils.normalize(dto.name()); 
		            String filename = "collaborator_" + safeName + "_" + System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();
		            
		            String publicUrl = supabaseStorageService.uploadFile(logoFile, filename);
		            
		            collaborator.setPictureUrl(publicUrl);
		            
		        } catch (Exception e) {
		            log.error("Error subiendo logo del colaborador", e);
		            throw new RugbyException("Error al subir el logo del colaborador", HttpStatus.INTERNAL_SERVER_ERROR, ActionType.SEASON_ADMIN);
		        }
		    }
		collaboratorRepository.save(collaborator);
		log.info("Se ha creado el colaborador {} correctamente", 
				collaborator.getId());
		return collaboratorMapper.toDTO(collaborator);
	}

	@Override
	public CollaboratorResponseDTO updateCollaborator(Long id, CollaboratorRequestDTO dto, MultipartFile logoFile) {
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
		if(collaboratorRepository.existsByNameAndIdNot(dto.name(), id)) {
			throw new RugbyException("Ese nombre ya existe, usa otro", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			
		}
		if (logoFile != null && !logoFile.isEmpty()) {
	      try {
              // a. Generamos nombre seguro igual que en el create
              String safeName = StringUtils.normalize(collaborator.getName()); // Usamos el nombre actual o el nuevo
              String filename = "team_" + safeName + "_" + System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();
              
              // b. Subimos el nuevo archivo
              String publicUrl = supabaseStorageService.uploadFile(logoFile, filename); // O uploadProfilePicture si no lo renombraste
              
              // c. Actualizamos la URL en la entidad
              collaborator.setPictureUrl(publicUrl);
              
              // OPCIONAL: Aquí podrías intentar borrar la imagen antigua de Supabase si quisieras ahorrar espacio
              
          } catch (Exception e) {
              log.error("Error actualizando el logo del colaborador {}", id, e);
              throw new RugbyException("Error al actualizar el logo del colaborador", HttpStatus.INTERNAL_SERVER_ERROR, ActionType.SEASON_ADMIN);
          }
      }else if(dto.deletePicture()) {
      	collaborator.setPictureUrl(null);
      }
		collaboratorRepository.save(collaborator);
		log.info("Se ha actualizado el colaborador {} correctamente", 
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

	private Collaborator checkCollaborator(Long id) {
		Collaborator collaborator = collaboratorRepository.findById(id)
				.orElseThrow(()
				-> new RugbyException("Colaborador no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN)); 
		return collaborator;
	}
	
	private String deletedName(String name) {
	    return "DEL_" + name;
	}
	
	private void checkNegativePage(int page) {
		if(page<0) {
			throw new RugbyException("La pagina no puede ser negativa", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
	}

	
}
