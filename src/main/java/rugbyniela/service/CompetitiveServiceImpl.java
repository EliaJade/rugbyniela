package rugbyniela.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.parse.ANTLRParser.throwsSpec_return;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.division.DivisionAddToSeasonRequestDTO;
import rugbyniela.entity.dto.division.DivisionRequestDTO;
import rugbyniela.entity.dto.division.DivisionResponseDTO;
import rugbyniela.entity.dto.division.DivisionUpdateRequestDTO;
import rugbyniela.entity.dto.match.MatchAddToMatchDayRequestDTO;
import rugbyniela.entity.dto.match.MatchRequestDTO;
import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.match.MatchUpdateRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayAddToDivisionRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayResponseDTO;
import rugbyniela.entity.dto.season.SeasonRequestDTO;
import rugbyniela.entity.dto.season.SeasonResponseDTO;
import rugbyniela.entity.dto.season.SeasonUpdateRequestDTO;
import rugbyniela.entity.dto.team.TeamAddToDivisionRequestDTO;
import rugbyniela.entity.dto.team.TeamRequestDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;
import rugbyniela.entity.pojo.Address;
import rugbyniela.entity.pojo.CoalitionSeasonScore;
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.MatchDay;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.Team;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.Bonus;
import rugbyniela.enums.Category;
import rugbyniela.enums.MatchStatus;
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
@Slf4j
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
	
	
//------------all and by id-------------------------------------------------------------------------
	

	//TODO: check the authentication is correct
	@Override
	public Page<SeasonResponseDTO> fetchAllSeasons(int page, Boolean isActive) {
		//Validate there are seasons
		checkNegativePage(page);

		Pageable pageable = PageRequest.of(page, 10, Sort.by("startSeason").descending());
		
		Page<Season> seasons;
		if(isActive == null) {
			seasons = seasonRepository.findAll(pageable);
			
		} else {
			seasons = seasonRepository.findByIsActive(isActive, pageable);
		}
		if(seasons.isEmpty()) {
			throw new RugbyException("No hay temporadas", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			
		}
		return seasons.map(seasonMapper::toDTO);
		
		
	}

	@Override
	public SeasonResponseDTO fetchSeasonById(Long seasonId) {
		Season season = checkSeason(seasonId);
		return seasonMapper.toDTO(season);
		
		
	}
	@Override
	public Page<DivisionResponseDTO> fetchAllDivisions(int page, Boolean isActive) {
		checkNegativePage(page);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("name").descending());
		Page<Division> divisions;
		if(isActive == null) {
			divisions = divisionRepository.findAll(pageable);;
			
		} else {
			divisions = divisionRepository.findByIsActive(isActive, pageable);
		}
		if(divisions.isEmpty()) {
			throw new RugbyException("No hay divisiones", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			
		}
		return divisions.map(divisionMapper::toDTO);
	}

	@Override
	public DivisionResponseDTO fetchDivisionById(Long divisionId) {
		Division division = checkDivision(divisionId);
		
		return divisionMapper.toDTO(division);
	}

	@Override
	public Page<MatchDayResponseDTO> fetchAllMatchDays(int page, Boolean isActive) {
		checkNegativePage(page);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("dateBegin").descending());
		Page<MatchDay> matchDays;
		if(isActive == null) {
			matchDays = matchDayRepository.findAll(pageable);
			
		} else {
			matchDays = matchDayRepository.findByIsActive(isActive, pageable);
		}
		if(matchDays.isEmpty()) {
			throw new RugbyException("No hay jornadas", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		return matchDays.map(matchDayMapper::toDTO);
	}

	@Override
	@Transactional
	public MatchDayResponseDTO fetchMatchDayById(long matchDayId) {
		MatchDay matchDay = checkMatchDay(matchDayId);
		System.out.println(matchDay);
		MatchDayResponseDTO response = matchDayMapper.toDTO(matchDay);
		System.out.println(response);
		return response;
	}

	@Override
	public Page<MatchResponseDTO> fetchAllMatches(int page, Boolean isActive) {
		checkNegativePage(page);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("timeMatchStart").descending());
		Page<Match> matches; 
		if(isActive == null) {
			matches = matchRepository.findAll(pageable);
			
		} else {
			matches = matchRepository.findByIsActive(isActive, pageable);
		}
		if(matches.isEmpty()) {
			throw new RugbyException("No hay partidos", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN);
			
		}
		return matches.map(matchMapper::toDTO);
	}

	@Override
	public MatchResponseDTO fetchMatchById(Long matchId) {
		Match match = checkMatch(matchId);
		return matchMapper.toDTO(match);
	}
	
	@Override
	public Page<TeamResponseDTO> fetchAllTeams(int page, Boolean isActive) {
		
		checkNegativePage(page);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("name").descending());
		Page<Team> teams;
		if(isActive == null) {
			teams = teamRepository.findAll(pageable);
		} else {
			teams = teamRepository.findByIsActive(isActive, pageable);
		}
		if(teams.isEmpty()) {
			throw new RugbyException("No hay equipos", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN);
		}
		return teams.map(teamMapper::toDTO);
		
	}

	@Override
	public TeamResponseDTO fetchTeamById(Long teamId) {
		Team team = checkTeam(teamId);
		return teamMapper.toDTO(team);
		
	}
	
//------------other fetch-------------------------------------------------------------------------
	


	@Override
	public Page<DivisionResponseDTO> fetchDivisionsBySeason(Long seasonId, int page, Boolean isActive) {
		checkNegativePage(page);
		Season season = checkSeason(seasonId);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
		
		Page<Division> divisions ;
		if(isActive == null) {
			divisions =  divisionRepository.findBySeason(season, pageable);
		} else {
			divisions =  divisionRepository.findByIsActiveAndSeason(isActive, season, pageable);
		}
		
		return divisions.map(divisionMapper::toDTO); //“Take every Division in this page, convert it to a DivisionResponseDTO using MapStruct, and return a new Page of DTOs while keeping pagination info.”
		
	}

	@Override
	public DivisionResponseDTO fetchDivisionBySeasonAndId(Long seasonId, Long divisionId) {
		Season season = checkSeason(seasonId);
		
		
		Division division = divisionRepository.findByIdAndSeason(divisionId, season)
				.orElseThrow(()-> new RugbyException("Division no encontrada para esta temporada", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		return divisionMapper.toDTO(division);
	}

	@Override 
	public Page<TeamResponseDTO> fetchTeamsBySeason(Long seasonId, int page, Boolean isActive) {
		checkNegativePage(page);
		Season season = checkSeason(seasonId);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
		
		Page<Team> teams; 
		if(isActive == null) {
			teams = teamRepository.findTeamsBySeason(season, pageable);
		} else {
			teams = teamRepository.findTeamsBySeasonAndIsActive(season, isActive, pageable);
		}
		
		return teams.map(teamMapper::toDTO);
	}
	
	public Page<MatchResponseDTO> fetchMatchesBySeason(Long seasonId, int page, Boolean isActive) {
		checkNegativePage(page);
		Season season = checkSeason(seasonId);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("timeMatchStart").descending());
		
		Page<Match> matches;
		if(isActive == null) {
			matches = matchRepository.findByMatchDayDivisionSeason(season, pageable);
			} else {
			matches = matchRepository.findByIsActiveAndMatchDay_Division_Season(isActive, season, pageable);
			}
		return matches.map(matchMapper::toDTO);
	}
	 
	public Page<TeamResponseDTO> fetchTeamsBySeasonAndDivision (Long seasonId, Long divisionId, int page, Boolean isActive){
		Season season = checkSeason(seasonId);
		Division division = checkDivision(divisionId);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
		Page<Team> teams;
		if(isActive == null) {
			teams = teamRepository.findTeamsBySeasonAndDivision(season, division.getId(), pageable);
			} else {
			teams = teamRepository.findTeamsByIsActiveAndSeasonAndDivision(isActive, season, division.getId(), pageable);
			}
		//TODO:logs
		return teams.map(teamMapper::toDTO);
		
	}

	
//------------CREATE-------------------------------------------------------------------------
	

	@Transactional
	@Override
	public SeasonResponseDTO createSeason(SeasonRequestDTO dto) {
		if (dto.endSeason().isBefore(dto.startSeason())) {
			throw new RugbyException("La fecha de inicio de temporada no puede ser despues del final de temporada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(seasonRepository.existsByName(dto.name())) {
			throw new RugbyException("Ya existe una temporada con este nombre", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		Set<Division> divisions = divisionMapper.toEntitySet(dto.divisions());
		
		Season season = seasonMapper.toEntity(dto);
		if(season.getIsActive()==null) {
			season.setIsActive(true);
		}
		//so that division also saves season 
		for (Division division : divisions) {
			division.setSeason(season);
		}
		
		season.setDivisions(divisions);
		seasonRepository.save(season);
		log.info("Se ha creado la temporada {} correctamente", 
				season.getId());
		return seasonMapper.toDTO(season);
		
	}

	
	
	@Transactional
	@Override
	public DivisionResponseDTO createDivision(DivisionRequestDTO dto) {
		Category categoryEnum;
		try {
			categoryEnum = Category.valueOf(dto.category());
		}catch (IllegalArgumentException e ) {
			throw new RugbyException("La categoria no es valida", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		//TODO: validation in name must contain year of division to order it or add a varaible in division that has localdate it was made
		
		if(!dto.name().matches(".*\\d.*")) {
			throw new RugbyException("El nombre de la Division debe tener el año", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		DivisionRequestDTO dtoWithEnumCategory = new DivisionRequestDTO(
				dto.name(),
				categoryEnum.name(),
				dto.matchDays(),
				dto.teams());
		Division division = divisionMapper.toEntity(dtoWithEnumCategory);
		Set<Team> teams = dto.teams().stream()
				.map(id ->checkTeam(id))
				.collect(Collectors.toSet());
		if(division.getIsActive()==null) {
			division.setIsActive(true);
		}						
		division.setTeams(teams);		
		divisionRepository.save(division);
		log.info("Se ha creado la division {} correctamente", 
				division.getId());
		return divisionMapper.toDTO(division);
		
		
	}
	@Transactional
	@Override
	public MatchDayResponseDTO createMatchDay(MatchDayRequestDTO dto) {
		//TODO: validation that you cant create a matchday with the same name begin date and end date
		Division division = checkDivision(dto.divisionId());
		if(dto.dateEnd()!= null) {
			if(dto.dateBegin().isAfter(dto.dateEnd())) {
				throw new RugbyException("No puede acabar la jornada antes de que haya empezado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			}
		}
		//TODO: add id of division it belongs to
		MatchDay matchDay = matchDayMapper.toEntity(dto);
		if(matchDay.getIsActive()==null) {
			matchDay.setIsActive(true);
		}
		matchDay.setDivision(division);
		matchDayRepository.save(matchDay);
		log.info("Se ha creado la jornada {} correctamente", 
				matchDay.getId());
		return matchDayMapper.toDTO(matchDay);
		
	}
	@Transactional
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
		if(match.getIsActive()==null) {
			match.setIsActive(true);
		}
		if(match.getStatus()==null) {
			match.setStatus(MatchStatus.SCHEDULED);
		}
		match.setLocation(address);
		match.setLocalTeam(localTeam);
		match.setAwayTeam(awayTeam);
		matchRepository.save(match); 
		log.info("Se ha creado el partido {} correctamente", 
				match.getId());
		return matchMapper.toDTO(match);
		
	}
	@Transactional
	@Override
	public TeamResponseDTO createTeam(TeamRequestDTO dto) {
		if(teamRepository.existsByName(dto.name())) {
			throw new RugbyException("Este equipo ya existe", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		Team team = teamMapper.toEntity(dto);
		if(team.getIsActive()==null) {
			team.setIsActive(true);
		}
		teamRepository.save(team);
		log.info("Se ha creado el equipo {} correctamente", 
				team.getId());
		return teamMapper.toDTO(team);
		
		
	}
	@Transactional
	@Override
	public SeasonResponseDTO addDivisionToSeason(DivisionAddToSeasonRequestDTO dto) {
		Season season = checkSeason(dto.season());
		Division division = checkDivision(dto.division());
		if(Boolean.FALSE.equals(season.getIsActive())) {
			throw new RugbyException("No puedes añadir una division a una temporada eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(Boolean.FALSE.equals(division.getIsActive())) {
			throw new RugbyException("No puedes añadir una division eliminada a una temporada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		LocalDate seasonStart = season.getStartSeason();
		LocalDate seasonEnd = season.getEndSeason();
		LocalDate now = LocalDate.now();
		boolean existsCat = season.getDivisions().stream()
				.anyMatch(div -> div.getCategory().equals(division.getCategory()));
		boolean existsName = season.getDivisions().stream()
				.anyMatch(div -> div.getName().equals(division.getName()));
		if(existsName && existsCat) {
			throw new RugbyException("Ya existe una Division con este nombre y categoria", HttpStatus.BAD_REQUEST,  ActionType.SEASON_ADMIN);
		} //TODO: check this validation works
		boolean anyOutside = division.getMatchDays().stream()
				.map(MatchDay::getDateBegin)
				.anyMatch(date -> date.isBefore(seasonStart)|| date.isAfter(seasonEnd));
		if(anyOutside) {
			throw new RugbyException("Alguna jornada de la division esta fuera del rango de la temporada",HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(seasonStart.isBefore(now)) {
			throw new RugbyException("No puedes añadir divisiones cuando ya ha empezado la temporada",HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		if(season.getIsActive()==null) {
			season.setIsActive(false);
		}
		
		season.addDivision(division);
		division.setSeason(season);
		seasonRepository.save(season);
		divisionRepository.save(division);
		log.info("Se ha añadido la division {} a la temporada {} correctamente", 
				division.getId(), season.getId());
		return seasonMapper.toDTO(season);
		
	}
	@Transactional
	@Override
	public DivisionResponseDTO addMatchDayToDivision(MatchDayAddToDivisionRequestDTO dto) {
		MatchDay matchDay = checkMatchDay(dto.matchDay());
		Division division = checkDivision(dto.division());
		if(Boolean.FALSE.equals(division.getIsActive())) {
			throw new RugbyException("No puedes añadir una jornada a una division eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(Boolean.FALSE.equals(matchDay.getIsActive())) {
			throw new RugbyException("No puedes añadir una jornada eliminada a una division", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		Season season = checkSeason(division.getSeason().getId());
		LocalDate now = LocalDate.now();
		boolean teamOutside = matchDay.getMatches().stream()
				.flatMap(match -> Stream.of(match.getAwayTeam(), match.getLocalTeam())) //flatMap collects both localTeam and awayTeam for each match into one stream.
				.anyMatch(team -> !division.getTeams().contains(team)); //if at least one team playing in matchDay does not belong to the division it will give true, otherwise false
		if(teamOutside) {
			throw new RugbyException("Hay un equipo que juega en la jornada que no juega en esta division", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(season.getEndSeason().isBefore(now)) {
			throw new RugbyException("No se puede añadir una jornada a una division cuando ya ha terminado la temporada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			
		}
		division.addMatchDay(matchDay);
		matchDay.setDivision(division);
		matchDayRepository.save(matchDay);
		divisionRepository.save(division);
		log.info("Se ha añadido la jornada {} a la division {} correctamente", 
				matchDay.getId(), division.getId());
		return divisionMapper.toDTO(division);
	}
	
	@Transactional
	@Override
	public MatchDayResponseDTO addMatchToMatchDay(MatchAddToMatchDayRequestDTO dto) {
		MatchDay matchDay = checkMatchDay(dto.matchDay());
		Match match = checkMatch(dto.match());
		if(Boolean.FALSE.equals(matchDay.getIsActive())) {
			throw new RugbyException("No puedes añadir un partido a una jornada eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(Boolean.FALSE.equals(match.getIsActive())) {
			throw new RugbyException("No puedes añadir un partido eliminado a una jornada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		LocalDate now = LocalDate.now();
		if(match.getTimeMatchStart().isBefore(matchDay.getDateBegin().atStartOfDay())) {
			throw new RugbyException("No puede empezar el partido antes de la jornada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(matchDay.getDateBegin().isBefore(now)) {
			throw new RugbyException("No se puede añadir un partido a una jornada ya empezada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
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
		log.info("Se ha añadido el partido {} a la jornada {} correctamente", 
				match.getId(), matchDay.getId());
		return matchDayMapper.toDTO(matchDay);
	}
	@Transactional
	@Override
	public DivisionResponseDTO addTeamToDivision(TeamAddToDivisionRequestDTO dto) {
		Team team = checkTeam(dto.team());
		Division division = checkDivision(dto.division());
		if(Boolean.FALSE.equals(division.getIsActive())) {
			throw new RugbyException("No puedes añadir un equipo a una division eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(Boolean.FALSE.equals(team.getIsActive())) {
			throw new RugbyException("No puedes añadir un equipo eliminado a una division", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		Season season = checkSeason(division.getSeason().getId());
		LocalDate now = LocalDate.now();
		boolean exists = division.getTeams().stream()
				.anyMatch(existingTeam -> 
							existingTeam.getId().equals(team.getId()));
				
		if(exists) {
			throw new RugbyException("Este equipo ya juegan en la division", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(season.getEndSeason().isBefore(now)) {
			throw new RugbyException("No se puede añadir equipos a una temporada terminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		division.addTeam(team);
		divisionRepository.save(division);
		log.info("Se ha añadido el equipo {} a la division {} correctamente", 
				team.getId(), division.getId());
//		log.debug(division.toString());
		return divisionMapper.toDTO(division);
	}
	
//------------DELETE-------------------------------------------------------------------------	
	
	@Transactional
	@Override
	public void deleteSeason(Long id) {
		 
		Season season = checkSeason(id);
		
		if(Boolean.FALSE.equals(season.getIsActive())) {
			throw new RugbyException("No puedes borrar una temporada ya eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		boolean hasActiveDivisions = season.getDivisions().stream()
				.anyMatch(Division::getIsActive);
		boolean hasActiveUsers = season.getSeasonParticipants().stream()
				.anyMatch(UserSeasonScore::getIsActive);
		boolean hasActiveCoalSeasonScore = season.getCoalSeasonScores().stream()
				.anyMatch(CoalitionSeasonScore::getIsActive);
		if(hasActiveDivisions) {
			throw new RugbyException("No se puede eliminar una temporada con divisiones activas asociadas, recomiendo actualizar la temporada en vez de eliminarlo", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(hasActiveUsers) {
			throw new RugbyException("No se puede eliminar una temporada con coaliciones activas asociadas, recomiendo actualizar la temporada en vez de eliminarlo.", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(hasActiveCoalSeasonScore) {
			throw new RugbyException("No se puede eliminar una temporada con usuarios asociadas, recomiendo actualizar la temporada en vez de eliminarlo", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		season.setIsActive(false);
		String name = deletedName(season.getName());
		season.setName(name);
		seasonRepository.save(season);
		log.info("Se ha eliminado la temporada {}", 
					season.getId());
	}

	@Transactional
	@Override
	public void deleteDivision(Long id) {
		Division division = checkDivision(id);
		if(Boolean.FALSE.equals(division.getIsActive())) {
			throw new RugbyException("No puedes borrar una division ya eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		boolean hasActiveMatchDays = division.getMatchDays().stream()
				.anyMatch(MatchDay::getIsActive);
		if(hasActiveMatchDays) {
			throw new RugbyException("No se puede eliminar una división con jornadas activas asociadas", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		division.setIsActive(false);
		String name = deletedName(division.getName());
		division.setName(name);
		divisionRepository.save(division);
		log.info("Se ha eliminado la division {}", 
				division.getId());
	}
	
	@Transactional
	@Override
	public void deleteMatchDay(Long id) {
		LocalDate now = LocalDate.now();
		MatchDay matchDay = checkMatchDay(id);
		if(Boolean.FALSE.equals(matchDay.getIsActive())) {
			throw new RugbyException("No puedes borrar una jornada ya eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		boolean hasActiveMatches = matchDay.getMatches().stream()
				.anyMatch(Match::getIsActive);
		if(hasActiveMatches) {
			throw new RugbyException("No se puede eliminar una jornadas con partidos activos asociadas", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			
		}
		if(matchDay.getDateEnd().isBefore(now)) {
			throw new RugbyException("No se puede borrar una jornada que ya ha terminado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		matchDay.setIsActive(false);
		String name = deletedName(matchDay.getName());
		matchDay.setName(name);
		matchDayRepository.save(matchDay);
		log.info("Se ha eliminado la jornada {}", 
				matchDay.getId());
	}
	
	@Transactional
	@Override
	public void deleteMatch(Long id) {
		LocalDateTime now = LocalDateTime.now();
		Match match = checkMatch(id);
		if(Boolean.FALSE.equals(match.getIsActive())) {
			throw new RugbyException("No puedes borrar un partido ya eliminado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(match.getTimeMatchStart().isBefore(now)) {
			throw new RugbyException("No se puede borrar un partido que ya se ha jugado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);			
		}
		match.setIsActive(false);
		String name = deletedName(match.getName());
		match.setName(name);
		matchRepository.save(match);
		log.info("Se ha eliminado el partido {}", 
				match.getId());
	}
	@Transactional
	@Override
	public void deleteTeam(Long id) {
		Team team = checkTeam(id);
		if(Boolean.FALSE.equals(team.getIsActive())) {
			throw new RugbyException("No puedes borrar un partido ya eliminado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		boolean isUsedInMatch = matchRepository.existsByLocalTeamOrAwayTeam(team, team);
		if(isUsedInMatch) {
			throw new RugbyException("No se puede borrar un equipo que esta associado a un partido", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		team.setIsActive(false);
		String name = deletedName(team.getName());
		team.setName(name);
		teamRepository.save(team);
		log.info("Se ha eliminado el equipo {}", 
				team.getId());
	}
	
	
//------------UPDATE-------------------------------------------------------------------------	
	
	@Transactional
	@Override
	public SeasonResponseDTO updateSeason(Long id, SeasonUpdateRequestDTO dto) {
		Season season = checkSeason(id);
		
		if(Boolean.FALSE.equals(season.getIsActive())) {
			throw new RugbyException("No puedes actualizar una temporada eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		if(dto.name()!= null) {
			season.setName(dto.name());
		}
		
		if(dto.startSeason()!=null) {
			season.setStartSeason(dto.startSeason());
		}
		
		if(dto.endSeason()!=null) {
			season.setEndSeason(dto.endSeason());
		}
		
		seasonRepository.save(season);
		
		log.info("Se ha actualizado la temporada {} correctamente", 
				season.getId());
		return seasonMapper.toDTO(season);
	}
	
	@Transactional
	@Override
	public DivisionResponseDTO updateDivision(Long id, DivisionUpdateRequestDTO dto) {
		Division division = checkDivision(id);
		if(Boolean.FALSE.equals(division.getIsActive())) {
			throw new RugbyException("No puedes actualizar una division eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		if(dto.name()!= null) {
			division.setName(dto.name());
		}
		
		if(dto.category()!=null) {
			Category categoryEnum;
			try {
				categoryEnum = Category.valueOf(dto.category());
			}catch (IllegalArgumentException e ) {
				throw new RugbyException("La categoria no es valida", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			}
			division.setCategory(categoryEnum);
		}
		
		divisionRepository.save(division);
		log.info("Se ha actualizado la division {} correctamente", 
				division.getId());
		
		return divisionMapper.toDTO(division);
		
	}

	@Override
	@Transactional
	public MatchDayResponseDTO updateMatchDay(Long id, MatchDayRequestDTO dto) {
		MatchDay matchDay = checkMatchDay(id);
		if(Boolean.FALSE.equals(matchDay.getIsActive())) {
			throw new RugbyException("No puedes actualizar una jornada eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(dto.dateBegin()!=null) {
			matchDay.setDateBegin(dto.dateBegin());
		}
		if(dto.dateEnd()!=null) {
			matchDay.setDateEnd(dto.dateEnd());
		}
		if(dto.name()!=null) {
			matchDay.setName(dto.name());
		}
		matchDayRepository.save(matchDay);
		log.info("Se ha actualizado la jornada {} correctamente", 
				matchDay.getId());
		return matchDayMapper.toDTO(matchDay);
		
	}
	
	@Override
	@Transactional
	public MatchResponseDTO updateMatch(Long id, MatchUpdateRequestDTO dto) {
		Match match = checkMatch(id);
		if(Boolean.FALSE.equals(match.getIsActive())) {
			throw new RugbyException("No puedes actualizar un partido eliminado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		MatchDay matchDay = checkMatchDay(match.getMatchDay().getId());
		
		if(matchDay.isArePointsCalculated()) {
			throw new RugbyException("No puedes actualizar un partido de una jornada terminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		if(match.getStatus().equals(MatchStatus.SCHEDULED)||match.getStatus().equals(MatchStatus.CANCELLED)) {
			if(dto.localResult()!=null) {
				throw new RugbyException("No puedes modificar los resultados del equipo local de un partido con status: " + dto.status() , HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			}
			
			if(dto.awayResult()!=null) {
				throw new RugbyException("No puedes modificar los resultados del equipo visitante de un partido con status: " + dto.status() , HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
				
			}
			
			if(dto.bonus()!=null) {
				throw new RugbyException("No puedes modificar los resultados del bonus de un partido con status: " + dto.status() , HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
				
			}
			if(dto.location()!=null) {
				String street = StringUtils.normalize(dto.location().street());
			    String city = StringUtils.normalize(dto.location().city());
			    String postalCode = StringUtils.normalize(dto.location().postalCode());
			    String description = StringUtils.normalize(dto.location().description());

				
						
				Address address = addressRepository.findAddressByStreetAndCityAndPostalCodeAndDescription(
						street, city, postalCode, description)
					    .orElseGet(() -> addressRepository.save(addressMapper.toEntity(dto.location())));
				match.setLocation(address);
				
				
			}
			if(dto.name()!=null) {
				match.setName(dto.name());
			}
			
			if(dto.timeMatchStart()!=null) {
				match.setTimeMatchStart(dto.timeMatchStart());
			}
			
			if(dto.localTeam()!=null) {
			
				Team localTeam = checkTeam(dto.localTeam());
				match.setLocalTeam(localTeam);
			}
			
			if(dto.awayTeam()!=null) {
				Team awayTeam = checkTeam(dto.awayTeam());
				match.setAwayTeam(awayTeam);
			}
			
			if(dto.status()!=null) {
				match.setStatus(MatchStatus.valueOf(dto.status()));
			}
			
			
			
		} else if(match.getStatus().equals(MatchStatus.IN_PLAY)||match.getStatus().equals(MatchStatus.FINISHED)){
			if(dto.location()!=null) {
				throw new RugbyException("No puedes modificar la localizacion de un partido con status: " + dto.status() , HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
								
			}
			if(dto.name()!=null) {
				throw new RugbyException("No puedes modificar el nombre de un partido con status: " + dto.status() , HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
				
			}
			
			if(dto.timeMatchStart()!=null) {
				throw new RugbyException("No puedes modificar la hora que empieza el partido de un partido con status: " + dto.status() , HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
				
			}
			
			if(dto.localTeam()!=null) {
				throw new RugbyException("No puedes modificar el equipo local de un partido con status: " + dto.status() , HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
				
			}
			
			if(dto.awayTeam()!=null) {
				throw new RugbyException("No puedes modificar el equipo visitante de un partido con status: " + dto.status() , HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
				
			}
			if(dto.localResult()!=null) {
				match.setLocalResult(dto.localResult());
			}
			
			if(dto.awayResult()!=null) {
				match.setAwayResult(dto.awayResult());
			}
			
			if(dto.bonus()!=null) {
				match.setBonus(Bonus.valueOf(dto.bonus()));
			}
			
			if(dto.status()!=null) {
				match.setStatus(MatchStatus.valueOf(dto.status()));
			}
			
		}
		
		matchRepository.save(match);
		log.info("Se ha actualizado el partido {} correctamente", 
				match.getId());
		return matchMapper.toDTO(match);
		
	}
	public TeamResponseDTO updateTeam(Long id, TeamRequestDTO dto) {
		
		Team team = checkTeam(id);
		if(Boolean.FALSE.equals(team.getIsActive())) {
			throw new RugbyException("No puedes actualizar un equipo eliminado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		if(dto.name()!=null) {
			team.setName(dto.name());
			
		}
		if(dto.url()!=null) {
			team.setUrl(dto.url());
		}
		teamRepository.save(team);
		log.info("Se ha actualizado el equipo {} correctamente", 
				team.getId());
		return teamMapper.toDTO(team);
		
	}
	
	
	

//------------REMOVE-------------------------------------------------------------------------	
	
//	@Transactional
//	@Override
//	public SeasonResponseDTO removeDivisionFromSeason(Long divisionId, Long seasonId) {
//		Season season = checkSeason(seasonId);
//		Division division = checkDivision(divisionId);
//		
//		if(!season.getDivisions().contains(division)) {
//			throw new RugbyException("Esta temporada no contiene esta division", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN);
//		}
//		
//		if(!division.getMatchDays().isEmpty()) {
//			throw new RugbyException("No se puede borrar una division que tiene jornadas associadas", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
//			
//		}
//		
//		season.getDivisions().remove(division);
//		division.setSeason(null);
//		
//		seasonRepository.save(season);
//		
//		return seasonMapper.toDTO(season);
//	}
//
	@Transactional
	@Override
	public MatchDayResponseDTO removeMatchFromMatchDay(Long matchId, Long matchDayId) {
		Match match = checkMatch(matchId);
		MatchDay matchDay = checkMatchDay(matchDayId);
		if(Boolean.FALSE.equals(matchDay.getIsActive())) {
			throw new RugbyException("No puedes quitar un partido de una jornada eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		LocalDate now = LocalDate.now();

		LocalDateTime nowTime = LocalDateTime.now();
		if(!matchDay.getMatches().contains(match)) {
			throw new RugbyException("Esta jornada no contiene este partido", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN);
		}
		if(matchDay.getDateBegin().isBefore(now)) {
			throw new RugbyException("No se puede quitar una jornada empezada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			
		}
		
		if(match.getTimeMatchStart().isBefore(nowTime)) {
			throw new RugbyException("No se puede quitar un partido empezado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			//unnecessary since it would never happen since the match day needs to start before the match
		}
		
		if(matchDay.isArePointsCalculated()) {
			throw new RugbyException("No se puede modificar las jornadas ya finalizado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		matchDay.getMatches().remove(match);
		match.setMatchDay(null);
		matchDayRepository.save(matchDay);
		log.info("Se ha quitado el partido {} de la jornada {} correctamente", 
				match.getId(), matchDay.getId());
		return matchDayMapper.toDTO(matchDay);
	}
//	
//	@Transactional
//	@Override
//	public MatchResponseDTO removeTeamFromMatch(Long matchId, Long teamId) {
//		Match match = checkMatch(matchId);
//		Team team = checkTeam(teamId);
//		
//		boolean isLocal = match.getLocalTeam() != null && match.getLocalTeam().getId().equals(team.getId());
//		boolean isAway = match.getAwayTeam() != null && match.getAwayTeam().getId().equals(team.getId());
//		
//		
//		if(!isAway && !isLocal) {
//			throw new RugbyException("Esta partido no contiene este equipo", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN);
//		}
//		if(!(match.getStatus() == MatchStatus.SCHEDULED)) {
//			throw new RugbyException( "No se puede modificar los equipos de un partido en juego o finalizado o cancellados", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
//		}
//		if(isAway) {
//			match.setAwayTeam(null);
//		}
//		if(isLocal) {
//			match.setLocalTeam(null);
//		}
//		matchRepository.save(match);
//		return matchMapper.toDTO(match);
//	}
	@Override
	public DivisionResponseDTO removeMatchDayFromDivision(Long matchId, Long divisionId) {
		MatchDay matchDay = checkMatchDay(matchId);
		Division division = checkDivision(divisionId);
		if(Boolean.FALSE.equals(division.getIsActive())) {
			throw new RugbyException("No puedes quitar una jornada de una division eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		LocalDate now = LocalDate.now();
		
		if(!division.getMatchDays().contains(matchDay)) {
			throw new RugbyException("Esta division no contiene este jornada", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN);
		}
		
		if(matchDay.getDateBegin().isBefore(now)) {
			throw new RugbyException("No se puede quitar una jornada empezada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		division.getMatchDays().remove(matchDay);
		matchDay.setDivision(null);
		divisionRepository.save(division);
		log.info("Se ha quitado la jornada {} de la division {} correctamente", 
				matchDay.getId(), division.getId());
		return divisionMapper.toDTO(division);
	}
	
	@Override
	public DivisionResponseDTO removeTeamFromDivision(Long teamId, Long divisionId) {
		Team team = checkTeam(teamId);
		Division division = checkDivision(divisionId);
		if(Boolean.FALSE.equals(division.getIsActive())) {
			throw new RugbyException("No puedes quitar un equipo de una division eliminada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		if(!division.getTeams().contains(team)) {
			throw new RugbyException("Esta division no contiene este equipo", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN);
		}
		
		division.getTeams().remove(team);
		divisionRepository.save(division);
		log.info("Se ha quitado el equipo {} de la division {} correctamente", 
				team.getId(), division.getId());
		return divisionMapper.toDTO(division);
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
	public Team checkTeam(Long teamId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(()
				-> new RugbyException("Equipo no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		return team;
		
	}
	
	public Match checkMatch(Long matchId) {
		Match match = matchRepository.findById(matchId)
				.orElseThrow(()
				-> new RugbyException("Partido no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN)); 
		return match;
	}
	
	public void checkNegativePage(int page) {
		if(page<0) {
			throw new RugbyException("La pagina no puede ser negativa", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
	}
	
	private String deletedName(String name) {
	    return "DEL_" + name;
	}

	

	

	


	

	
	
	

	

	

	

	

	

}
