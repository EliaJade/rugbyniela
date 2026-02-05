package rugbyniela.service;

import org.springframework.web.multipart.MultipartFile;

public interface ISupabaseStorageService {

	public String uploadFile(MultipartFile file, String uniqueName);
}
