package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rugbyniela.entity.dto.bet.BetRequestDTO;
import rugbyniela.entity.dto.bet.BetResponseDTO;
import rugbyniela.entity.pojo.Bet;

@Mapper(componentModel = "spring")
public interface IBetMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "betCorrect", ignore = true)
	@Mapping(target = "pointsAwarded", ignore = true)
	@Mapping(target = "weeklyBetTicket", ignore = true)
	@Mapping(target="match", ignore = true)
	@Mapping(target="predictedWinner", ignore = true)
	Bet toEntity(BetRequestDTO dto);
	
	@Mapping(source="match.id", target = "matchId")
	@Mapping(source="predictedWinner.id", target = "predictedWinnerId")
	@Mapping(source="weeklyBetTicket.id", target = "weeklyBetTicketId")
	BetResponseDTO toDTO(Bet bet);
}
