package rugbyniela.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rugbyniela.entity.dto.division.DivisionRequestDTO;
import rugbyniela.entity.dto.division.DivisionResponseDTO;
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.Team;

@Mapper(componentModel = "spring")
public interface DivisionMapper {
	
	@Mapping(target = "matchDays", expression = "java(new java.util.HashSet<>())")
	@Mapping(target= "teams", ignore = true)
	Division toEntity(DivisionRequestDTO dto);
	DivisionResponseDTO toDTO(Division division);
	
	
	
	
}
