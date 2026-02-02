package rugbyniela.mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.coalition.CoalitionActiveMemberResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionJoinResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionSimpleResponseDTO;
import rugbyniela.entity.dto.coalitionSeasonScore.CoalitionMatchDayScoreResponseDTO;
import rugbyniela.entity.dto.coalitionSeasonScore.CoalitionSeasonScoreResponseDTO;
import rugbyniela.entity.dto.user.UserSimpleResponseDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;
import rugbyniela.entity.pojo.Coalition;
import rugbyniela.entity.pojo.CoalitionMatchDayScore;
import rugbyniela.entity.pojo.CoalitionRequest;
import rugbyniela.entity.pojo.CoalitionSeasonScore;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserSeasonScore;

@Mapper(
		  componentModel = "spring", // Para poder usar @Autowired UserMapper
		    unmappedTargetPolicy = ReportingPolicy.IGNORE // Ignora campos que no coincidan (útil para password)
	)
public interface ICoalitionMapper {

	@Mapping(target = "userSeasonScores", expression = "java(mapMembers(coalition))")
	CoalitionResponseDTO toDto(Coalition coalition);
	
	UserSimpleResponseDTO userToSimpleDTO(User user);
	
	@Mapping(target="seasonId",source="season.id")
	@Mapping(target="coalitionId",source="coalition.id")
	CoalitionSeasonScoreResponseDTO toCoalitionSeasonScoreDTO(CoalitionSeasonScore coalitionSeasonScore);
	
	@Mapping(target="coalitionSeasonId",source="coalitionSeason.id")
	CoalitionMatchDayScoreResponseDTO toMatchDayScoreDTO(CoalitionMatchDayScore coalitionMatchDayScore);
	
//	@Mapping(target="seasonId",source="season.id")
//	@Mapping(target="userId",source="user.id")
//	@Mapping(target="coalitionId",source="coalition.id")
//	UserSeasonScoreResponseDTO toUserSeasonScoreDTO(UserSeasonScore userSeasonScore);
	
	@Mapping(target = "captainName", source = "capitan.name")
    @Mapping(target = "membersCount", expression = "java(coalition.getMembers() != null ? coalition.getMembers().size() : 0)")
	CoalitionSimpleResponseDTO toSimpleDTO(Coalition coalition);
	
	@Mapping(target="name",source="user.name")
	@Mapping(target="coalitionId",source="coalition.id")
	CoalitionJoinResponseDTO toCoalitionJoinResponseDTO(CoalitionRequest coalitionRequest);
	
	@Mapping(target = "id",ignore = true)
	@Mapping(target = "capitan",ignore = true)
	@Mapping(target = "coalitionSeasonScores",expression = "java(new java.util.HashSet<>())")
	@Mapping(target = "userSeasonScores",expression = "java(new java.util.HashSet<>())")
	@Mapping(target = "requests",expression = "java(new java.util.HashSet<>())")
	Coalition toEntity(CoalitionRequestDTO dto);
	
	
	// Método default para fusionar usuarios con sus puntos
    default Set<CoalitionActiveMemberResponseDTO> mapMembers(Coalition coalition) {
        if (coalition.getMembers() == null) return new HashSet<>();

        Set<CoalitionActiveMemberResponseDTO> result = new HashSet<>();
        
        // 1. Convertimos la lista de Scores en un Mapa para búsqueda rápida
        // Clave: UserID, Valor: Puntos
        Map<Long, Integer> pointsMap = new HashMap();
        if (coalition.getUserSeasonScores() != null) {
            for (UserSeasonScore score : coalition.getUserSeasonScores()) {
                 // OJO: Aquí filtramos solo los de la temporada activa si la entidad trajo todas
                 // Asumiendo que tu repositorio ya filtró por temporada activa o que 
                 // quieres mostrar los puntos de la temporada actual de la coalición.
                 if(score.getSeason().getIsActive()) {
                     pointsMap.put(score.getUser().getId(), score.getTotalPoints());
                 }
            }
        }
        Long captainId = (coalition.getCapitan() != null) ? coalition.getCapitan().getId() : -1L;
        // 2. Iteramos sobre los miembros (Usuarios reales)
        for (User user : coalition.getMembers()) {
            Integer points = pointsMap.get(user.getId());
            boolean isRegistered = points != null;

            result.add(new CoalitionActiveMemberResponseDTO(
                user.getId(),
                user.getNickname(),
                user.getId().equals(captainId) ? "CAPITAN" : "MIEMBRO",
                user.getProfilePictureUrl(),
                isRegistered ? points : 0, // Si tiene puntos, ponlos. Si no, 0.
                isRegistered
            ));
        }
        return result;
    }
}
