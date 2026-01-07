package rugbyniela.service;

import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;

public interface BettingService {

	void submitTicket(WeeklyBetTicketRequestDTO dto);

	void cancelTicket();

	void fetchUserSeasonTickets();

	void fetchUserSeasonTicketsByMatchDay();
}
