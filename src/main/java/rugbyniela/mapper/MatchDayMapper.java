package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rugbyniela.entity.dto.matchDay.MatchDayRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayResponseDTO;
import rugbyniela.entity.pojo.MatchDay;

@Mapper(componentModel = "spring")
public interface MatchDayMapper {

	MatchDay toEntity(MatchDayRequestDTO dto);
	@Mapping(target = "matches", expression = "java(new java.util.HashSet<>())")
	MatchDayResponseDTO toDTO(MatchDay matchDay);
}
