package rugbyniela.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rugbyniela.entity.dto.division.DivisionRequestDTO;
import rugbyniela.entity.dto.division.DivisionResponseDTO;
import rugbyniela.entity.dto.match.MatchRequestDTO;
import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.season.SeasonRequestDTO;
import rugbyniela.entity.dto.season.SeasonResponseDTO;
import rugbyniela.entity.dto.team.TeamRequestDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.Team;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.Category;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.MatchMapper;
import rugbyniela.mapper.SeasonMapper;
import rugbyniela.mapper.TeamMapper;
import rugbyniela.repository.DivisionRepository;
import rugbyniela.repository.MatchRepository;
import rugbyniela.repository.SeasonRepository;
import rugbyniela.repository.TeamRepository;


@Service
@RequiredArgsConstructor
public class CompetitiveServiceImpl implements CompetitiveService{

	private final SeasonRepository seasonRepository;
	private final DivisionRepository divisionRepository;
	private final TeamRepository teamRepository;
	private final MatchRepository matchRepository;
	
	private final SeasonMapper seasonMapper;
	private final TeamMapper teamMapper;
	private final MatchMapper matchMapper;
	
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
			throw new RugbyException("La fecha de fin no puede ser antes que el de inicio", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(seasonRepository.existsByName(dto.name())) {
			throw new RugbyException("Ya existe una temporada con este nombre", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		//Set<Division> divisions = dto.divisions();
		
		Season season = seasonMapper.toEntity(dto);
		//season.setDivisions(divisions);
		seasonRepository.save(season);
		return seasonMapper.toDTO(season);
		
	}

	@Override
	public DivisionResponseDTO createDivision(DivisionRequestDTO dto) {
		
		return null;
		
		
	}

	@Override
	public MatchResponseDTO createMatch(MatchRequestDTO dto) {
		LocalDateTime now = LocalDateTime.now();
		if(dto.localTeam()==dto.awayTeam()) {
			throw new RugbyException("No puede jugar un equipo contra si mismo", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		if(dto.timeMatchStart().isAfter(now)) {
			throw new RugbyException("El partido no puede empezar en el pasado",  HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
		
		Team localTeam = teamRepository.findById(dto.localTeam()).orElseThrow(()-> new RugbyException("Equipo local no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		Team awayTeam = teamRepository.findById(dto.awayTeam()).orElseThrow(()-> new RugbyException("Equipo visitante no encontrado", HttpStatus.NOT_FOUND, ActionType.SEASON_ADMIN));
		
		Match match = matchMapper.toEntity(dto);
		match.setLocalTeam(localTeam);
		match.setAwayTeam(awayTeam);
		matchRepository.save(match);
		
		
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
	public void addDivisionToSeason() {
		// TODO Auto-generated method stub
		
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
	
	public void checkNegativePage(int page) {
		if(page<0) {
			throw new RugbyException("La pagina no puede ser negativa", HttpStatus.BAD_REQUEST, ActionType.SEASON_ADMIN);
		}
	}
	

}
