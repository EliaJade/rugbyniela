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
import rugbyniela.entity.dto.season.SeasonRequestDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreRequestDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketResponseDTO;
import rugbyniela.entity.pojo.Bet;
import rugbyniela.entity.pojo.Coalition;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.MatchDay;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.Team;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.entity.pojo.WeeklyBetTicket;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.IUserSeasonScoreMaper;
import rugbyniela.mapper.WeeklyBetTicketMapper;
import rugbyniela.repository.BetRepository;
import rugbyniela.repository.CoalitionRepository;
import rugbyniela.repository.MatchDayRepository;
import rugbyniela.repository.MatchRepository;
import rugbyniela.repository.SeasonRepository;
import rugbyniela.repository.TeamRepository;
import rugbyniela.repository.UserRepository;
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
	private final BetRepository betRepository;
	private final SeasonRepository seasonRepository;
	private final UserRepository userRepository;
	private final CoalitionRepository coalitionRepository;
	
	private  final WeeklyBetTicketMapper weeklyBetTicketMapper;
	private final IUserSeasonScoreMaper userSeasonScoreMapper;
	
	@Transactional
	@Override
	public UserSeasonScoreResponseDTO participateInSeason(UserSeasonScoreRequestDTO dto) {
		Season season = checkSeason(dto.seasonId());
		User user = checkUser(dto.userId());
//check user and season are active
		UserSeasonScore userScore= userSeasonScoreMapper.toEntity(dto);
		log.debug("User Season Score as entity: {}, {}, {}, {}, {}", userScore.getCoalition(), userScore.getIsActive(), userScore.getSeason().getName(), userScore.getUser().getName(), userScore.getTotalPoints());
		if(season.getSeasonParticipants().stream().anyMatch(seasonParticipant -> seasonParticipant.getUser().getId().equals(user.getId()))) {
			throw new RugbyException("Ya estas participando en esta temporada", HttpStatus.BAD_REQUEST, ActionType.REGISTRATION);
		}
		if(userScore.getIsActive()==null) {
			userScore.setIsActive(true);
		}
		userScore.setUser(user);
		userScore.setSeason(season);
		season.addParticipant(userScore);
		
		if(user.getCurrentCoalition()!=null) {
			Coalition coalition = checkCoalition(user.getCurrentCoalition().getId());
			userScore.setCoalition(coalition);
			
		}

		userSeasonScoreRepository.save(userScore);
		
		log.debug("User Season Score as entity: {}, {}, {}, {}, {}", userScore.getCoalition(), userScore.getIsActive(), userScore.getSeason().getName(), userScore.getUser().getName(), userScore.getTotalPoints());
		
		
		
		return userSeasonScoreMapper.toDto(userScore);
	}


	@Override
	@Transactional
	public WeeklyBetTicketResponseDTO submitTicket(WeeklyBetTicketRequestDTO dto) {
		
		UserSeasonScore userScore = checkUserSeason(dto.userSeasonId());
		
		
		
		return null;
		
		
//		LocalDateTime now = LocalDateTime.now();
//		log.debug("Time now: " + now);
//		// 1. Validate UserSeasonScore exists
//		UserSeasonScore userSeason = checkUserSeason(dto.userSeasonId());
//		log.debug("User: " + userSeason.getUser().getName());
//		// 2. Validate MatchDay exists and fetch matches
//		MatchDay matchDay = matchDayRepository.findById(dto.matchDayId())
//				.orElseThrow(() -> new RugbyException("No se ha encontrado la jornada", HttpStatus.BAD_REQUEST, ActionType.BETTING));
//		log.debug("Match Day: " + matchDay.getName());
//		//Find earliest match start time in this MatchDay
//		LocalDateTime earliestMatchStart = matchDay.getMatches().stream()
//				.map(Match::getTimeMatchStart)
//				.min(LocalDateTime::compareTo)
//				.orElseThrow(() -> new RugbyException("No hay partidos en esta jornada", HttpStatus.BAD_REQUEST, ActionType.BETTING));
//		log.debug("Earliest Match of jornada: " + earliestMatchStart);
//		// 3. Validate predicted leaderboard winner team exists
//		Team predictedWinner = teamRepository.findById(dto.predictedLeaderboardWinner())
//				.orElseThrow(() -> new RugbyException("No se ha encontrado el equipo", HttpStatus.BAD_REQUEST, ActionType.BETTING));
////		Long winnerId = dto.predictedLeaderboardWinner();
////		        .stream()
////		        .findFirst()
////		        .orElseThrow(() -> new RugbyException(
////		                "No hay apuesta de división",
////		                HttpStatus.BAD_REQUEST,
////		                ActionType.USER_ACTION
////		        ))
////		        .predictedLeaderboardWinnerId();
//
//		log.debug("Winner Id: " + predictedWinner.getName());
////		Team predictedLeaderboardWinner =
////		        teamRepository.findById(winnerId)
////		                .orElseThrow(() -> new RugbyException(
////		                        "Equipo ganador no encontrado",
////		                        HttpStatus.NOT_FOUND,
////		                        ActionType.USER_ACTION
////		                ));
////		log.debug("PredictedLeaderboardWinner: " + predictedLeaderboardWinner.getName());
//		
//		// 4. Retrieve or create WeeklyBetTicket
//		Optional<WeeklyBetTicket> ticketThatExists = weeklyBetTicketRepository.findByUserSeasonAndMatchDay(userSeason, matchDay);
//		log.debug("ticketThatExists: " + ticketThatExists);		
//		if(ticketThatExists.isEmpty()) {
//			// makes sure that no matches have started before doing that validation		
//			if(earliestMatchStart.isAfter(now)) {
//				log.debug("Matches start before now: " + earliestMatchStart.isAfter(now));				
//				// 4. Validate that bets cover all matches in matchDay
//					Set<Match> matchesInMatchDay = matchDay.getMatches();
//					if(dto.bets().size() != matchesInMatchDay.size()) {
//						log.debug("How many matches did you bet on: " + dto.bets().size());	
//						log.debug("How many matches should you have bet on: " + matchesInMatchDay.size());
//						throw new RugbyException("Debe apostar a todos los partidos", HttpStatus.BAD_REQUEST, ActionType.BETTING);
//					}
//			}
//					
//					//5. Create weeklybetTicket
//					WeeklyBetTicket newTicket = new WeeklyBetTicket();
//					newTicket.setUserSeason(userSeason);
//					newTicket.setCreationDate(LocalDateTime.now());
//					if(earliestMatchStart.isAfter(now)) {						
//						newTicket.setPredictedLeaderBoardWinner(predictedWinner);
//						log.debug("newTicketId with predictedWinnerLeaderBoard {}, {}, {}, ", newTicket.getCreationDate(), newTicket.getPredictedLeaderBoardWinner().getName(), newTicket.getUserSeason().getUser().getName()  );
//						
//					}
//					log.debug("newTicketId {}, {}, ", newTicket.getCreationDate(),  newTicket.getUserSeason().getUser().getName()  );
//					
//					
//					// 6. Create Bet for all matches
//					for(BetRequestDTO betDTO : dto.bets()) {
//						//make sure match exists
//						Match match = matchRepository.findById(betDTO.matchId()).orElseThrow(()-> new RugbyException("Partido no encontrado", HttpStatus.BAD_REQUEST, ActionType.BETTING));
//						log.debug("Match: " + match.getName());
//						//Check current time is before match start time
//						if(now.isAfter(match.getTimeMatchStart())) {
//							throw new RugbyException("La apuesta al partido de " + match.getName() + "no es valido porque ya ha empezado el partido", HttpStatus.BAD_REQUEST, ActionType.BETTING);
//						}
//						Bet bet = new Bet();
//						log.debug("Bet: " + bet.getId());
//						bet.setMatch(match);
//						bet.setPredictedWinner(teamRepository.findById(betDTO.predictedWinnerId())
//								.orElseThrow(() -> new RugbyException("Equipo no encontrado", HttpStatus.BAD_REQUEST, ActionType.BETTING)));
//						bet.setBonus(betDTO.bonus()); 
//						bet.setPointsAwarded(0);
//						log.debug("Bet: " + bet.getId());
//						betRepository.save(bet);
//
//						log.debug("Bet after save: " + bet.getId());
//						newTicket.addBet(bet);
//					}
//					// 7. Save the ticket (cascade saves bets)
//					weeklyBetTicketRepository.save(newTicket);
//					//return bettingMapper.toDTO(newTicket);
//					return weeklyBetTicketMapper.toDTO(newTicket);
//				
//		} else {
//			
//			//8. Update existing ticket
//			WeeklyBetTicket existingTicket = ticketThatExists.get();
//			
//			if(now.isBefore(earliestMatchStart)) {
//				existingTicket.setPredictedLeaderBoardWinner(predictedWinner);
//			} else {
//				log.warn("No se puede modificar el líder predicho porque ya empezó el primer partido.");
//			}
//			
//			for(BetRequestDTO betDTO : dto.bets()) {
//				//find existing bet from the selected match
//				Optional<Bet> betThatExists = existingTicket.getBets().stream()
//						.filter(b -> b.getMatch().getId().equals(betDTO.matchId())).findFirst();
//				
//				if(betThatExists.isPresent()) {
//					Bet existingBet = betThatExists.get();
//					
//					//Check bet is made before the time of the match when updating
//					if(now.isBefore(existingBet.getMatch().getTimeMatchStart())) {
//						existingBet.setPredictedWinner(teamRepository.findById(betDTO.predictedWinnerId()).orElseThrow(()-> new RugbyException("Equipo no encontrado", HttpStatus.BAD_REQUEST, ActionType.BETTING)));
//						existingBet.setBonus(betDTO.bonus());
//						existingBet.setPointsAwarded(0);
//					} else {
//						log.warn("No se puede modificar apuesta para el partido (ID {}) porque ya comenzó.", betDTO.matchId());
////						throw new RugbyException("La apuesta al partido de " + existingBet.getMatch().getName() + "no es valido porque ya ha empezado el partido", HttpStatus.BAD_REQUEST, ActionType.BETTING);
//					}
//					
//				}
//			}
//			//Save new updated ticket
//			weeklyBetTicketRepository.save(existingTicket);
//			//return bettingMapper.toDTO(existingTicket);
//			return weeklyBetTicketMapper.toDTO(existingTicket);
//		}
//		
//		
//		// 8. Return some response DTO (you can create a mapper for this)
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
				.orElseThrow(()-> new RugbyException("Usuario score no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return userSeasonScore;
		
	}
	
	public Season checkSeason (Long id) {
		Season season = seasonRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Temporada no encontrada", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return season;
		
	}

	public User checkUser (Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Usuario no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return user;
		
	}
	
	public Coalition checkCoalition (Long id) {
		Coalition coalition = coalitionRepository.findById(id)
				.orElseThrow(()-> new RugbyException("coalicion no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return coalition;
		
	}

}
