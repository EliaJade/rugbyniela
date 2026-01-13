package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionSimpleResponseDTO;
import rugbyniela.entity.dto.coalitionSeasonScore.CoalitionMatchDayScoreResponseDTO;
import rugbyniela.entity.dto.coalitionSeasonScore.CoalitionSeasonScoreResponseDTO;
import rugbyniela.entity.dto.user.UserSimpleResponseDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;
import rugbyniela.entity.pojo.Coalition;
import rugbyniela.entity.pojo.CoalitionMatchDayScore;
import rugbyniela.entity.pojo.CoalitionSeasonScore;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserSeasonScore;

@Mapper(
		  componentModel = "spring", // Para poder usar @Autowired UserMapper
		    unmappedTargetPolicy = ReportingPolicy.IGNORE // Ignora campos que no coincidan (útil para password)
	)
public interface ICoalitionMapper {

	CoalitionResponseDTO toDto(Coalition coalition);
	
	UserSimpleResponseDTO userToSimpleDTO(User user);
	
	@Mapping(target="seasonId",source="season.id")
	@Mapping(target="coalitionId",source="coalition.id")
	CoalitionSeasonScoreResponseDTO toCoalitionSeasonScoreDTO(CoalitionSeasonScore coalitionSeasonScore);
	
	@Mapping(target="coalitionSeasonId",source="coalitionSeason.id")
	CoalitionMatchDayScoreResponseDTO toMatchDayScoreDTO(CoalitionMatchDayScore coalitionMatchDayScore);
	
	@Mapping(target="seasonId",source="season.id")
	@Mapping(target="userId",source="user.id")
	@Mapping(target="coalitionId",source="coalition.id")
	UserSeasonScoreResponseDTO toUserSeasonScoreDTO(UserSeasonScore userSeasonScore);
	
	@Mapping(target = "captainName", source = "capitan.name")
    @Mapping(target = "membersCount", expression = "java(coalition.getUserSeasonScores() != null ? coalition.getUserSeasonScores().size() : 0)")
	CoalitionSimpleResponseDTO toSimpleDTO(Coalition coalition);
	
	@Mapping(target = "id",ignore = true)
	@Mapping(target = "capitan",ignore = true)
	@Mapping(target = "coalitionSeasonScores",expression = "java(new java.util.HashSet<>())")
	@Mapping(target = "userSeasonScores",expression = "java(new java.util.HashSet<>())")
	@Mapping(target = "requests",expression = "java(new java.util.HashSet<>())")
	Coalition toEntity(CoalitionRequestDTO dto);
	
}
