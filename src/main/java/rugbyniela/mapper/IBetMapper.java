package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rugbyniela.entity.dto.address.AddressRequestDTO;
import rugbyniela.entity.dto.address.AddressResponseDTO;
import rugbyniela.entity.dto.bet.BetRequestDTO;
import rugbyniela.entity.dto.bet.BetResponseDTO;
import rugbyniela.entity.pojo.Address;
import rugbyniela.entity.pojo.Bet;

@Mapper(componentModel = "spring")
public interface IBetMapper {

	
	@Mapping(target="match.id", source = "matchId")
	@Mapping(target="predictedWinner.id", source = "predictedWinnerId")
	Bet toEntity(BetRequestDTO dto);
	
	@Mapping(source="match.id", target = "matchId")
	@Mapping(source="predictedWinner.id", target = "predictedWinnerId")
	@Mapping(source="weeklyBetTicket.id", target = "weeklyBetTicketId")
	BetResponseDTO toDTO(Bet bet);
}
