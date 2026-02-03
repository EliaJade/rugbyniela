package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rugbyniela.entity.dto.match.MatchResultDTO;
import rugbyniela.entity.pojo.Match;

@Mapper(componentModel = "spring")
public interface IMatchResultMapper {

	@Mapping(source = "id", target = "matchId")
	@Mapping(source = "localTeam.id", target = "localTeamId")
	@Mapping(source = "awayTeam.id", target = "awayTeamId")
	MatchResultDTO toDto(Match match);
}
