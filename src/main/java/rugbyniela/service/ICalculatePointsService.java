package rugbyniela.service;

import rugbyniela.entity.pojo.MatchDay;
import rugbyniela.entity.pojo.Season;

public interface ICalculatePointsService {

	void calculatePointsByBet(Long betId);
	
	int calculatePointsByWeeklyBetTicket(Long weeklyBetTicketId);
	
	int calculateTotalPoints();
	
	void calculateMatchDayPoints(Long matchDayId);
	
//	void calculateTeamDivisionScore(Long division);
}
