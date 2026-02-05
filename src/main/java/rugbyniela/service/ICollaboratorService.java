package rugbyniela.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import rugbyniela.entity.dto.collaborator.CollaboratorRequestDTO;
import rugbyniela.entity.dto.collaborator.CollaboratorResponseDTO;

public interface ICollaboratorService {
	
	Page<CollaboratorResponseDTO> fetchAllCollaborators(int page, Boolean isActive, String name);
	CollaboratorResponseDTO fetchCollaboratorById(Long id);
	
	CollaboratorResponseDTO createCollaborator(CollaboratorRequestDTO dto, MultipartFile logoFile);
//	void removeCollaborator();
	CollaboratorResponseDTO updateCollaborator(Long id, CollaboratorRequestDTO dto, MultipartFile logoFile);
	void deleteCollaborator(Long id);
	

}
