package rugbyniela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean active,
            Authentication authentication // Inyectamos la auth para verificar el rol manualmente
    ) {
        // Verificamos si tiene el rol ADMIN
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // LÓGICA DE NEGOCIO EN CAPA CONTROLADOR (Filtrado de Seguridad)
        if (!isAdmin) {
            active = true; // Forzamos a que solo vea las activas
        }

        return ResponseEntity.ok(coalitionService.fetchAllCoalitions(page, size, active));
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(coalitionService.getPendingRequests(page, size));
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

    @PostMapping("/seasons/{seasonId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> registerInSeason(@PathVariable Long seasonId) {
        coalitionService.registerCoalitionInSeason(seasonId);
        return ResponseEntity.ok().build();
    }
}
