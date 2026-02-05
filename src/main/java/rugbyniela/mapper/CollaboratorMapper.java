package rugbyniela.mapper;

import org.mapstruct.Mapper;

import rugbyniela.entity.dto.collaborator.CollaboratorRequestDTO;
import rugbyniela.entity.dto.collaborator.CollaboratorResponseDTO;
import rugbyniela.entity.pojo.Collaborator;
import rugbyniela.entity.pojo.Division;

@Mapper(componentModel = "spring")
public interface CollaboratorMapper {

	Collaborator toEntity(CollaboratorRequestDTO dto);
	
	CollaboratorResponseDTO toDTO(Collaborator collaborator);
}
