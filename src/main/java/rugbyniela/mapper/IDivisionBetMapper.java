package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rugbyniela.entity.dto.divisionBet.DivisionBetDTO;
import rugbyniela.entity.pojo.DivisionBet;

@Mapper(componentModel = "spring")
public interface IDivisionBetMapper {
	
//	@Mapping(target = "weeklyBetTicket.id", ignore = true)
//	@Mapping(target="division.id", source = "divisionId")
//	@Mapping(target="predictedLeader.id", source = "predictedLeaderId")
	DivisionBet toEntity(DivisionBetDTO dto);
	
//	@Mapping(source="division.id", target = "divisionId")
//	@Mapping(source="predictedLeader.id", target = "predictedLeaderId")
	DivisionBetDTO toDTO(DivisionBet divisionBet);
}
