package rugbyniela.entity.dto.bet;

//for both add and update
public record BetRequestDTO(
		Long predictedWinnerId,
		Long matchId,
		String bonus) {

}
