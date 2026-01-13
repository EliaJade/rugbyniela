package rugbyniela.service;

import java.util.List;

import rugbyniela.entity.dto.coalition.CoalitionJoinRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;

public interface ICoalitionService {

	//--- generic basic CRUD
	CoalitionResponseDTO createCoalition(CoalitionRequestDTO dto);
	CoalitionResponseDTO fetchCoalitionById(Long id);
	List<CoalitionResponseDTO> fetchAllCoalitions();
	//update coalition ??
	
	// --- management of members (from user's point of view) ---
    void requestJoinCoalition(CoalitionJoinRequestDTO joinRequestDTO);
    void leaveCoalition();
    
    // --- administrative management(from leader's point of view) ---
    List<CoalitionRequestDTO> getPendingRequests(Long coalitionId);
    void respondToRequest(Long requestId, boolean accepted);
    void kickMember(Long userId, Long coalitionId);
    void transferCaptaincy(Long currentCaptainId, Long newCaptainId);
    void deleteCoalition(Long coalitionId);
    
    // --- Participate in a season---
    void registerCoalitionInSeason(Long coalitionId, Long seasonId); 
    
    // --- Historial ---
    // Obtener historial de temporadas de una coalición
    //List<CoalitionSeasonScoreDTO> getCoalitionHistory(Long coalitionId);
}
