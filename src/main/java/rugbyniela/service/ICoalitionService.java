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
	
	//--- management of members (from leader's point of view)
	List<CoalitionJoinRequestDTO> fetchPendingRequests(Long coalitionId);
	void responseJoinRequest(Long requestId);
	
}
