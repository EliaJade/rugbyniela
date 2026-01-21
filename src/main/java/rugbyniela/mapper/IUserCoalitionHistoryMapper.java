package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.userSeasonScore.UserCoalitionHistoryResponseDTO;
import rugbyniela.entity.pojo.UserSeasonScore;

@Mapper(
	    componentModel = "spring", // Para poder usar @Autowired UserMapper
	    unmappedTargetPolicy = ReportingPolicy.IGNORE // Ignora campos que no coincidan (útil para password)
	)
public interface IUserCoalitionHistoryMapper {
	
	@Mapping(target = "coalitionName",
			 expression = "java(userSeasonScore.getCoalition() != null ? userSeasonScore.getCoalition().getName() : null)")
	@Mapping(target = "seasonName",source="season.name")
	@Mapping(target = "score",source="totalPoints")
	@Mapping(target = "seasonId",source="season.id")
	UserCoalitionHistoryResponseDTO toHistoryDTO(UserSeasonScore userSeasonScore);
}
