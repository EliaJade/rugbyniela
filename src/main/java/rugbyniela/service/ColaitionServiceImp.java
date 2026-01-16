package rugbyniela.service;

import java.security.DrbgParameters.Capability;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.coalition.CoalitionJoinRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionJoinResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionSimpleResponseDTO;
import rugbyniela.entity.pojo.Coalition;
import rugbyniela.entity.pojo.CoalitionMatchDayScore;
import rugbyniela.entity.pojo.CoalitionRequest;
import rugbyniela.entity.pojo.CoalitionSeasonScore;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.ICoalitionMapper;
import rugbyniela.repository.CoalitionRepository;
import rugbyniela.repository.CoalitionRequestRepository;
import rugbyniela.repository.CoalitionSeasonScoreRepository;
import rugbyniela.repository.SeasonRepository;
import rugbyniela.repository.UserRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class ColaitionServiceImp implements ICoalitionService {

	private final UserRepository userRepository;
	private final CoalitionRepository coalitionRepository;
	private final ICoalitionMapper coalitionMapper;
	private final CoalitionRequestRepository coalitionRequestRepository;
	private final SeasonRepository seasonRepository;
	private final CoalitionSeasonScoreRepository coalitionSeasonScoreRepository;
	//TODO: we use the @Transactional because we need it to the mapper cause' within that it make some joins
	
	@Override
	@Transactional
	public CoalitionResponseDTO createCoalition(CoalitionRequestDTO dto) {
		if(dto==null)
		{
			log.warn("Intento fallido de creacion de coalicion");
			throw new RugbyException("El dto para crear la coalicion es null", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->{ 
	    	throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
	    });
		if(userRepository.existsByIdAndCurrentCoalitionIsNotNull(user.getId())) {
			throw new RugbyException("El usuario con id "+user.getId()+" ya pertenece a una coalicion o es lider de alguna!", HttpStatus.INTERNAL_SERVER_ERROR, ActionType.TEAM_MANAGEMENT);
		}
		Coalition coalition = coalitionMapper.toEntity(dto);
		coalition.setCapitan(user);
		coalitionRepository.save(coalition);
		
		user.setCurrentCoalition(coalition);
		user.setCoalitionJoinedAt(LocalDateTime.now());
		userRepository.save(user);
		log.info("El usuario {} ha creado la coalicion {}",
				user.getName(),
				coalition.getName());
		return coalitionMapper.toDto(coalition);
	}

	@Override
	@Transactional 
	public CoalitionResponseDTO fetchCoalitionById(Long id) {
		Coalition coalition = coalitionRepository.findById(id).orElseThrow(()->{
			throw new RugbyException("La coalicion con id "+id+" no existe", HttpStatus.NOT_FOUND, ActionType.TEAM_MANAGEMENT);
		});
		return coalitionMapper.toDto(coalition);
	}

	@Override
	public Page<CoalitionSimpleResponseDTO> fetchAllCoalitions(int page, int size, Boolean active) {
		
		if(page < 0) {
			throw new RugbyException("La pagina no puede ser negativa", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
		}
		Page<Coalition> coalitionPage;
		if(active == null) {
			coalitionPage= coalitionRepository.findAll(PageRequest.of(page, size));
		}else {
			coalitionPage= coalitionRepository.findByActive(active,PageRequest.of(page, size));
		}
		if(page >= coalitionPage.getTotalPages() && coalitionPage.getTotalElements() > 0) {
			throw new RugbyException("No existen suficientes coaliciones para mostrar la página ", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
		}
		return coalitionPage.map(coalitionMapper::toSimpleDTO);
	}

	@Override
	@Transactional
	public void requestJoinCoalition(CoalitionJoinRequestDTO joinRequestDTO) {
		if(joinRequestDTO == null) {
			throw new RugbyException("El peticion para unirse es nula", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
		}
		Coalition coalition = coalitionRepository.findById(joinRequestDTO.coalitionId()).orElseThrow(()->{
			throw new RugbyException("La coalicion con id "+joinRequestDTO.coalitionId()+" no existe", HttpStatus.NOT_FOUND, ActionType.TEAM_MANAGEMENT);
		});
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		User user = userRepository.findByEmail(email).orElseThrow(()->{
			throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
		});
		if(user.getCurrentCoalition()!=null) {
			throw new RugbyException("El usuario ya pertenece a una coalicion, debe dejarla antes de pedir unirse a otra", HttpStatus.NO_CONTENT, ActionType.TEAM_MANAGEMENT);
		}
		coalitionRequestRepository.save(new CoalitionRequest(null, user, coalition, LocalDateTime.now()));
	}

	@Override
	@Transactional
	public void leaveCoalition() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		User user = userRepository.findByEmail(email).orElseThrow(()->{
			throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
		});
		
		Coalition coalition = user.getCurrentCoalition(); 
		
		if(coalition==null) {
			throw new RugbyException("El usuario no pertenece a ninguna coalicion", HttpStatus.NO_CONTENT, ActionType.TEAM_MANAGEMENT);
		}
		
		if(coalition.getCapitan().getId().equals(user.getId())) {
			long numMembers = userRepository.countByCurrentCoalition(coalition);
			if( numMembers > 1) {
				throw new RugbyException("El usuario es capitan, no puede abandonar la coalicion debe ceder la capitania", 
	                    HttpStatus.CONFLICT, ActionType.TEAM_MANAGEMENT);
			}else {
				coalition.setActive(false);
				coalition.setCapitan(null);
				String name = coalition.getName()+"_DEL_"+System.currentTimeMillis();
				coalition.setName(name);
				coalitionRepository.save(coalition);
				user.setCurrentCoalition(null);
				user.setCoalitionJoinedAt(null);
			}
		}else {
			user.setCurrentCoalition(null);
			user.setCoalitionJoinedAt(null);
		}
		userRepository.save(user);
	}

	@Override
	public Page<CoalitionJoinResponseDTO> getPendingRequests(int page, int size) {
		if(page < 0) {
			throw new RugbyException("La pagina no puede ser negativa", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		User user = userRepository.findByEmail(email).orElseThrow(()->{
			throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
		});
		
		Coalition coalition = user.getCurrentCoalition();
	    if (coalition == null) {
	        throw new RugbyException("No perteneces a ninguna coalición.", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
	    }
	    if (!coalition.getCapitan().getId().equals(user.getId())) {
	        throw new RugbyException("Solo el capitán puede ver las solicitudes de ingreso.", HttpStatus.FORBIDDEN, ActionType.TEAM_MANAGEMENT);
	    }

	    Page<CoalitionRequest> requestPage = coalitionRequestRepository.findByCoalitionId(coalition.getId(), PageRequest.of(page, size));

	    if (page >= requestPage.getTotalPages() && requestPage.getTotalElements() > 0) {
	    	throw new RugbyException("No existen suficientes solicitudes para mostrar la página", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
	    }
		return requestPage.map(coalitionMapper::toCoalitionJoinResponseDTO);
	}

	@Override
	@Transactional
	public void respondToRequest(Long requestId, Boolean accepted) {
		CoalitionRequest request  = coalitionRequestRepository.findById(requestId).orElseThrow(()->{
			throw new RugbyException("Peticion no encontrada", HttpStatus.NOT_FOUND,  ActionType.TEAM_MANAGEMENT);
		});
		//TODO: either admin aprove or not the request we delete it from the DB
		Coalition coalition = request.getCoalition();
		if(coalition == null) {
			throw new RugbyException("La coalicion a la cual se hizo la peticion es null", HttpStatus.INTERNAL_SERVER_ERROR,  ActionType.TEAM_MANAGEMENT);
		}
		if(accepted == true) {
			User user = request.getUser();
			if(user == null) {
				throw new RugbyException("El usuario de la peticion no existe", HttpStatus.INTERNAL_SERVER_ERROR,  ActionType.TEAM_MANAGEMENT);
			}
			if(user.getCurrentCoalition()!=null) {
				throw new RugbyException("El usuario ya pertenece a una coalicion, debe dejarla antes de pedir unirse a otra", HttpStatus.CONFLICT, ActionType.TEAM_MANAGEMENT);
			}
			user.setCurrentCoalition(coalition);
			user.setCoalitionJoinedAt(LocalDateTime.now());
			userRepository.save(user);
			log.info("El usuario {} ha aceptado una solicitud de ingreso a {} en la coalicion {}",
					SecurityContextHolder.getContext().getAuthentication().getName(),
					user.getName(),
					coalition.getName());
			//TODO: notify user it was accepted
		}else {
			//TODO: notify user it was not accepted
			log.info("El usuario {} ha rechazado una solicitud de ingreso en la coalicion {}",
					SecurityContextHolder.getContext().getAuthentication().getName(),
					coalition.getName());
		}
		coalitionRequestRepository.delete(request);
	}

	@Override
	@Transactional
	//TODO: admins no could do this, because if we want to allow them to do this, we must change the logic and add the id of the capitan
	//		as we're doing a MVP, we don't need the admin do that (for the moment)
	public void kickMember(Long userId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		User capitan = userRepository.findByEmail(email).orElseThrow(()->{
			throw new RugbyException("El usuario quien quiere hacer la accion no existe", HttpStatus.NO_CONTENT, ActionType.TEAM_MANAGEMENT);
		});
		User userToKick = userRepository.findById(userId).orElseThrow(()->{
			throw new RugbyException("El usuario a expulsar no existe", HttpStatus.NOT_FOUND, ActionType.TEAM_MANAGEMENT);
		});
		if(userToKick.getId().equals(capitan.getId())) {
			throw new RugbyException("El capitan no puede eliminarse a si mismo", HttpStatus.NOT_FOUND, ActionType.TEAM_MANAGEMENT);
		}
		Coalition coalition = capitan.getCurrentCoalition();
	    if (coalition == null) {
	        throw new RugbyException("No perteneces a ninguna coalición.", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
	    }
		if(!coalition.getCapitan().getId().equals(capitan.getId())) {
			throw new RugbyException("No tienes permisos sobre esta coalición.", HttpStatus.NO_CONTENT, ActionType.TEAM_MANAGEMENT);
		}
		if(!coalition.getCapitan().getId().equals(capitan.getId())) {
			throw new RugbyException("El usuario que quiere hacer la accion, no es capitan, por lo tanto no puede eliminar miembros", HttpStatus.CONFLICT, ActionType.TEAM_MANAGEMENT);	
		}
		if(!userToKick.getCurrentCoalition().getId().equals(coalition.getId())) {
			throw new RugbyException("El usuario a eliminar, no hace parte de la coalicion del capitan", HttpStatus.CONFLICT, ActionType.TEAM_MANAGEMENT);
		}
		userToKick.setCurrentCoalition(null);
		userToKick.setCoalitionJoinedAt(null);
		userRepository.save(userToKick);
		log.info("El usuario {} ha eliminado a {} de la coalicion {}",
				capitan.getName(),
				userToKick.getName(),
				coalition.getName());
	}

	@Override
	@Transactional
	public void transferCaptaincy(Long newCaptainId) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentCaptain = userRepository.findByEmail(auth.getName()).orElseThrow(() -> 
	        new RugbyException("Usuario no encontrado", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION)
	    );

	    Coalition coalition = currentCaptain.getCurrentCoalition();
	    if (coalition == null) {
	        throw new RugbyException("No tienes coalición para transferir.", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
	    }

	    if (!coalition.getCapitan().getId().equals(currentCaptain.getId())) {
	        throw new RugbyException("Solo el capitán actual puede transferir el liderazgo.", HttpStatus.FORBIDDEN, ActionType.TEAM_MANAGEMENT);
	    }

	    if (currentCaptain.getId().equals(newCaptainId)) {
	        throw new RugbyException("Ya eres el capitán.", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
	    }

	    User newCaptain = userRepository.findById(newCaptainId).orElseThrow(() -> 
	        new RugbyException("El nuevo capitán no existe.", HttpStatus.NOT_FOUND, ActionType.TEAM_MANAGEMENT)
	    );

	    if (newCaptain.getCurrentCoalition() == null || 
	        !newCaptain.getCurrentCoalition().getId().equals(coalition.getId())) {
	        throw new RugbyException("El usuario seleccionado no es miembro de tu coalición.", HttpStatus.CONFLICT, ActionType.TEAM_MANAGEMENT);
	    }
	    coalition.setCapitan(newCaptain);
	    
	    coalitionRepository.save(coalition);

	    log.info("Liderazgo de la coalicion {}, se a pasado de {} a {}", 
	            coalition.getName(),
	            currentCaptain.getNickname(),
	            newCaptain.getNickname());
	}

	@Override
	@Transactional
	public void deleteCoalition() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User currentCaptain = userRepository.findByEmail(auth.getName()).orElseThrow(() -> 
	        new RugbyException("Usuario no encontrado", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION)
	    );
	    Coalition coalition = currentCaptain.getCurrentCoalition();
	    if (coalition == null) {
	        throw new RugbyException("No tienes coalición para eliminar.", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
	    }
	    if (!coalition.getCapitan().getId().equals(currentCaptain.getId())) {
	        throw new RugbyException("Solo el capitán actual puede eliminar la coalicion.", HttpStatus.FORBIDDEN, ActionType.TEAM_MANAGEMENT);
	    }
	    if(userRepository.countByCurrentCoalition(coalition)>1) {
	    	throw new RugbyException("Hay mas miembros, no puedes eliminar la coalicion, o transfieres el liderazgo y te sales o eliminar a todos los miembros.",
	    			HttpStatus.FORBIDDEN, ActionType.TEAM_MANAGEMENT);
	    }
	    coalition.setActive(false);
		coalition.setCapitan(null);
		String name = coalition.getName()+"_DEL_"+System.currentTimeMillis();
		coalition.setName(name);
		coalitionRepository.save(coalition);
		currentCaptain.setCurrentCoalition(null);
		currentCaptain.setCoalitionJoinedAt(null);
		userRepository.save(currentCaptain);
		 log.info("El usuario {} ha eliminado la colicion {}",
				 currentCaptain.getName(),
				 coalition.getName().split("_DEL_")[0]);
	    
	}

	@Override
	@Transactional
	public void registerCoalitionInSeason(Long seasonId) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    User captain = userRepository.findByEmail(auth.getName()).orElseThrow(() -> 
	        new RugbyException("Usuario no encontrado", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION)
	    );

	    Coalition coalition = captain.getCurrentCoalition();
	    if (coalition == null) {
	        throw new RugbyException("No tienes una coalición para registrar.", HttpStatus.BAD_REQUEST, ActionType.TEAM_MANAGEMENT);
	    }

	    if (!coalition.getCapitan().getId().equals(captain.getId())) {
	        throw new RugbyException("Solo el capitán puede inscribir al equipo en una temporada.", HttpStatus.FORBIDDEN, ActionType.TEAM_MANAGEMENT);
	    }

	    Season season = seasonRepository.findById(seasonId).orElseThrow(() -> 
	        new RugbyException("La temporada no existe.", HttpStatus.NOT_FOUND, ActionType.TOURNAMENT) // Asumo que tienes un ActionType para torneos
	    );

	    boolean isAlreadyRegistered = coalitionSeasonScoreRepository.existsByCoalitionAndSeason(coalition, season);
	    
	    if (isAlreadyRegistered) {
	        throw new RugbyException("Tu coalición ya está registrada en esta temporada.", HttpStatus.CONFLICT, ActionType.TEAM_MANAGEMENT);
	    }

	    CoalitionSeasonScore inscription = new CoalitionSeasonScore(null, 0, season, coalition, new HashSet<CoalitionMatchDayScore>());
	    
	    coalitionSeasonScoreRepository.save(inscription);

	    log.info("Coalición '{}' inscrita en la temporada '{}'", coalition.getName(), season.getName());
	}

}
