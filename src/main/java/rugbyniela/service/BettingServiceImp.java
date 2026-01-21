package rugbyniela.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.bet.BetRequestDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketResponseDTO;
import rugbyniela.entity.pojo.Bet;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.MatchDay;
import rugbyniela.entity.pojo.Team;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.entity.pojo.WeeklyBetTicket;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.WeeklyBetTicketMapper;
import rugbyniela.repository.MatchDayRepository;
import rugbyniela.repository.MatchRepository;
import rugbyniela.repository.TeamRepository;
import rugbyniela.repository.UserSeasonScoreRepository;
import rugbyniela.repository.WeeklyBetTicketRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class BettingServiceImp implements IBettingService{
	
	private final UserSeasonScoreRepository userSeasonScoreRepository;
	
	private final MatchDayRepository matchDayRepository;
	
	private final WeeklyBetTicketRepository weeklyBetTicketRepository;
	
	private final MatchRepository matchRepository;
	
	private final TeamRepository teamRepository;
	
	private  final WeeklyBetTicketMapper bettingMapper;
	@Override
	@Transactional
	public WeeklyBetTicketResponseDTO submitTicket(WeeklyBetTicketRequestDTO dto) {
		
		LocalDateTime now = LocalDateTime.now();
		
		// 1. Validate UserSeasonScore exists
		UserSeasonScore userSeason = checkUserSeason(dto.userSeasonId());
				
		// 2. Validate MatchDay exists and fetch matches
		MatchDay matchDay = matchDayRepository.findById(dto.matchDayId())
				.orElseThrow(() -> new RugbyException("No se ha encontrado la jornada", HttpStatus.BAD_REQUEST, ActionType.BETTING));
		
		//Find earliest match start time in this MatchDay
		LocalDateTime earliestMatchStart = matchDay.getMatches().stream()
				.map(Match::getTimeMatchStart)
				.min(LocalDateTime::compareTo)
				.orElseThrow(() -> new RugbyException("No hay partidos en esta jornada", HttpStatus.BAD_REQUEST, ActionType.BETTING));
		
		// 3. Validate predicted leaderboard winner team exists
		Team predictedLeaderboardWinner = teamRepository.findById(dto.predictedLeaderboardWinner())
				.orElseThrow(()-> new RugbyException("No se ha encontrador el equipo elegido como lider", HttpStatus.BAD_REQUEST, ActionType.BETTING));
		
		
		// 4. Retrieve or create WeeklyBetTicket
		Optional<WeeklyBetTicket> ticketThatExists = weeklyBetTicketRepository.findByUserSeasonAndMatchDay(userSeason, matchDay);
				
		if(ticketThatExists.isEmpty()) {
			// makes sure that no matches have started before doing that validation		
			if(earliestMatchStart.isAfter(now)) {
				// 4. Validate that bets cover all matches in matchDay
					Set<Match> matchesInMatchDay = matchDay.getMatches();
					if(dto.bets().size() != matchesInMatchDay.size()) {
						throw new RugbyException("Debe apostar a todos los partidos", HttpStatus.BAD_REQUEST, ActionType.BETTING);
					}
			}
					
					//5. Create weeklybetTicket
					WeeklyBetTicket newTicket = new WeeklyBetTicket();
					newTicket.setUserSeason(userSeason);
					newTicket.setCreationDate(LocalDateTime.now());
					if(!earliestMatchStart.isAfter(now)) {						
						newTicket.setPredictedLeaderBoardWinner(predictedLeaderboardWinner);
					}
					
					// 6. Create Bet for all matches
					for(BetRequestDTO betDTO : dto.bets()) {
						//make sure match exists
						Match match = matchRepository.findById(betDTO.matchId()).orElseThrow(()-> new RugbyException("Partido no encontrado", HttpStatus.BAD_REQUEST, ActionType.BETTING));
						
						//Check current time is before match start time
						if(now.isAfter(match.getTimeMatchStart())) {
							throw new RugbyException("La apuesta al partido de " + match.getName() + "no es valido porque ya ha empezado el partido", HttpStatus.BAD_REQUEST, ActionType.BETTING);
						}
						Bet bet = new Bet();
						bet.setMatch(match);
						bet.setPredictedWinner(teamRepository.findById(betDTO.predictedWinnerId())
								.orElseThrow(() -> new RugbyException("Equipo no encontrado", HttpStatus.BAD_REQUEST, ActionType.BETTING)));
						bet.setBonus(betDTO.bonus()); 
						bet.setPointsAwarded(0);
						newTicket.addBet(bet);
					}
					// 7. Save the ticket (cascade saves bets)
					weeklyBetTicketRepository.save(newTicket);
					return bettingMapper.toDTO(newTicket);
				
		} else {
			
			//8. Update existing ticket
			WeeklyBetTicket existingTicket = ticketThatExists.get();
			
			if(now.isBefore(earliestMatchStart)) {
				existingTicket.setPredictedLeaderBoardWinner(predictedLeaderboardWinner);
			} else {
				log.warn("No se puede modificar el líder predicho porque ya empezó el primer partido.");
			}
			
			for(BetRequestDTO betDTO : dto.bets()) {
				//find existing bet from the selected match
				Optional<Bet> betThatExists = existingTicket.getBets().stream()
						.filter(b -> b.getMatch().getId().equals(betDTO.matchId())).findFirst();
				
				if(betThatExists.isPresent()) {
					Bet existingBet = betThatExists.get();
					
					//Check bet is made before the time of the match when updating
					if(now.isBefore(existingBet.getMatch().getTimeMatchStart())) {
						existingBet.setPredictedWinner(teamRepository.findById(betDTO.predictedWinnerId()).orElseThrow(()-> new RugbyException("Equipo no encontrado", HttpStatus.BAD_REQUEST, ActionType.BETTING)));
						existingBet.setBonus(betDTO.bonus());
						existingBet.setPointsAwarded(0);
					} else {
						log.warn("No se puede modificar apuesta para el partido (ID {}) porque ya comenzó.", betDTO.matchId());
//						throw new RugbyException("La apuesta al partido de " + existingBet.getMatch().getName() + "no es valido porque ya ha empezado el partido", HttpStatus.BAD_REQUEST, ActionType.BETTING);
					}
					
				}
			}
			//Save new updated ticket
			weeklyBetTicketRepository.save(existingTicket);
			return bettingMapper.toDTO(existingTicket);
		}
		
		
		// 8. Return some response DTO (you can create a mapper for this)
	}


	// TODO: Si no se usa hay que borrarlo
	@Override
	public void cancelTicket() {
		
	}

	@Override
	@Transactional()
	public Page<WeeklyBetTicket> fetchUserSeasonTickets(Long userSeasonId, int page) {
		//Validate userSeasonScore exists
		UserSeasonScore userSeasonScore = checkUserSeason(userSeasonId);
		if (page < 0) {
	        throw new RugbyException("La pagina no puede ser negativa", HttpStatus.BAD_REQUEST, ActionType.BETTING);
	    }
		
		//Create pageable: 10 tickets, newest first
		Pageable pageable = PageRequest.of(page, 10, Sort.by("creationDate").descending());
		
		return weeklyBetTicketRepository.findByUserSeason(userSeasonScore, pageable);
	
	}

	
	@Transactional(readOnly = true)
	@Override
	public WeeklyBetTicket fetchUserSeasonTicketByMatchDay(Long userSeasonId, Long matchDayId) {
		UserSeasonScore userSeasonScore = checkUserSeason(userSeasonId);//Validate match day exists
		MatchDay matchDay = matchDayRepository.findById(matchDayId)
				.orElseThrow(()-> new RugbyException("Jornada no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		//crear pageable
		//buscar tickets en el return
		WeeklyBetTicket ticket = weeklyBetTicketRepository.findByUserSeasonAndMatchDay(userSeasonScore, matchDay)
				.orElseThrow(()-> new RugbyException("No existe ticket para esta jornada", HttpStatus.NOT_FOUND, ActionType.BETTING));
		
		return ticket;
		
		
	}
	
	public UserSeasonScore checkUserSeason (Long userSeasonId) {
		UserSeasonScore userSeasonScore = userSeasonScoreRepository.findById(userSeasonId)
				.orElseThrow(()-> new RugbyException("Usuario no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return userSeasonScore;
		
	}

}
