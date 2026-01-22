package rugbyniela.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rugbyniela.entity.dto.division.DivisionAddToSeasonRequestDTO;
import rugbyniela.entity.dto.division.DivisionRequestDTO;
import rugbyniela.entity.dto.division.DivisionResponseDTO;
import rugbyniela.entity.dto.match.MatchAddToMatchDayRequestDTO;
import rugbyniela.entity.dto.match.MatchRequestDTO;
import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.matchDay.MatchDayAddToDivisionRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayResponseDTO;
import rugbyniela.entity.dto.season.SeasonRequestDTO;
import rugbyniela.entity.dto.season.SeasonResponseDTO;
import rugbyniela.entity.dto.team.TeamAddToDivisionRequestDTO;
import rugbyniela.entity.dto.team.TeamRequestDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;
import rugbyniela.entity.pojo.Address;
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.MatchDay;
import rugbyniela.entity.pojo.MatchStatus;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.Team;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.Category;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.IAddressMapper;
import rugbyniela.mapper.DivisionMapper;
import rugbyniela.mapper.MatchDayMapper;
import rugbyniela.mapper.MatchMapper;
import rugbyniela.mapper.SeasonMapper;
import rugbyniela.mapper.TeamMapper;
import rugbyniela.repository.AddressRepository;
import rugbyniela.repository.DivisionRepository;
import rugbyniela.repository.MatchDayRepository;
import rugbyniela.repository.MatchRepository;
import rugbyniela.repository.SeasonRepository;
import rugbyniela.repository.TeamRepository;
import rugbyniela.utils.StringUtils;


@Service
@RequiredArgsConstructor
public class CompetitiveServiceImpl implements ICompetitiveService{

	private final SeasonRepository seasonRepository;
	private final DivisionRepository divisionRepository;
	private final TeamRepository teamRepository;
	private final MatchRepository matchRepository;
	private final AddressRepository addressRepository;
	private final MatchDayRepository matchDayRepository;
	
	private final SeasonMapper seasonMapper;
	private final TeamMapper teamMapper;
	private final MatchMapper matchMapper;
	private final IAddressMapper addressMapper;
	private final MatchDayMapper matchDayMapper;
	private final DivisionMapper divisionMapper;
	
	@Override
	public Page<Season> fetchAllSeasons(int page) {
		//Validate there are seasons
		checkNegativePage(page);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("creationDate").descending());
		Page<Season> seasons = seasonRepository.findAll(pageable);
		
		if(seasons.isEmpty()) {
			throw new RugbyException("No hay temporadas", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			
		}
		return seasons;
		
		
	}

	@Override
	public Season fetchSeasonById(Long seasonId) {
		return checkSeason(seasonId);
		
		
	}

	@Override
	public Page<Division> fetchDivisionsBySeason(Long seasonId, int page) {
		checkNegativePage(page);
		Season season = checkSeason(seasonId);
		Pageable pageable = PageRequest.of(page, 10);
		return divisionRepository.findBySeason(season, pageable);
		
	}

	@Override
	public Division fetchDivisionBySeasonAndId(Long seasonId, Long divisionId) {
		Season season = checkSeason(seasonId);
		Division division = divisionRepository.findById(divisionId)
				.orElseThrow(()-> new RugbyException("Division no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		
		
		
		return divisionRepository.findByIdAndSeason(divisionId, season)
				.orElseThrow(()-> new RugbyException("Division no encontrada para esta temporada", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		
	}

	@Override
	public Page<Team> fetchAllTeams(int page) {
		
		checkNegativePage(page);
		Pageable pageble = PageRequest.of(page, 10, Sort.by("name").ascending());
		Page<Team> teams = teamRepository.findAll(pageble);
		if(teams.isEmpty()) {
			throw new RugbyException("No hay equipos", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN);
		}
		return teams;
		
	}

	@Override
	public Team fetchTeamById(Long teamId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(()-> new RugbyException("Equipo no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		
		return team;
		
	}

	@Transactional
	@Override
	public SeasonResponseDTO createSeason(SeasonRequestDTO dto) {
		if (dto.endSeason().isBefore(dto.startSeason())) {
			throw new RugbyException("La fecha de inicio de temporada no puede ser despues del final de temporada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(seasonRepository.existsByName(dto.name())) {
			throw new RugbyException("Ya existe una temporada con este nombre", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		
		Season season = seasonMapper.toEntity(dto);
		seasonRepository.save(season);
		return seasonMapper.toDTO(season);
		
	}

	
	
	
	@Override
	public DivisionResponseDTO createDivision(DivisionRequestDTO dto) {
		Category cateegoryEnum;
		try {
			cateegoryEnum = Category.valueOf(dto.category().toUpperCase());
		}catch (IllegalArgumentException e ) {
			throw new RugbyException("La categoria no es valida", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		DivisionRequestDTO dtoWithEnumCategory = new DivisionRequestDTO(
				dto.name(),
				cateegoryEnum.name(),
				dto.matchDays(),
				dto.teams());
		Division division = divisionMapper.toEntity(dtoWithEnumCategory);
		Set<Team> teams = dto.teams().stream()
				.map(id ->teamRepository.findById(id)
						.orElseThrow(()-> new RugbyException("Equipo no encontrado: "+ id, HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN)))
				.collect(Collectors.toSet());
								
		division.setTeams(teams);		
		divisionRepository.save(division);
		
		return divisionMapper.toDTO(division);
		
		
	}
	
	@Override
	public MatchDayResponseDTO createMatchDay(MatchDayRequestDTO dto) {
		if(dto.dateEnd()!= null) {
			if(dto.dateBegin().isAfter(dto.dateEnd())) {
				throw new RugbyException("No puede acabar la jornada antes de que haya empezado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			}
		}
		
		MatchDay matchDay = matchDayMapper.toEntity(dto);
		matchDayRepository.save(matchDay);
		
		return matchDayMapper.toDTO(matchDay);
		
	}

	@Override
	public MatchResponseDTO createMatch(MatchRequestDTO dto) {
		LocalDateTime now = LocalDateTime.now();
		if(dto.localTeam()==dto.awayTeam()) {
			throw new RugbyException("No puede jugar un equipo contra si mismo", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(dto.timeMatchStart().isBefore(now)) {
			throw new RugbyException("El partido no puede empezar en el pasado",  HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		Team localTeam = teamRepository.findById(dto.localTeam()).orElseThrow(()-> new RugbyException("Equipo local no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		Team awayTeam = teamRepository.findById(dto.awayTeam()).orElseThrow(()-> new RugbyException("Equipo visitante no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		
		String street = StringUtils.normalize(dto.location().street());
	    String city = StringUtils.normalize(dto.location().city());
	    String postalCode = StringUtils.normalize(dto.location().postalCode());
	    String description = StringUtils.normalize(dto.location().description());

		Address address = addressRepository.findAddressByStreetAndCityAndPostalCodeAndDescription(street, city, postalCode, description)
				.orElseGet(()->{
					Address location = addressMapper.toEntity(dto.location());
					return addressRepository.save(location);	
				});
		
		Match match = matchMapper.toEntity(dto);
		if(match.getStatus()==null) {
			match.setStatus(MatchStatus.SCHEDULED);
		}
		match.setLocation(address);
		match.setLocalTeam(localTeam);
		match.setAwayTeam(awayTeam);
		matchRepository.save(match); ;
		return matchMapper.toDTO(match);
		
	}

	@Override
	public TeamResponseDTO createTeam(TeamRequestDTO dto) {
		if(teamRepository.existsByName(dto.name())) {
			throw new RugbyException("Este equipo ya existe", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		Team team = teamMapper.toEntity(dto);
		teamRepository.save(team);
		return teamMapper.toDTO(team);
		
		
	}
	@Override
	public SeasonResponseDTO addDivisionToSeason(DivisionAddToSeasonRequestDTO dto) {
		Season season = checkSeason(dto.season());
		Division division = checkDivision(dto.division());
		LocalDate seasonStart = season.getStartSeason();
		LocalDate seasonEnd = season.getEndSeason();
		
		boolean anyOutside = division.getMatchDays().stream()
				.map(MatchDay::getDateBegin)
				.anyMatch(date -> date.isBefore(seasonStart)|| date.isAfter(seasonEnd));
		if(anyOutside) {
			throw new RugbyException("Alguna jornada de la division esta fuera del rango de la temporada",HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(season.getIsActive()==null) {
			season.setIsActive(false);
		}
		
		season.addDivision(division);
		division.setSeason(season);
		seasonRepository.save(season);
		divisionRepository.save(division);
		
		return seasonMapper.toDTO(season);
		
	}
	
	@Override
	public DivisionResponseDTO addMatchDayToDivision(MatchDayAddToDivisionRequestDTO dto) {
		MatchDay matchDay = checkMatchDay(dto.matchDay());
		Division division = checkDivision(dto.division());
		boolean teamOutside = matchDay.getMatches().stream()
				.flatMap(match -> Stream.of(match.getAwayTeam(), match.getLocalTeam())) //flatMap collects both localTeam and awayTeam for each match into one stream.
				.anyMatch(team -> !division.getTeams().contains(team)); //if at least one team playing in matchDay does not belong to the division it will give true, otherwise false
		if(teamOutside) {
			throw new RugbyException("Hay un equipo que juega en la jornada que no juega en esta division", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		division.addMatchDay(matchDay);
		matchDay.setDivision(division);
		matchDayRepository.save(matchDay);
		divisionRepository.save(division);
		return divisionMapper.toDTO(division);
	}
	
	@Override
	public MatchDayResponseDTO addMatchToMatchDay(MatchAddToMatchDayRequestDTO dto) {
		MatchDay matchDay = checkMatchDay(dto.matchDay());
		Match match = matchRepository.findById(dto.match()).orElseThrow(()-> new RugbyException("Partido no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN)); 
		if(match.getTimeMatchStart().isBefore(matchDay.getDateBegin().atStartOfDay())) {
			throw new RugbyException("No puede empezar el partido antes de la jornada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		boolean exists = matchDay.getMatches().stream()
			    .anyMatch(existingMatch -> {
			        boolean sameTeams = 
			            (existingMatch.getLocalTeam().equals(match.getLocalTeam()) && existingMatch.getAwayTeam().equals(match.getAwayTeam())) ||
			            (existingMatch.getLocalTeam().equals(match.getAwayTeam()) && existingMatch.getAwayTeam().equals(match.getLocalTeam()));
			        return sameTeams;
			    });

			if (exists) {
			    throw new RugbyException("Estos equipos ya juegan entre sí en esta jornada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			}
		matchDay.addMatch(match);
		match.setMatchDay(matchDay);
		matchDayRepository.save(matchDay);
		matchRepository.save(match);
		return matchDayMapper.toDTO(matchDay);
	}

	@Override
	public DivisionResponseDTO addTeamToDivision(TeamAddToDivisionRequestDTO dto) {
		Team team = teamRepository.findById(dto.team()).orElseThrow(()->new RugbyException("Equipo no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		Division division = checkDivision(dto.division());
		boolean exists = division.getTeams().stream()
				.anyMatch(existingTeam -> 
							existingTeam.getId().equals(team.getId()));
				
		if(exists) {
			throw new RugbyException("Este equipo ya juegan en la division", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		division.addTeam(team);
		divisionRepository.save(division);
		
		return divisionMapper.toDTO(division);
	}
	

	@Override
	public void fetchMatchesOfSeason() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchMatchesOfSeasonByMatchDay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishMatchesSeasonByMatchDay() {
		// TODO Auto-generated method stub
		
	}
	
	
	public Season checkSeason(Long seasonId) {
		Season season = seasonRepository.findById(seasonId)
				.orElseThrow(()
				-> new RugbyException("Temporada no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		return season;
		
	}
	public MatchDay checkMatchDay(Long matchDayId) {
		MatchDay matchDay = matchDayRepository.findById(matchDayId)
				.orElseThrow(()
				-> new RugbyException("Jornada no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		return matchDay;
		
	}
	public Division checkDivision(Long divisionId) {
		Division division = divisionRepository.findById(divisionId)
				.orElseThrow(()
				-> new RugbyException("Division no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		return division;
		
	}
	
	public void checkNegativePage(int page) {
		if(page<0) {
			throw new RugbyException("La pagina no puede ser negativa", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
	}

	

	

	

	

}
