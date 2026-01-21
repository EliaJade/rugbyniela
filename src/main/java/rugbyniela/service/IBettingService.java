package rugbyniela.service;

import org.springframework.data.domain.Page;

import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketResponseDTO;
import rugbyniela.entity.pojo.WeeklyBetTicket;


public interface IBettingService {

	WeeklyBetTicketResponseDTO submitTicket(WeeklyBetTicketRequestDTO dto);

	void cancelTicket();

	Page<WeeklyBetTicket> fetchUserSeasonTickets(Long userSeasonId, int page);

	WeeklyBetTicket fetchUserSeasonTicketByMatchDay(Long userSeasonId, Long matchDayId);
}
