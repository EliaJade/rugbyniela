package rugbyniela.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.bet.BetResponseDTO;
import rugbyniela.entity.dto.divisionBet.DivisionBetDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketResponseDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketUpdateRequestDTO;
import rugbyniela.entity.pojo.Bet;
import rugbyniela.entity.pojo.DivisionBet;
import rugbyniela.entity.pojo.WeeklyBetTicket;


@Mapper(
		componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE 
		)
public interface WeeklyBetTicketMapper {

//		@Mapping(target= "predictedLeaderboardWinner", source= "predictedLeaderBoardWinner.id")
		@Mapping(source = "userSeason.id", target = "userSeasonId")
	   WeeklyBetTicketResponseDTO toDTO (WeeklyBetTicket ticket);
		
//		@Mapping(target= "predictedLeaderBoardWinner.id", source="predictedLeaderboardWinner")
		@Mapping(target = "userSeason.id", source = "userSeasonId")
		@Mapping(target = "creationDate", ignore = true)
		@Mapping(target = "coalitionAtBetTime", ignore = true)
		@Mapping(target = "bets", expression = "java(new java.util.HashSet<>())")
		@Mapping(target = "divisionBets", expression = "java(new java.util.HashSet<>())")
		WeeklyBetTicket toEntity(WeeklyBetTicketRequestDTO dto);
		
		@Mapping(source="match.id", target = "matchId")
		@Mapping(source="predictedWinner.id", target = "predictedWinnerId")
		@Mapping(source="weeklyBetTicket.id", target = "weeklyBetTicketId")
		BetResponseDTO toDTO(Bet bet);
		
		@Mapping(source="division.id", target = "divisionId")
		@Mapping(source="predictedLeader.id", target = "predictedLeaderboardWinnerId")
		DivisionBetDTO toDTO(DivisionBet divisionBet);
}
