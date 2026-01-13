package rugbyniela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.service.ICoalitionService;

@RestController
@RequestMapping("/coalition")
public class CoalitionController {
	
	@Autowired
	private ICoalitionService coalitionService;

	@PostMapping("/create")
	public ResponseEntity<?> create(@Valid @RequestBody CoalitionRequestDTO requestDTO){
		return ResponseEntity.ok(coalitionService.createCoalition(requestDTO));
	}
	
}
