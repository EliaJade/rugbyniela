package rugbyniela.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


import rugbyniela.entity.dto.match.MatchRequestDTO;
import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.match.MatchUpdateRequestDTO;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.Team;


@Mapper(componentModel = "spring")
public interface MatchMapper {
//	void updateMatchFromDTO(MatchUpdateRequestDTO dto, @MappingTarget Match match);
	

	@Mapping(target = "localTeam", ignore = true)
	@Mapping(target = "awayTeam", ignore = true)
	@Mapping(target = "location", ignore = true)
	Match toEntity(MatchRequestDTO dto);
	

	@Mapping(target = "matchStatus", source = "status")
	@Mapping(target = "localTeamName", source = "localTeam.name")
	@Mapping(target = "awayTeamName", source = "awayTeam.name")
	MatchResponseDTO toDTO(Match match);
	
	
//	default Team map(Long id) {
//		if(id == null) {
//			return null;
//		}
//		Team team = new Team();
//		team.setId(id);
//		return team;
//		
//	}
//	
//	default Set<Team> map(Set<Long> ids){
//		if(ids == null) {
//			return null;
//		}
//		return ids.stream()
//				.map(this::map)
//				.collect(Collectors.toSet());//reuse the above map(Long id)
//	}
}
