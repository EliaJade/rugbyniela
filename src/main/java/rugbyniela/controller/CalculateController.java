package rugbyniela.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.service.CalculatePointsServiceImpl;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/calculate")
public class CalculateController {
	
	private final CalculatePointsServiceImpl calculatePointsService;
	

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{matchDayId}/finish")
	public ResponseEntity<String>finishMatchDay(@PathVariable Long matchDayId){
		calculatePointsService.calculateMatchDayPoints(matchDayId);
		log.debug("entered finishMatch");
		return ResponseEntity.ok("MatchDay finished and points calculated");
		
		
	}
}
