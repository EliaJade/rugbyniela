package rugbyniela.service;

import java.util.List;

import org.springframework.data.domain.Page;

import rugbyniela.entity.dto.coalition.CoalitionJoinRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionJoinResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionSimpleResponseDTO;

public interface ICoalitionService {

	//--- generic basic CRUD
	CoalitionResponseDTO createCoalition(CoalitionRequestDTO dto);
	CoalitionResponseDTO fetchCoalitionById(Long id);
	Page<CoalitionSimpleResponseDTO> fetchAllCoalitions(int page, int size, Boolean active);
	//update coalition ??
	
	// --- management of members (from user's point of view) ---
    void requestJoinCoalition(CoalitionJoinRequestDTO joinRequestDTO);
    void leaveCoalition();
    
    // --- administrative management(from leader's point of view) ---
    Page<CoalitionJoinResponseDTO> getPendingRequests(Long coalitionId,int page, int size);
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
