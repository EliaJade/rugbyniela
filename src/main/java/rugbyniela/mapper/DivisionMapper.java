package rugbyniela.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rugbyniela.entity.dto.division.DivisionRequestDTO;
import rugbyniela.entity.dto.division.DivisionResponseDTO;
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.Team;

@Mapper(componentModel = "spring")
public interface DivisionMapper {
	
	@Mapping(target = "matchDays", expression = "java(new java.util.HashSet<>())")
	Division toEntity(DivisionRequestDTO dto);
	
	@Mapping(target="seasonId",source = "season.id")
	DivisionResponseDTO toDTO(Division division);
	
	default Set<Division> toEntitySet(Set<DivisionRequestDTO> dtos){
		
	 if (dtos == null) {
         return new HashSet<>();
     }
     return dtos.stream()
             .map(this::toEntity)
             .collect(Collectors.toSet());
	}
 
	
	
	
}
