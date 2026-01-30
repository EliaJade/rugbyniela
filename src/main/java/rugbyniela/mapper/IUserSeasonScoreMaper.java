package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreRequestDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;
import rugbyniela.entity.pojo.User;
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
	
	@Mapping(target = "season.id", source = "seasonId")
	@Mapping(target = "user.id", source = "userId")
	@Mapping(target = "matchDayScores", expression = "java(new java.util.HashSet<>())")
	@Mapping(target = "tickets", expression = "java(new java.util.HashSet<>())")
	UserSeasonScore toEntity (UserSeasonScoreRequestDTO dto);
}
