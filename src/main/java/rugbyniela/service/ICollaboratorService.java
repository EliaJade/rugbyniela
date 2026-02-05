package rugbyniela.service;

import rugbyniela.entity.dto.collaborator.CollaboratorRequestDTO;
import rugbyniela.entity.dto.collaborator.CollaboratorResponseDTO;

public interface ICollaboratorService {
	
	CollaboratorResponseDTO createCollaborator(CollaboratorRequestDTO dto);
//	void removeCollaborator();
	CollaboratorResponseDTO updateCollaborator(Long id, CollaboratorRequestDTO dto);
	void deleteCollaborator(Long id);
	

}
