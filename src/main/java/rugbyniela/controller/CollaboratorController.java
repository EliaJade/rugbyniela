package rugbyniela.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;
import rugbyniela.entity.dto.collaborator.CollaboratorRequestDTO;
import rugbyniela.entity.dto.collaborator.CollaboratorResponseDTO;
import rugbyniela.entity.dto.season.SeasonResponseDTO;
import rugbyniela.entity.dto.season.SeasonUpdateRequestDTO;
import rugbyniela.service.CollaboratorServiceImpl;
import rugbyniela.service.ICollaboratorService;

@Slf4j
@RestController
@RequestMapping("/collaborator")
@RequiredArgsConstructor
public class CollaboratorController {

	private final ICollaboratorService service;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<Page<CollaboratorResponseDTO>> getCollaborators(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) Boolean isActive,
			@RequestParam(required = false) String name,
			Authentication auth){
		
		boolean isAdmin = auth != null && auth.getAuthorities().stream()
				.anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		if(!isAdmin) {
			isActive=true;
		}
		return ResponseEntity.ok(service.fetchAllCollaborators(page, isActive,name));
	}
	
	 @GetMapping("/{id}")
	 @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	 public ResponseEntity<CollaboratorResponseDTO> getCollaboratorById(@PathVariable Long id) {
	    return ResponseEntity.ok(service.fetchCollaboratorById(id));
	 }
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<CollaboratorResponseDTO>createCollaborator(
			@Valid @RequestPart("collaborator") CollaboratorRequestDTO dto,
			@RequestPart(value = "file", required = false) MultipartFile file){
		CollaboratorResponseDTO response = service.createCollaborator(dto,file);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<CollaboratorResponseDTO>deleteCollaborator(@PathVariable Long id){
		log.debug("entro en delete controller");
		service.deleteCollaborator(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/update/{id}")
	public ResponseEntity<CollaboratorResponseDTO> updateSeason(
			@PathVariable Long id, 
			@Valid@RequestPart("collaborator") CollaboratorRequestDTO dto,
			@RequestPart(value = "file", required = false) MultipartFile file){
		CollaboratorResponseDTO response = service.updateCollaborator(id, dto, file);
		return ResponseEntity.ok(response);
	}
}
