package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.pojo.Coalition;

@Mapper(
		  componentModel = "spring", // Para poder usar @Autowired UserMapper
		    unmappedTargetPolicy = ReportingPolicy.IGNORE // Ignora campos que no coincidan (útil para password)
	)
public interface ICoalitionMapper {

	@Mapping(target = "id",ignore = true)
	@Mapping(target = "capitan",ignore = true)
	@Mapping(target = "coalitionSeasonScores",expression = "java(new java.util.HashSet<>())")
	@Mapping(target = "userSeasonScores",expression = "java(new java.util.HashSet<>())")
	@Mapping(target = "requests",expression = "java(new java.util.HashSet<>())")
	Coalition toEntity(CoalitionRequestDTO dto);
}
