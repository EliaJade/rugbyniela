package rugbyniela.entity.dto.bet;

import rugbyniela.enums.Bonus;

//for both add and update
public record BetRequestDTO(
		Long predictedWinnerId,
		Long matchId,
		Bonus bonus) {

}
