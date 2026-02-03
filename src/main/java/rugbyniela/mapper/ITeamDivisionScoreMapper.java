package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rugbyniela.entity.dto.teamDivisionScore.TeamDivisionScoreRequestDTO;
import rugbyniela.entity.dto.teamDivisionScore.TeamDivisionScoreResponseDTO;
import rugbyniela.entity.pojo.TeamDivisionScore;

@Mapper(componentModel = "spring")
public interface ITeamDivisionScoreMapper {

	@Mapping(target="id", ignore = true)
	@Mapping(target="season", ignore = true)
	@Mapping(target="division", ignore = true)
	@Mapping(target="team", ignore = true)
	TeamDivisionScore toEntity(TeamDivisionScoreRequestDTO dto);
	
	@Mapping(source="season.id", target = "seasonId")
	@Mapping(source="division.id", target = "divisionId")
	@Mapping(source="team.id", target = "teamId")
	TeamDivisionScoreResponseDTO toDTO(TeamDivisionScore teamDivisionScore);
}
