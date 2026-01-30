package rugbyniela.mapper;

import java.beans.BeanProperty;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import rugbyniela.entity.dto.season.SeasonRequestDTO;
import rugbyniela.entity.dto.season.SeasonResponseDTO;
import rugbyniela.entity.dto.season.SeasonUpdateRequestDTO;
import rugbyniela.entity.pojo.Season;

@Mapper(componentModel = "spring")
public interface SeasonMapper {
	

	void updateSeasonFromDTO(SeasonUpdateRequestDTO dto, @MappingTarget Season season);
	
	@Mapping(target = "divisions", expression = "java(new java.util.HashSet<>())")
	Season toEntity(SeasonRequestDTO dto);
	
	SeasonResponseDTO toDTO(Season season);
	
}
