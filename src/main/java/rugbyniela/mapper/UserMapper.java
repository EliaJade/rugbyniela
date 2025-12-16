package rugbyniela.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.pojo.User;


@Mapper(
	    componentModel = "spring", // Para poder usar @Autowired UserMapper
	    unmappedTargetPolicy = ReportingPolicy.IGNORE // Ignora campos que no coincidan (útil para password)
	)
public interface UserMapper {
	
		User toEntity(UserRequestDTO dto);
		UserResponseDTO toDTO(User entity);
}
