package rugbyniela.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.divisionBet.DivisionBetDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketResponseDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketUpdateRequestDTO;
import rugbyniela.entity.pojo.WeeklyBetTicket;


@Mapper(
		componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE 
		)
public interface WeeklyBetTicketMapper {
//		@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//		void updateTicketFromDTO(WeeklyBetTicketUpdateRequestDTO dto, @MappingTarget WeeklyBetTicket ticket);
		
		@Mapping(target= "predictedLeaderboardWinner", source= "predictedLeaderBoardWinner.id")
		@Mapping(source = "userSeason.id", target = "userSeasonId")
	   WeeklyBetTicketResponseDTO toDTO (WeeklyBetTicket ticket);
		
		@Mapping(target= "predictedLeaderBoardWinner.id", source="predictedLeaderboardWinner")
		@Mapping(target = "userSeason.id", source = "userSeasonId")
		@Mapping(target = "creationDate", ignore = true)
		@Mapping(target = "coalitionAtBetTime", ignore = true)
		WeeklyBetTicket toEntity(WeeklyBetTicketRequestDTO dto);
		
//		@Mapping(target = "predictedLeaderBoardWinner.id", source = "predictedLeaderboardWinnerId")
//		WeeklyBetTicket toEntity(DivisionBetDTO dto);
		

}
