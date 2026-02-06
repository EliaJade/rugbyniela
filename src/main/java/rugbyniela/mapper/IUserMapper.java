package rugbyniela.mapper;

import java.util.Arrays;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.http.HttpStatus;

import rugbyniela.entity.dto.address.AddressResponseDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.entity.pojo.Address;
import rugbyniela.entity.pojo.User;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.Gender;
import rugbyniela.enums.Role;
import rugbyniela.exception.RugbyException;


@Mapper(
	    componentModel = "spring", // Para poder usar @Autowired UserMapper
	    unmappedTargetPolicy = ReportingPolicy.IGNORE // Ignora campos que no coincidan (útil para password)
	)
public interface IUserMapper {
	
		@Mapping(target = "gender",source="gender",qualifiedByName = "stringToGender")
		@Mapping(target = "password",ignore = true)//TODO: change this in order to allow mapstruct do this for us
//		@Mapping(target="email",source="email",qualifiedByName = "normalizeEmail")

		
		//@Mapping(target = "role",source="role",qualifiedByName = "stringToRole")
		@Mapping(target = "address", ignore = true)
		@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	    void updateUserFromDto(UserUpdatedRequestDTO dto, @MappingTarget User user); // im not sure what this does need to check

	    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
		User toEntity(UserRequestDTO dto);
		
		@Mapping(target = "coalitonName",source="currentCoalition.name")
		@Mapping(target = "rol", expression = "java(entity.getRole().name())")
		UserResponseDTO toDTO(User entity);
		
		AddressResponseDTO addressToDto(Address address);
		
		@Named("normalizeEmail")
		default String normalizeEmail(String email) {
			if(email==null) return null;
			return email.trim().toLowerCase();
		}
		@Named("stringToGender")
	    default Gender mapGender(String genderStr) {
	        if (genderStr == null || genderStr.isBlank()) {
	            // Si en el DTO pusiste @NotNull, esto teóricamente no pasaría, 
	            // pero es bueno protegerse o retornar null si es opcional.
	            return null; 
	        }
	        try {
	            return Gender.valueOf(genderStr.trim().toUpperCase());
	        } catch (IllegalArgumentException e) {
	            throw new RugbyException(
	                "El género '" + genderStr + "' no es válido. Valores permitidos: " + Arrays.toString(Gender.values()),
	                HttpStatus.BAD_REQUEST,
	                ActionType.REGISTRATION // O ActionType.VALIDATION
	            );
	        }
	    }
		
		@Named("stringToRole")
	    default Role mapRole(String roleStr) {
	        if (roleStr == null || roleStr.isBlank()) {
	            return null; // O lanzar excepción si es obligatorio
	        }
	        try {
	            return Role.valueOf(roleStr.trim().toUpperCase());
	        } catch (IllegalArgumentException e) {
	            throw new RugbyException(
	                "El rol '" + roleStr + "' no es válido. Valores permitidos: " + Arrays.toString(Role.values()),
	                HttpStatus.BAD_REQUEST,
	                ActionType.REGISTRATION
	            );
	        }
	    }
}
