package rugbyniela.entity.dto.user;

public record LoginResponseDTO(
		//@JsonProperty("access_token")
		String accessToken,
		//@JsonProperty("refresh_token")
		String refreshToken){

}
