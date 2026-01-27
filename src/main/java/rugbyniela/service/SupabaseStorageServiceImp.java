package rugbyniela.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupabaseStorageServiceImp implements ISupabaseStorageService {

	@Value("${supabase.base.url}")
    private String supabaseUrl;

    @Value("${supabase.base.key}")
    private String supabaseKey;

    @Value("${supabase.bucket.name}")
    private String bucketName;
	
    private final RestTemplate restTemplate = new RestTemplate(); 

    @Override
    public String uploadProfilePicture(MultipartFile file, String uniqueName) {
        if (file.isEmpty()) return null;

        try {
            // 1. Definir la URL de subida de Supabase
            // Endpoint: /storage/v1/object/{bucket}/{path}
            String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + uniqueName;

            // 2. Configurar Headers (Importante el Bearer Token y el Content-Type)
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.setContentType(MediaType.parseMediaType(file.getContentType()));

            // 3. Crear la entidad Http
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

            // 4. Enviar petición POST
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            // 5. Construir y retornar la URL pública
            // URL Pública: {supabaseUrl}/storage/v1/object/public/{bucket}/{path}
            return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + uniqueName;

        } catch (Exception e) {
            log.error("Error subiendo imagen a Supabase", e);
            throw new RugbyException("Error al subir la imagen de perfil", HttpStatus.INTERNAL_SERVER_ERROR, ActionType.REGISTRATION);
        }
    }

}
