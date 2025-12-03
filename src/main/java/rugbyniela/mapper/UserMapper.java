package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.AddressDTO;
import rugbyniela.entity.dto.BetDTO;
import rugbyniela.entity.dto.MatchResponseDTO;
import rugbyniela.entity.dto.TeamDTO;
import rugbyniela.entity.dto.UserMatchDayScoreDTO;
import rugbyniela.entity.dto.UserResponseDTO;
import rugbyniela.entity.dto.UserSeasonScoreResponseDTO;
import rugbyniela.entity.dto.WeeklyBetTicketResponseDTO;
import rugbyniela.entity.pojo.Address;
import rugbyniela.entity.pojo.Bet;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.Team;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserMatchDayScore;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.entity.pojo.WeeklyBetTicket;


@Mapper(
	    componentModel = "spring", // Para poder usar @Autowired UserMapper
	    unmappedTargetPolicy = ReportingPolicy.IGNORE // Ignora campos que no coincidan (útil para password)
	)
public interface UserMapper {

	// ==========================================
    // 1. User -> UserResponseDTO
    // ==========================================
    @Mapping(source = "seasonScores", target = "userSeasonScore")
    UserResponseDTO toUserDto(User entity);

    AddressDTO toAddressDto(Address entity);

    // ==========================================
    // 2. UserSeasonScore -> UserSeasonScoreResponseDTO
    // ==========================================
    @Mapping(source = "season.id", target = "seasonId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "coalition.id", target = "coalitionId")
    @Mapping(source = "tickets", target = "bettingTickets") 
    @Mapping(source = "matchDayScores", target = "scorePerMatches") 
    UserSeasonScoreResponseDTO toScoreDto(UserSeasonScore entity);


    // ==========================================
    // 3. WeeklyBetTicket -> WeeklyBetTicketResponseDTO
    // ==========================================
    @Mapping(source = "userSeason.id", target = "userSeasonId")
    WeeklyBetTicketResponseDTO toTicketDto(WeeklyBetTicket entity);

    // ==========================================
    // 4. UserMatchDayScore -> UserMatchDayScoreDTO
    // ==========================================
    @Mapping(source = "userSeason.id", target = "userSeasonId")
    UserMatchDayScoreDTO toMatchDayScoreDto(UserMatchDayScore entity);


    // ==========================================
    // 5. Bet -> BetDTO
    // ==========================================
    @Mapping(source = "weeklyBetTicket.id", target = "weeklyBetTicketId")
    @Mapping(source = "predictedWinner.id", target = "predictedWinnerTeamId")
    BetDTO toBetDto(Bet entity);


    // ==========================================
    // 6. Match -> MatchResponseDTO
    // ==========================================
    // CORREGIDO: Eliminamos los mappings de 'address' y 'timeDate' porque ya coinciden los nombres
    @Mapping(source = "matchDay.id", target = "matchDayId")
    MatchResponseDTO toMatchDto(Match entity);

    // ==========================================
    // 7. Team -> TeamDTO
    // ==========================================
    TeamDTO toTeamDto(Team entity);
}
