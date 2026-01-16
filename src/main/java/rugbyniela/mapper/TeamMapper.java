package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import rugbyniela.entity.dto.team.TeamRequestDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;
import rugbyniela.entity.pojo.Team;

@Mapper(componentModel = "spring")
public interface TeamMapper {
	void updateTeamFromDTO(TeamRequestDTO dto, @MappingTarget Team team);
	Team toEntity(TeamRequestDTO dto);
	TeamResponseDTO toDTO(Team team);
}
