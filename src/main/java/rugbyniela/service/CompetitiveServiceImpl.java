package rugbyniela.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
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
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.division.DivisionAddToSeasonRequestDTO;
import rugbyniela.entity.dto.division.DivisionRequestDTO;
import rugbyniela.entity.dto.division.DivisionResponseDTO;
import rugbyniela.entity.dto.division.DivisionUpdateRequestDTO;
import rugbyniela.entity.dto.match.MatchAddToMatchDayRequestDTO;
import rugbyniela.entity.dto.match.MatchRequestDTO;
import rugbyniela.entity.dto.match.MatchResponseDTO;
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
import rugbyniela.entity.pojo.MatchStatus;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.Team;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserSeasonScore;
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
	

	
	@Override
	public Page<SeasonResponseDTO> fetchAllSeasons(int page) {
		//Validate there are seasons
		checkNegativePage(page);

		Pageable pageable = PageRequest.of(page, 10, Sort.by("startSeason").descending());
		Page<Season> seasons = seasonRepository.findAll(pageable);
		
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
	public Page<DivisionResponseDTO> fetchAllDivisions(int page) {
		checkNegativePage(page);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("name").descending());
		Page<Division> divisions = divisionRepository.findAll(pageable);
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
	public Page<MatchDayResponseDTO> fetchAllMatchDays(int page) {
		checkNegativePage(page);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("dateBegin").descending());
		Page<MatchDay> matchDays = matchDayRepository.findAll(pageable);
		if(matchDays.isEmpty()) {
			throw new RugbyException("No hay jornadas", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		return matchDays.map(matchDayMapper::toDTO);
	}

	@Override
	public MatchDayResponseDTO fetchMatchDayById(long matchDayId) {
		MatchDay matchDay = checkMatchDay(matchDayId);
		
		return matchDayMapper.toDTO(matchDay);
	}

	@Override
	public Page<MatchResponseDTO> fetchAllMatches(int page) {
		checkNegativePage(page);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("timeMatchStart").descending());
		Page<Match> matches = matchRepository.findAll(pageable);
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
	public Page<TeamResponseDTO> fetchAllTeams(int page) {
		
		checkNegativePage(page);
		Pageable pageble = PageRequest.of(page, 10, Sort.by("name").descending());
		Page<Team> teams = teamRepository.findAll(pageble);
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
	public Page<DivisionResponseDTO> fetchDivisionsBySeason(Long seasonId, int page) {
		checkNegativePage(page);
		Season season = checkSeason(seasonId);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
		
		Page<Division> divisions =  divisionRepository.findBySeason(season, pageable);
		
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
	public Page<TeamResponseDTO> fetchTeamsBySeason(Long seasonId, int page) {
		checkNegativePage(page);
		Season season = checkSeason(seasonId);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
		
		Page<Team> teams = divisionRepository.findTeamsBySeason(season, pageable);
		return teams.map(teamMapper::toDTO);
	}
	
	public Page<MatchResponseDTO> fetchMatchesBySeason(Long seasonId, int page) {
		checkNegativePage(page);
		Season season = checkSeason(seasonId);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("timeMatchStart").descending());
		
		Page<Match> matches = matchRepository.findByMatchDayDivisionSeason(season, pageable);
		return matches.map(matchMapper::toDTO);
	}
	
	public Page<TeamResponseDTO> fetchTeamBySeasonAndDivision (Long seasonId, Long divisionId, int page){
		Season season = checkSeason(seasonId);
		Division division = checkDivision(divisionId);
		Pageable pageable = PageRequest.of(page, 10, Sort.by("name").ascending());
		Page<Team> teams = teamRepository.findTeamsBySeasonAndDivision(season, division.getId(), pageable);
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
		
		return seasonMapper.toDTO(season);
		
	}

	
	
	@Transactional
	@Override
	public DivisionResponseDTO createDivision(DivisionRequestDTO dto) {
		Category categoryEnum;
		try {
			categoryEnum = Category.valueOf(dto.category().toUpperCase());
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
		
		return divisionMapper.toDTO(division);
		
		
	}
	@Transactional
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
		if(match.getStatus()==null) {
			match.setStatus(MatchStatus.SCHEDULED);
		}
		match.setLocation(address);
		match.setLocalTeam(localTeam);
		match.setAwayTeam(awayTeam);
		matchRepository.save(match); ;
		return matchMapper.toDTO(match);
		
	}
	@Transactional
	@Override
	public TeamResponseDTO createTeam(TeamRequestDTO dto) {
		if(teamRepository.existsByName(dto.name())) {
			throw new RugbyException("Este equipo ya existe", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		Team team = teamMapper.toEntity(dto);
		teamRepository.save(team);
		return teamMapper.toDTO(team);
		
		
	}
	@Transactional
	@Override
	public SeasonResponseDTO addDivisionToSeason(DivisionAddToSeasonRequestDTO dto) {
		Season season = checkSeason(dto.season());
		Division division = checkDivision(dto.division());
		LocalDate seasonStart = season.getStartSeason();
		LocalDate seasonEnd = season.getEndSeason();
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
		if(season.getIsActive()==null) {
			season.setIsActive(false);
		}
		
		season.addDivision(division);
		division.setSeason(season);
		seasonRepository.save(season);
		divisionRepository.save(division);
		
		return seasonMapper.toDTO(season);
		
	}
	@Transactional
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
	
	@Transactional
	@Override
	public MatchDayResponseDTO addMatchToMatchDay(MatchAddToMatchDayRequestDTO dto) {
		MatchDay matchDay = checkMatchDay(dto.matchDay());
		Match match = checkMatch(dto.match());
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
	@Transactional
	@Override
	public DivisionResponseDTO addTeamToDivision(TeamAddToDivisionRequestDTO dto) {
		Team team = checkTeam(dto.team());
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
	
//------------DELETE-------------------------------------------------------------------------	
	
	@Transactional
	@Override
	public void deleteSeason(Long id) {
		//Only admin
		Season season = checkSeason(id);
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
		boolean hasActiveMatchDays = division.getMatchDays().stream()
				.anyMatch(MatchDay::getIsActive);
		if(hasActiveMatchDays) {
			throw new RugbyException("No se puede eliminar una división con jornadas activas asociadas", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		division.setIsActive(false);
		String name = deletedName(division.getName());
		division.setName(name);
		divisionRepository.save(division);
		
	}
	
	@Transactional
	@Override
	public void deleteMatchDay(Long id) {
		LocalDate now = LocalDate.now();
		MatchDay matchDay = checkMatchDay(id);
		boolean hasActiveMatches = matchDay.getMatches().stream()
				.anyMatch(Match::getIsActive);
		if(hasActiveMatches) {
			throw new RugbyException("No se puede eliminar una jornadas con partidos activos asociadas", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
			
		}
		if(matchDay.getDateEnd().isBefore(now)) {
			throw new RugbyException("No se puede borrar una jornada que ya ha terminado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
//			
		}
		
		matchDay.setIsActive(false);
		String name = deletedName(matchDay.getName());
		matchDay.setName(name);
		matchDayRepository.save(matchDay);
	}
	@Transactional
	@Override
	public void deleteMatch(Long id) {
		LocalDateTime now = LocalDateTime.now();
		Match match = checkMatch(id);
//		boolean isUsedInMatchDay = matchDayRepository.existsByMatchesContaining(match);
//		if(isUsedInMatchDay) {
//			throw new RugbyException("No se puede borrar un partido que esta associado a una jornada", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
//			
//		}
		if(match.getTimeMatchStart().isBefore(now)) {
			throw new RugbyException("No se puede borrar un partido que ya se ha jugado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
//			
		}
		match.setIsActive(false);
		String name = deletedName(match.getName());
		match.setName(name);
		matchRepository.save(match);
		
	}
	@Transactional
	@Override
	public void deleteTeam(Long id) {
		Team team = checkTeam(id);
		boolean isUsedInMatch = matchRepository.existsByLocalTeamOrAwayTeam(team, team);
		if(isUsedInMatch) {
			throw new RugbyException("No se puede borrar un equipo que esta associado a un partido", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		team.setIsActive(false);
		String name = deletedName(team.getName());
		team.setName(name);
		teamRepository.save(team);
	}
	
	
//------------REMOVE-------------------------------------------------------------------------	
	
	@Transactional
	@Override
	public SeasonResponseDTO updateSeason(Long id, SeasonUpdateRequestDTO dto) {
		Season season = checkSeason(id);
		
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
		
		
		return seasonMapper.toDTO(season);
	}
	
	@Transactional
	@Override
	public DivisionResponseDTO updateDivision(Long id, DivisionUpdateRequestDTO dto) {
		Division division = checkDivision(id);
		if(dto.name()!= null) {
			division.setName(dto.name());
		}
		
		if(dto.category()!=null) {
//			division.setCategory(dto.category());
		}
		
		return null;
		
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
//	@Transactional
//	@Override
//	public MatchDayResponseDTO removeMatchFromMatchDay(Long matchId, Long matchDayId) {
//		Match match = checkMatch(matchId);
//		MatchDay matchDay = checkMatchDay(matchDayId);
//		if(!matchDay.getMatches().contains(match)) {
//			throw new RugbyException("Esta jornada no contiene este partido", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN);
//		}
//		
//		if(matchDay.isArePointsCalculated()) {
//			throw new RugbyException("No se puede modificar las jornadas ya finalizado", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
//		}
//		
//		matchDay.getMatches().remove(match);
//		match.setMatchDay(null);
//		matchDayRepository.save(matchDay);
//		return matchDayMapper.toDTO(matchDay);
//	}
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
