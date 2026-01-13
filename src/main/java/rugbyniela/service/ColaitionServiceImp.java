package rugbyniela.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.coalition.CoalitionJoinRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionSimpleResponseDTO;
import rugbyniela.entity.pojo.Coalition;
import rugbyniela.entity.pojo.CoalitionRequest;
import rugbyniela.entity.pojo.CoalitionSeasonScore;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.ICoalitionMapper;
import rugbyniela.repository.CoalitionRepository;
import rugbyniela.repository.CoalitionRequestRepository;
import rugbyniela.repository.UserRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class ColaitionServiceImp implements ICoalitionService {

	private final UserRepository userRepository;
	private final CoalitionRepository coalitionRepository;
	private final ICoalitionMapper coalitionMapper;
	private final CoalitionRequestRepository coalitionRequestRepository;
	//TODO: we use the @Transactional because we need it to the mapper cause' within that it make some joins
	
	@Override
	@Transactional
	public CoalitionResponseDTO createCoalition(CoalitionRequestDTO dto) {
		if(dto==null)
		{
			log.warn("Intento fallido de creacion de coalicion");
			throw new RugbyException("El dto para crear la coalicion es null", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
		}
		User user = userRepository.findById(dto.idCapitan()).orElseThrow(()->{
			throw new RugbyException("El usuario con id "+dto.idCapitan()+" no existe", HttpStatus.NO_CONTENT, ActionType.TEAM_MANAGEMENT);
		});
		if(userRepository.existsByIdAndCurrentCoalitionIsNotNull(user.getId())) {
			throw new RugbyException("El usuario con id "+dto.idCapitan()+" ya pertenece a una coalicion o es lider de alguna!", HttpStatus.INTERNAL_SERVER_ERROR, ActionType.TEAM_MANAGEMENT);
		}
		Coalition coalition = coalitionMapper.toEntity(dto);
		coalition.setCapitan(user);
		coalitionRepository.save(coalition);
		
		user.setCurrentCoalition(coalition);
		user.setCoalitionJoinedAt(LocalDateTime.now());
		userRepository.save(user);
		
		return coalitionMapper.toDto(coalition);
	}

	@Override
	@Transactional 
	public CoalitionResponseDTO fetchCoalitionById(Long id) {
		Coalition coalition = coalitionRepository.findById(id).orElseThrow(()->{
			throw new RugbyException("La coalicion con id "+id+" no existe", HttpStatus.NO_CONTENT, ActionType.TEAM_MANAGEMENT);
		});
		return coalitionMapper.toDto(coalition);
	}

	@Override
	public Page<CoalitionSimpleResponseDTO> fetchAllCoalitions(int page, int size) {
		
		if(page < 0) {
			throw new RugbyException("La pagina no puede ser negativa", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
		}
		Page<Coalition> coalitionPage = coalitionRepository.findAll(PageRequest.of(page, size));
		if(page >= coalitionPage.getTotalPages() && coalitionPage.getTotalElements() > 0) {
			throw new RugbyException("No existen suficientes coaliciones para mostrar la página ", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
		}
		return coalitionPage.map(coalitionMapper::toSimpleDTO);
	}

	@Override
	@Transactional
	public void requestJoinCoalition(CoalitionJoinRequestDTO joinRequestDTO) {
		if(joinRequestDTO == null) {
			throw new RugbyException("El dto para unirse es null", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
		}
		Coalition coalition = coalitionRepository.findById(joinRequestDTO.coalitionId()).orElseThrow(()->{
			throw new RugbyException("La coalicion con id "+joinRequestDTO.coalitionId()+" no existe", HttpStatus.NO_CONTENT, ActionType.TEAM_MANAGEMENT);
		});
		User user = userRepository.findById(joinRequestDTO.userId()).orElseThrow(()->{
			throw new RugbyException("El usuario con id "+joinRequestDTO.userId()+" no existe", HttpStatus.NO_CONTENT, ActionType.TEAM_MANAGEMENT);
		});
		coalitionRequestRepository.save(new CoalitionRequest(null, user, coalition, LocalDateTime.now()));
	}

	@Override
	public void leaveCoalition() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<CoalitionRequestDTO> getPendingRequests(Long coalitionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void respondToRequest(Long requestId, boolean accepted) {
		// TODO Auto-generated method stub

	}

	@Override
	public void kickMember(Long userId, Long coalitionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transferCaptaincy(Long currentCaptainId, Long newCaptainId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCoalition(Long coalitionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerCoalitionInSeason(Long coalitionId, Long seasonId) {
		// TODO Auto-generated method stub

	}

}
