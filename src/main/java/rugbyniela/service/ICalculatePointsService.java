package rugbyniela.service;

import rugbyniela.entity.dto.bet.BetRequestDTO;

public interface ICalculatePointsService {

	void calculatePointsByBet(Long betId);
	
	int calculatePointsByWeeklyBetTicket(Long weeklyBetTicketId);
	
	int calculateTotalPoints();
}
