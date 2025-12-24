package rugbyniela.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.repository.TokenRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupService {
	
	private final TokenRepository tokenRepository;
	
	/**
	 * This method will be execute automatically
	 * according to the CRON expression.
	 * CRON "second minute hour day month day_week"
	 * in this case this will be execute every day
	 * at 3AM 
	 */
	@Scheduled(cron = "0 0 3 * * *")
	public void removeUselessTokens() {
		tokenRepository.deleteAllExpiredOrRevokedTokens();
		log.info("Eliminando tokens expirados o revocados de los usuarios");
	}

}
