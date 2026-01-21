package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;
import rugbyniela.entity.pojo.UserSeasonScore;

@Mapper(
		  componentModel = "spring",
		    unmappedTargetPolicy = ReportingPolicy.IGNORE
	)
public interface IUserSeasonScoreMaper {

	@Mapping(target = "seasonId",source = "season.id")
	@Mapping(target = "userId",source = "user.id")
	@Mapping(target = "coalitionId",source = "coalition.id")
	@Mapping(target = "coalitionName",source = "coalition.name")
	UserSeasonScoreResponseDTO toDto(UserSeasonScore userSeasonScore);
}
