package rugbyniela.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.coalition.CoalitionJoinRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;
import rugbyniela.entity.pojo.Coalition;
import rugbyniela.entity.pojo.CoalitionRequest;
import rugbyniela.entity.pojo.CoalitionSeasonScore;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.ICoalitionMapper;
import rugbyniela.repository.CoalitionRepository;
import rugbyniela.repository.UserRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class ColaitionServiceImp implements ICoalitionService {

	private final UserRepository userRepository;
	private final CoalitionRepository coalitionRepository;
	private final ICoalitionMapper coalitionMapper;
	@Override
	public CoalitionResponseDTO createCoalition(CoalitionRequestDTO dto) {
		if(dto==null)
		{
			log.warn("Intento fallido de creacion de coalicion");
			throw new RugbyException("El dto para crear la coalicion es null", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
		}
		User user = userRepository.findById(dto.idCapitan()).orElseThrow(()->{
			throw new RugbyException("El usuario con id "+dto.idCapitan()+" no existe", HttpStatus.INTERNAL_SERVER_ERROR, ActionType.TEAM_MANAGEMENT);
		});
		Coalition coalition = coalitionMapper.toEntity(dto);
		coalition.setCapitan(user);
		//TODO: finish
		
		return null;
	}

	@Override
	public CoalitionResponseDTO fetchCoalitionById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CoalitionResponseDTO> fetchAllCoalitions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void requestJoinCoalition(CoalitionJoinRequestDTO joinRequestDTO) {
		// TODO Auto-generated method stub

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
