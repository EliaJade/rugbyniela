package rugbyniela.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import rugbyniela.entity.dto.coalition.CoalitionJoinRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionJoinResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionSimpleResponseDTO;

public interface ICoalitionService {

	//method to get the coalition of the current user
	CoalitionResponseDTO getMyCoalition();
	//--- generic basic CRUD
	CoalitionResponseDTO createCoalition(CoalitionRequestDTO dto);
	CoalitionResponseDTO fetchCoalitionById(Long id);
	Page<CoalitionSimpleResponseDTO> fetchAllCoalitions(Pageable pageable, Boolean active, String name);
	//update coalition ??
	
	// --- management of members (from user's point of view) ---
    void requestJoinCoalition(CoalitionJoinRequestDTO joinRequestDTO);
    void leaveCoalition();
    
    // --- administrative management(from leader's point of view) ---
    Page<CoalitionJoinResponseDTO> getPendingRequests(Pageable pageable);
    void respondToRequest(Long requestId, Boolean accepted);
    void kickMember(Long userId);
    void transferCaptaincy(Long newCaptainId);
    void deleteCoalition();
    
    // --- Participate in a season---
    void registerCoalitionInSeason(Long seasonId); 
    
    // --- Historial ---
    // Obtener historial de temporadas de una coalición
    //List<CoalitionSeasonScoreDTO> getCoalitionHistory(Long coalitionId);
}
