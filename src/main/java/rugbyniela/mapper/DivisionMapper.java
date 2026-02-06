package rugbyniela.mapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;

import rugbyniela.entity.dto.division.DivisionRequestDTO;
import rugbyniela.entity.dto.division.DivisionResponseDTO;
import rugbyniela.entity.dto.teamDivisionScore.TeamDivisionScoreResponseDTO;
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.Team;
import rugbyniela.entity.pojo.TeamDivisionScore;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.Category;
import rugbyniela.enums.Gender;
import rugbyniela.exception.RugbyException;

@Mapper(componentModel = "spring")
public interface DivisionMapper {
	
	@Mapping(target = "matchDays", expression = "java(new java.util.HashSet<>())")
	@Mapping(target = "teamDivisionScores", expression = "java(new java.util.HashSet<>())")
	@Mapping(target = "isActive",constant = "true")
	@Mapping(target = "category",source = "category",qualifiedByName = "mapCategory")
	Division toEntity(DivisionRequestDTO dto);
	
	@Mapping(target="seasonId",source = "season.id")
	@Mapping(target = "teams",source = "teamDivisionScores")
	DivisionResponseDTO toDTO(Division division);
	
	@Mapping(target = "seasonId",source = "season.id")
	@Mapping(target = "divisionId",source = "division.id")
	@Mapping(target = "teamId",source="team.id")
	@Mapping(target = "name",source = "team.name")
	@Mapping(target = "teamPictureUrl",source="team.url")
	TeamDivisionScoreResponseDTO toTeamDivisionResponseDTo(TeamDivisionScore teamDivisionScore);
	
	default Set<Division> toEntitySet(Set<DivisionRequestDTO> dtos){
		
	 if (dtos == null) {
         return new HashSet<>();
     }
     return dtos.stream()
             .map(this::toEntity)
             .collect(Collectors.toSet());
	}
 
	 @Named("mapCategory")
	default Category mapCategory(String category) {
		 if (category == null || category.isBlank()) {
	            return null; 
	        }
	        try {
	            return Category.valueOf(category.trim().toUpperCase());
	        } catch (IllegalArgumentException e) {
	            throw new RugbyException(
	                "La categoria'" + category + "' no es válida. Valores permitidos: " + Arrays.toString(Category.values()),
	                HttpStatus.BAD_REQUEST,
	                ActionType.REGISTRATION // O ActionType.VALIDATION
	            );
	        }
	}
	
	
}
