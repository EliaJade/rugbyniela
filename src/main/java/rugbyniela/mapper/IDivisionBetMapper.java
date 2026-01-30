package rugbyniela.mapper;

import org.mapstruct.Mapping;

import rugbyniela.entity.dto.divisionBet.DivisionBetDTO;
import rugbyniela.entity.pojo.DivisionBet;

public interface IDivisionBetMapper {
	
	@Mapping(target="division.id", source = "divisionId")
	@Mapping(target="predictedLeader.id", source = "predictedLeaderboardWinnerId")
	DivisionBet toEntity(DivisionBetDTO dto);
	
	@Mapping(source="division.id", target = "divisionId")
	@Mapping(source="predictedLeader.id", target = "predictedLeaderboardWinnerId")
	DivisionBetDTO toDTO(DivisionBet divisionBet);
}
