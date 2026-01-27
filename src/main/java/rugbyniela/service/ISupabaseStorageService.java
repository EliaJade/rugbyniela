package rugbyniela.service;

import org.springframework.web.multipart.MultipartFile;

public interface ISupabaseStorageService {

	public String uploadProfilePicture(MultipartFile file, String uniqueName);
}
