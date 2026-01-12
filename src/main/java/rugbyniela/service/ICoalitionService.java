package rugbyniela.service;

import java.util.List;

import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionJoinRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;

public interface ICoalitionService {

	//--- generic basic CRUD
	CoalitionResponseDTO createCoalition(CoalitionRequestDTO dto);
	CoalitionResponseDTO fetchCoalitionById(Long id);
	List<CoalitionResponseDTO> fetchAllCoalitions();
	//update coalition ??
	
	// --- Gestión de Miembros (Desde el punto de vista del Usuario) ---
    void requestJoinCoalition(Long coalitionId); // Crea el CoalitionRequest
    void leaveCoalition(); // El usuario se sale voluntariamente (currentCoalition = null)
    
    // --- Gestión Administrativa (Desde el punto de vista del Capitán) ---
    List<CoalitionRequestDTO> getPendingRequests(Long coalitionId);
    void respondToRequest(Long requestId, boolean accepted); // Acepta (borra request, asigna user) o Rechaza (borra request)
    void kickMember(Long userId, Long coalitionId); // Expulsar usuario
    void transferCaptaincy(Long currentCaptainId, Long newCaptainId); // Ceder liderazgo
    void deleteCoalition(Long coalitionId); // Disolver coalición
    
    // --- Participación en Temporada ---
    // Inscribe a la coalición en la temporada actual (crea CoalitionSeasonScore)
    void registerCoalitionInSeason(Long coalitionId, Long seasonId); 
    
    // --- Historial ---
    // Obtener historial de temporadas de una coalición
    //List<CoalitionSeasonScoreDTO> getCoalitionHistory(Long coalitionId);
}
