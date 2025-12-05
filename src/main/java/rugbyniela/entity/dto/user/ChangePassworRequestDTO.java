package rugbyniela.entity.dto.user;

public record ChangePassworRequestDTO(
		Long id,
		String currentPassword,
		String newPassword
		) {
}
