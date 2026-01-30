package rugbyniela.service;

import org.springframework.data.domain.Page;

import rugbyniela.entity.dto.season.SeasonRequestDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreRequestDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketResponseDTO;
import rugbyniela.entity.pojo.WeeklyBetTicket;


public interface IBettingService {

	WeeklyBetTicketResponseDTO submitTicket(WeeklyBetTicketRequestDTO dto);

	void cancelTicket();
	
	UserSeasonScoreResponseDTO participateInSeason(UserSeasonScoreRequestDTO userScoreDTO);

	Page<WeeklyBetTicket> fetchUserSeasonTickets(Long userSeasonId, int page);

	WeeklyBetTicket fetchUserSeasonTicketByMatchDay(Long userSeasonId, Long matchDayId);
}
