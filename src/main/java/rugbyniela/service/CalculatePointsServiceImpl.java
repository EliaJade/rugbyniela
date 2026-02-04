package rugbyniela.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.pojo.Bet;
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.DivisionBet;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.MatchDay;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.Team;
import rugbyniela.entity.pojo.TeamDivisionScore;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.entity.pojo.WeeklyBetTicket;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.BetResult;
import rugbyniela.enums.Bonus;
import rugbyniela.exception.RugbyException;
import rugbyniela.repository.BetRepository;
import rugbyniela.repository.DivisionBetRepository;
import rugbyniela.repository.DivisionRepository;
import rugbyniela.repository.MatchDayRepository;
import rugbyniela.repository.TeamDivisionScoreRepository;
import rugbyniela.repository.TeamRepository;
import rugbyniela.repository.UserSeasonScoreRepository;
import rugbyniela.repository.WeeklyBetTicketRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatePointsServiceImpl implements ICalculatePointsService{

	private final BetRepository betRepository;
	private final WeeklyBetTicketRepository weeklyBetTicketRepository;
	private final DivisionRepository divisionRepository;
	private final MatchDayRepository matchDayRepository;
	private final TeamRepository teamRepository;
	private final TeamDivisionScoreRepository teamDivisionScoreRepository;
	private final UserSeasonScoreRepository userSeasonScoreRepository;
	private final DivisionBetRepository divisionBetRepository;
	
	
//	private MatchDayMapper matchDayMapper;
	
	@Transactional
	@Override
	public void calculatePointsByBet(Long betId) {
		Bet bet = checkBet(betId);
		Match match = bet.getMatch();
//		MatchDay matchDay = match.getMatchDay();
		WeeklyBetTicket ticket = bet.getWeeklyBetTicket();
		int weeklyPoints = 0;
		if(match.getLocalResult() == match.getAwayResult()) {
			bet.setBetCorrect(bet.getBetResult() == BetResult.DRAW ? true : false);
		}
		else {
		Team winner = match.getLocalResult() > match.getAwayResult()
		        ? match.getLocalTeam()
		        : match.getAwayTeam();
		
		if(bet.getPredictedWinner().equals(winner)) {
			bet.setBetCorrect(true);;
			if(bet.getBonus()!=(Bonus.NONE)) {
				if(bet.getBonus() == (match.getBonus())) {
//					bet.setPointsAwarded(bet.getPointsAwarded()+1);
					weeklyPoints=1;
				} 
				 else if (bet.getBonus()!=(match.getBonus())){
//					bet.setPointsAwarded(bet.getPointsAwarded()-1);
					weeklyPoints=-1;
				}
			}
			
		}
		else {
			bet.setBetCorrect(false);
		}
		}
		ticket.setWeeklyPoints(ticket.getWeeklyPoints() + weeklyPoints);
		log.debug("PointsAwarded: "+ticket.getWeeklyPoints());
		log.debug("Was bet correct: " + bet.getBetCorrect());
		betRepository.save(bet);
		weeklyBetTicketRepository.save(ticket);
	}
	

	@Override
	@Transactional
	public int calculatePointsByWeeklyBetTicket(Long weeklyBetTicketId) {
		WeeklyBetTicket ticket = checkTicket(weeklyBetTicketId);
		
		for(DivisionBet divisionBet:ticket.getDivisionBets()) {
			Division division = divisionBet.getDivision();
			Team predictedLeader = divisionBet.getPredictedLeader();
			
			TeamDivisionScore leader = division.getTeamDivisionScores().stream()
				    .max(Comparator.comparing(TeamDivisionScore::getTotalPoints))
				    .orElseThrow(()-> new RugbyException("No hay un ganador en la division " + division.getName(), HttpStatus.NOT_FOUND, ActionType.CALCULATION));
			Team actualLeader = leader != null ? leader.getTeam() : null;
			boolean correct = actualLeader != null && actualLeader.equals(predictedLeader);
			divisionBet.setBetCorrect(correct);
			divisionBetRepository.save(divisionBet);
		}
		
//		
//		int betAmount = ticket.getBets().size();
		long correctBets = ticket.getBets().stream()
		        .filter(bet -> Boolean.TRUE.equals(bet.getBetCorrect()))
		        .count();
		long correctDivisionBets = ticket.getDivisionBets().stream()
		        .filter(db -> Boolean.TRUE.equals(db.getBetCorrect()))
		        .count();
		log.debug("How many correctBets have there been" + correctBets);

		log.debug("How many correctDivisionBets have there been" + correctDivisionBets);
//		
		int points = switch ((int) correctBets) {
	    case 1 -> {
	        log.debug("Awarded 1 point");
	        yield 1;
	    }
	    case 2 -> {
	        log.debug("Awarded 3 points");
	        yield 3;
	    }
	    case 3 -> {
	        log.debug("Awarded 5 points");
	        yield 5;
	    }
	    case 4 -> {
	        int p = (correctDivisionBets <= 2) ? 25
	              : (correctDivisionBets == 1) ? 10
	              : 7;
	        log.debug("Awarded {} points for division bets", p);
	        yield p;
	    }
	    default -> {
	        log.debug("Awarded 0 points");
	        yield 0;
	    }
	};
		log.debug("Weekly points:" +ticket.getWeeklyPoints());
	if (ticket.getWeeklyPoints() == null) {
	    ticket.setWeeklyPoints(0);
	}
		log.debug("Points: " +points);
		ticket.setWeeklyPoints(ticket.getWeeklyPoints() + points);
		log.debug("Weekly points:" +ticket.getWeeklyPoints());
		weeklyBetTicketRepository.save(ticket);
		
		return ticket.getWeeklyPoints();
	}

	@Transactional
	@Override
	public int calculateTotalPoints(Long weeklyBetTicketId) {
		WeeklyBetTicket ticket = checkTicket(weeklyBetTicketId);
		UserSeasonScore userScore = ticket.getUserSeason();
		if(userScore == null) {
			throw new RugbyException("El ticket no esta asociado a un usuario", HttpStatus.NOT_FOUND, ActionType.CALCULATION);
		}
		else {
			log.debug("Weekly Points: " +ticket.getWeeklyPoints());
		userScore.setTotalPoints(userScore.getTotalPoints()+ticket.getWeeklyPoints());
		log.debug("Total Points: " +userScore.getTotalPoints());
		userSeasonScoreRepository.save(userScore);
		return userScore.getTotalPoints();
		}
	}
	

	@Transactional
	@Override
	public void calculateMatchDayPoints(Long matchDayId) {
		 MatchDay matchDay = checkMatchDay(matchDayId);
	     Season season = matchDay.getDivision().getSeason();
		
		if(Boolean.TRUE.equals(matchDay.getArePointsCalculated())) {
			throw new RugbyException("Los puntos de esta jornada ya estan calculado", HttpStatus.BAD_REQUEST, ActionType.CALCULATION);
		}
		
		Division division = matchDay.getDivision();
		if(division==null) {
			throw new RugbyException("La jornada no esta asociada a una division", HttpStatus.NOT_FOUND, ActionType.CALCULATION);
		}
		
		Map<Long, Integer> pointsByTeam = new HashMap<>();
		
		for (Match match : matchDay.getMatches()) {
			if(match.getLocalResult() == null || match.getAwayResult()==null) {
				continue;
			}
			
			Long localTeamId = match.getLocalTeam().getId();
			Long awayTeamId = match.getAwayTeam().getId();
			
			int localPoints = 0;
			int awayPoints = 0;
			
			if(match.getLocalResult() > match.getAwayResult()) {
	            localPoints = 4;
	            awayPoints = 0;
			
	            if(match.getBonus() != null) {
					if(match.getBonus() == Bonus.ATTACK) {
						localPoints = localPoints+1;
					}
					else if(match.getBonus() == Bonus.DEFENSE) {
						awayPoints = awayPoints+1;
					}
				}
		 	} 
			else if (match.getLocalResult() < match.getAwayResult()) {
	            localPoints = 0;
	            awayPoints = 4;
	            if(match.getBonus() == Bonus.DEFENSE) {
					localPoints = localPoints+1;
				}
				else if(match.getBonus() == Bonus.ATTACK) {
					awayPoints = awayPoints+1;
				}
			} else {
		        // Draw scenario
		        localPoints = 2;
		        awayPoints = 2;
			}
			
			pointsByTeam.merge(localTeamId, localPoints, Integer::sum);

			pointsByTeam.merge(awayTeamId, awayPoints, Integer::sum);
		}
		
		
		Set<TeamDivisionScore> teamScores = new HashSet<>(division.getTeamDivisionScores());
		 // Load existing TeamDivisionScores for this division and season
		Map<Long, TeamDivisionScore> scoreByTeamId = teamScores.stream()
				.filter(teamScore -> teamScore.getSeason().getId().equals(season.getId()))
				.collect(Collectors.toMap(teamScore -> teamScore.getTeam().getId(), Function.identity()));
		
		for (Map.Entry<Long, Integer> entry : pointsByTeam.entrySet()) {
			Long teamId = entry.getKey();
			int pointsToAdd = entry.getValue();
			
			TeamDivisionScore teamScore = scoreByTeamId.get(teamId);
			if(teamScore == null) {
				teamScore = new TeamDivisionScore();
				teamScore.setDivision(division);
				teamScore.setSeason(season);
				
				Team team = checkTeam(teamId);
				teamScore.setTeam(team);
				teamScore.setTotalPoints(pointsToAdd);
				teamDivisionScoreRepository.save(teamScore);
				division.getTeamDivisionScores().add(teamScore);
				team.getTeamDivisionScore().add(teamScore);
				
			}
			else {
				teamScore.setTotalPoints(teamScore.getTotalPoints() + pointsToAdd);
				teamDivisionScoreRepository.save(teamScore);
			}
		}
		
		matchDay.setArePointsCalculated(true);
		divisionRepository.save(division);
		matchDayRepository.save(matchDay);
	}
	
	
	@Transactional
	@Override
	public void finishMatchDay(Long matchDayId) {
		calculateMatchDayPoints(matchDayId);
		
		MatchDay matchDay = checkMatchDay(matchDayId);
		List<Bet> bets = betRepository.findByMatchDayId(matchDayId);
		for (Bet bet : bets) {
			calculatePointsByBet(bet.getId());
		}
		
		Set<WeeklyBetTicket> tickets = bets.stream().map(Bet::getWeeklyBetTicket).collect(Collectors.toSet());
		
		for (WeeklyBetTicket ticket : tickets) {
			calculatePointsByWeeklyBetTicket(ticket.getId());
			calculateTotalPoints(ticket.getId());
		}
	}
//	public void calculateTeamDivisionScore(Long divisionId) {
//		Division division = checkDivision(divisionId);
//		Set<MatchDay> matchDays = division.getMatchDays();
//		Set<TeamDivisionScore> teamDivisionScores = division.getTeamDivisionScores();
////		matchDays.stream().anyMatch(match)
//	}
	
	public Bet checkBet (Long id) {
		Bet bet = betRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Apuesta no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return bet;
		
	}
	public Team checkTeam (Long id) {
		Team team = teamRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Equipo no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return team;
		
	}
	
	public WeeklyBetTicket checkTicket (Long id) {
		WeeklyBetTicket ticket = weeklyBetTicketRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Ticket de apuesta no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return ticket;
		
	}
	
	public Division checkDivision (Long id) {
		Division division = divisionRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Division no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return division;
		
	}
	
	public MatchDay checkMatchDay (Long id) {
		MatchDay matchDay = matchDayRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Division no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return matchDay;
		
	}
	



	



}
