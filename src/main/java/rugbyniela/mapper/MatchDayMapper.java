package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.matchDay.MatchDayRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayResponseDTO;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.MatchDay;

@Mapper(componentModel = "spring")
public interface MatchDayMapper {
	

	@Mapping(target = "matches", expression = "java(new java.util.HashSet<>())")
	@Mapping(target = "arePointsCalculated", constant = "false")
	MatchDay toEntity(MatchDayRequestDTO dto);
	
	@Mapping(target = "divisionId",source = "division.id")
	MatchDayResponseDTO toDTO(MatchDay matchDay);
	
	@Mapping(target = "matchStatus", source = "status")
	@Mapping(target = "localTeamName", source = "localTeam.name")
	@Mapping(target = "awayTeamName", source = "awayTeam.name")
	MatchResponseDTO toDTO(Match match);
}
