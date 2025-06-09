package co.edu.udistrital.mdp.caminatas.controllers.CaminatasControllers;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.CaminatasRequestDTO.CaminataRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.CaminatasResponsesDTO.CaminataResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import co.edu.udistrital.mdp.caminatas.services.CaminatasServices.CaminataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Caminatas", description = "Gestión de caminatas disponibles")
@RestController
@RequestMapping("/caminatas")
@RequiredArgsConstructor
public class CaminataController {

    private final CaminataService caminataService;

    @PreAuthorize("hasAnyRole('JURIDICO', 'SUPER_ADMIN')")
    @Operation(summary = "Crear una nueva caminata")
    @PostMapping
    public ResponseEntity<CaminataResponseDTO> create(@Valid @RequestBody CaminataRequestDTO dto) {
        CaminataEntity caminata = caminataService.createFromDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(caminataService.toResponseDTO(caminata));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_COMENTARIOS', 'NATURAL', 'JURIDICO')")
    @Operation(summary = "Obtener todas las caminatas")
    @GetMapping
    public ResponseEntity<List<CaminataResponseDTO>> getAll() {
        List<CaminataResponseDTO> response = caminataService.findAll().stream()
                .map(caminataService::toResponseDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_COMENTARIOS', 'NATURAL', 'JURIDICO')")
    @Operation(summary = "Obtener una caminata por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CaminataResponseDTO> getById(@PathVariable Long id) {
        return caminataService.findById(id)
                .map(caminataService::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_COMENTARIOS')")
    @Operation(summary = "Actualizar una caminata por ID")
    @PutMapping("/{id}")
    public ResponseEntity<CaminataResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CaminataRequestDTO dto) {
        return caminataService.updateFromDTO(id, dto)
                .map(caminataService::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Eliminar una caminata por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        caminataService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
