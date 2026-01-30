package rugbyniela.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.DivisionBet;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.MatchDay;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.Team;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.entity.pojo.WeeklyBetTicket;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.IBetMapper;
import rugbyniela.mapper.IUserSeasonScoreMaper;
import rugbyniela.mapper.WeeklyBetTicketMapper;
import rugbyniela.repository.BetRepository;
import rugbyniela.repository.CoalitionRepository;
import rugbyniela.repository.DivisionRepository;
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
	private final DivisionRepository divisionRepository;
	
	private final IBetMapper betMapper;
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
		
		LocalDateTime now = LocalDateTime.now();
		
		UserSeasonScore userScore = checkUserSeason(dto.userSeasonId());
		
		MatchDay matchDay = checkMatchDay(dto.matchDayId());
		
		Optional<WeeklyBetTicket> optionalTicket = weeklyBetTicketRepository.findByUserSeasonAndMatchDay(userScore, matchDay);
		
		boolean isNewTicket = optionalTicket.isEmpty(); //true it's new false it already existed
		
		LocalDateTime earliestMatchStart = matchDay.getMatches().stream()
				.map(Match::getTimeMatchStart)
				.min(LocalDateTime::compareTo)
				.orElseThrow(() -> new RugbyException("No hay partidos en esta jornada", HttpStatus.BAD_REQUEST, ActionType.BETTING));
		
		Set<Match> matchesInMatchDay = matchDay.getMatches();
		boolean matchDayStarted =earliestMatchStart.isBefore(now);
		
		if(isNewTicket && matchDayStarted && dto.bets().size() != matchesInMatchDay.size()) {
				log.debug("How many matches did you bet on: " + dto.bets().size());	
				log.debug("How many matches should you have bet on: " + matchesInMatchDay.size());
				throw new RugbyException("La primera vez que apuesta a un partido debe apostar a todos los partidos", HttpStatus.BAD_REQUEST, ActionType.BETTING);
			
		}
//		Team predictedLeaderboardWinnerTeam =
//		        dto.predictedLeaderboardWinner() != null
//		                ? checkTeam(dto.predictedLeaderboardWinner())
//		                : null;
//		
		
		Set<Bet> bets = dto.bets().stream().map(betDTO->{
			Match match = checkMatch(betDTO.matchId());
			
			if(match.getTimeMatchStart().isBefore(now)) {
				log.debug("Ignoring bet for match {} because it already started", match.getId());
				
				return null;
				
			}
			
			Team predictedWinner =betDTO.predictedWinnerId() != null ? checkTeam(betDTO.predictedWinnerId()) : null;
			
			Bet bet = 	betMapper.toEntity(betDTO);
			bet.setPredictedWinner(predictedWinner);
			bet.setMatch(match);
			return bet;
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());
		
		
		
		WeeklyBetTicket ticket = optionalTicket.orElseGet(()->{ 
					log.debug("Created a new ticket");
					WeeklyBetTicket newTicket = new WeeklyBetTicket();
					newTicket.setUserSeason(userScore);
					newTicket.setCreationDate(now);
					return newTicket;
					});
		
		if(dto.divisionBets() != null && !dto.divisionBets().isEmpty()) {
			if(matchDayStarted) {
				throw new RugbyException("No se puede modificar el ganador del leaderboard cuando ya ha empezado la jornada", HttpStatus.BAD_REQUEST,ActionType.BETTING);
			}
			
			Set<DivisionBet> divisionBets = dto.divisionBets().stream().map(divisionBetDTO ->{
				Division division = checkDivision(divisionBetDTO.divisionId());
				Team predictedLeader = checkTeam(divisionBetDTO.predictedLeaderboardWinnerId());
				
				DivisionBet divisionBet = new DivisionBet();
				divisionBet.setDivision(division);
				divisionBet.setPredictedLeader(predictedLeader);
				return divisionBet;
			}).collect(Collectors.toSet());
			
			ticket.updateDivisionBets(divisionBets);
		}
		
//		if(predictedLeaderboardWinnerTeam!=null) {
//			if(earliestMatchStart.isBefore(now)) {
//				throw new RugbyException("No se puede modificar el ganador del leaderboard cuando ya ha empezado la jornada", HttpStatus.BAD_REQUEST,
//	                    ActionType.BETTING);
//			}else {
//				ticket.setPredictedLeaderBoardWinner(predictedLeaderboardWinnerTeam);
//				
//			}
//		}
		ticket.setCreationDate(now);
		ticket.setUserSeason(userScore);
		
		if(!bets.isEmpty()) {

			ticket.updateBets(bets);
			
		}
		
		weeklyBetTicketRepository.save(ticket);

		
		return weeklyBetTicketMapper.toDTO(ticket);
		
	
//		
		
		
		
	}
	
	

	// TODO: Si no se usa hay que borrarlo
	@Override
	public void cancelTicket() {
		
	}

	@Override
	@Transactional()
	public Page<WeeklyBetTicketResponseDTO> fetchUserSeasonTickets(Long userSeasonId, int page) {
		//Validate userSeasonScore exists
		UserSeasonScore userSeasonScore = checkUserSeason(userSeasonId);
		if (page < 0) {
	        throw new RugbyException("La pagina no puede ser negativa", HttpStatus.BAD_REQUEST, ActionType.BETTING);
	    }
		
		//Create pageable: 10 tickets, newest first
		Pageable pageable = PageRequest.of(page, 10, Sort.by("creationDate").descending());
		
		Page<WeeklyBetTicket> tickets = weeklyBetTicketRepository.findByUserSeason(userSeasonScore, pageable);
		 
		return tickets.map(weeklyBetTicketMapper::toDTO);
		 
	
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
				.orElseThrow(()-> new RugbyException("Coalicion no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return coalition;
		
	}
	
	public MatchDay checkMatchDay (Long id) {
		MatchDay matchDay = matchDayRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Jornada no encontrada", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return matchDay;
		
	}
	public Team checkTeam (Long id) {
		Team team = teamRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Equipo no encontrada", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return team;
		
	}
	
	public Match checkMatch (Long id) {
		Match match = matchRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Partido no encontrada", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return match;
		
	}
	
	public Division checkDivision (Long id) {
		Division division = divisionRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Partido no encontrada", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return division;
		
	}

}
