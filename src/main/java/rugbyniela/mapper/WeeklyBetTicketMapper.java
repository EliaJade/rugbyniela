package rugbyniela.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketResponseDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketUpdateRequestDTO;
import rugbyniela.entity.pojo.WeeklyBetTicket;


@Mapper(
		componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE 
		)
public interface WeeklyBetTicketMapper {
		@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
		void updateTicketFromDTO(WeeklyBetTicketUpdateRequestDTO dto, @MappingTarget WeeklyBetTicket ticket);
		
		@Mapping(source = "userSeason.id", target = "userSeasonId")
//	    @Mapping(source = "predictedLeaderBoardWinner.id", target = "predictedLeaderboardWinnerId")
		WeeklyBetTicketResponseDTO toDTO (WeeklyBetTicket ticket);
		
//		WeeklyBetTicket toEntity(WeeklyBetTicketRequestDTO dto);
		
		

}
