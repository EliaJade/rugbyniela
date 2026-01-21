package rugbyniela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rugbyniela.entity.dto.coalition.CoalitionJoinRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionJoinResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionRequestDTO;
import rugbyniela.entity.dto.coalition.CoalitionResponseDTO;
import rugbyniela.entity.dto.coalition.CoalitionSimpleResponseDTO;
import rugbyniela.service.ICoalitionService;

@RestController
@RequestMapping("/coalition")
@RequiredArgsConstructor
public class CoalitionController {

    private final ICoalitionService coalitionService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CoalitionResponseDTO> createCoalition(@Valid @RequestBody CoalitionRequestDTO dto) {
        return new ResponseEntity<>(coalitionService.createCoalition(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CoalitionResponseDTO> getCoalitionById(@PathVariable Long id) {
        return ResponseEntity.ok(coalitionService.fetchCoalitionById(id));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<CoalitionSimpleResponseDTO>> getAllCoalitions(
    		@RequestParam(required = false) Boolean active,
    		@PageableDefault(size = 10, sort = "name", direction = Direction.ASC) Pageable pageable,
            Authentication authentication // Inyectamos la auth para verificar el rol manualmente
    ) {
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            active = true;
        }
        return ResponseEntity.ok(coalitionService.fetchAllCoalitions(pageable, active));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteCoalition() {
        coalitionService.deleteCoalition();
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/join")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> requestJoin(@Valid @RequestBody CoalitionJoinRequestDTO dto) {
        coalitionService.requestJoinCoalition(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/leave")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> leaveCoalition() {
        coalitionService.leaveCoalition();
        return ResponseEntity.ok().build(); // 200 OK
    }

    @GetMapping("/requests")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Page<CoalitionJoinResponseDTO>> getPendingRequests(
    		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(coalitionService.getPendingRequests(pageable));
    }

    @PutMapping("/request/{requestId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> respondToRequest(
            @PathVariable Long requestId,
            @RequestParam Boolean accepted
    ) {
        coalitionService.respondToRequest(requestId, accepted);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> kickMember(
            @PathVariable Long userId
    ) {
        coalitionService.kickMember(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/captaincy")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> transferCaptaincy(@RequestParam Long newCapitanId) {
        coalitionService.transferCaptaincy(newCapitanId);
        return ResponseEntity.ok().build();
    }

    //TODO: teste this endpoint
    @PostMapping("/seasons/{seasonId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> registerInSeason(@PathVariable Long seasonId) {
        coalitionService.registerCoalitionInSeason(seasonId);
        return ResponseEntity.ok().build();
    }
}
