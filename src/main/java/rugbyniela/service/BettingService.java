package rugbyniela.service;

import org.springframework.data.domain.Page;

import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.pojo.WeeklyBetTicket;

public interface BettingService {

	void submitTicket(WeeklyBetTicketRequestDTO dto);

	void cancelTicket();

	Page<WeeklyBetTicket> fetchUserSeasonTickets(Long userSeasonId, int page);

	WeeklyBetTicket fetchUserSeasonTicketsByMatchDay(Long userSeasonId, Long matchDayId);
}
