package rugbyniela.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rugbyniela.entity.dto.collaborator.CollaboratorRequestDTO;
import rugbyniela.entity.dto.collaborator.CollaboratorResponseDTO;
import rugbyniela.entity.dto.season.SeasonResponseDTO;
import rugbyniela.entity.dto.season.SeasonUpdateRequestDTO;
import rugbyniela.service.CollaboratorServiceImpl;

@RestController
@RequestMapping("/collaborator")
@RequiredArgsConstructor
public class CollaboratorController {

	private final CollaboratorServiceImpl service;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<CollaboratorResponseDTO>createCollaborator(@Valid@RequestBody CollaboratorRequestDTO dto){
		CollaboratorResponseDTO response = service.createCollaborator(dto);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete{id}")
	public ResponseEntity<CollaboratorResponseDTO>deleteCollaborator(@PathVariable Long id){
		service.deleteCollaborator(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/update{id}")
	public ResponseEntity<CollaboratorResponseDTO> updateSeason(@PathVariable Long id, @Valid@RequestBody CollaboratorRequestDTO dto){
		CollaboratorResponseDTO response = service.updateCollaborator(id, dto);
		return ResponseEntity.ok(response);
	}
}
