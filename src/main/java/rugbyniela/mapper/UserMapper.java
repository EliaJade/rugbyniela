package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.pojo.User;


@Mapper(
	    componentModel = "spring", // Para poder usar @Autowired UserMapper
	    unmappedTargetPolicy = ReportingPolicy.IGNORE // Ignora campos que no coincidan (útil para password)
	)
public interface UserMapper {
//
//	// ==========================================
//    // 1. User -> UserResponseDTO
//    // ==========================================
	
		User toEntity(UserRequestDTO dto);
		UserResponseDTO toDTO(User entity);
	
//    @Mapping(source = "seasonScores", target = "userSeasonScore")
//    UserResponseDTO toUserDto(User entity);
//
//    AddressDTO toAddressDto(Address entity);
//
//    // ==========================================
//    // 2. UserSeasonScore -> UserSeasonScoreResponseDTO
//    // ==========================================
//    @Mapping(source = "season.id", target = "seasonId")
//    @Mapping(source = "user.id", target = "userId")
//    @Mapping(source = "coalition.id", target = "coalitionId")
//    @Mapping(source = "tickets", target = "bettingTickets") 
//    @Mapping(source = "matchDayScores", target = "scorePerMatches") 
//    UserSeasonScoreResponseDTO toScoreDto(UserSeasonScore entity);
//
//
//    // ==========================================
//    // 3. WeeklyBetTicket -> WeeklyBetTicketResponseDTO
//    // ==========================================
//    @Mapping(source = "userSeason.id", target = "userSeasonId")
//    WeeklyBetTicketResponseDTO toTicketDto(WeeklyBetTicket entity);
//
//    // ==========================================
//    // 4. UserMatchDayScore -> UserMatchDayScoreDTO
//    // ==========================================
//    @Mapping(source = "userSeason.id", target = "userSeasonId")
//    UserMatchDayScoreDTO toMatchDayScoreDto(UserMatchDayScore entity);
//
//
//    // ==========================================
//    // 5. Bet -> BetDTO
//    // ==========================================
//    @Mapping(source = "weeklyBetTicket.id", target = "weeklyBetTicketId")
//    @Mapping(source = "predictedWinner.id", target = "predictedWinnerTeamId")
//    BetDTO toBetDto(Bet entity);
//
//
//    // ==========================================
//    // 6. Match -> MatchResponseDTO
//    // ==========================================
//    // CORREGIDO: Eliminamos los mappings de 'address' y 'timeDate' porque ya coinciden los nombres
//    @Mapping(source = "matchDay.id", target = "matchDayId")
//    MatchResponseDTO toMatchDto(Match entity);
//
//    // ==========================================
//    // 7. Team -> TeamDTO
//    // ==========================================
//    TeamDTO toTeamDto(Team entity);
}
